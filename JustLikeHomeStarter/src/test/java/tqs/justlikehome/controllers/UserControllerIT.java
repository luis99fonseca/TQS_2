package tqs.justlikehome.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.InvalidPasswordException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;
import tqs.justlikehome.utils.ObjectJsonHelper;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tqs.justlikehome.utils.ObjectJsonHelper.objectToJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    private User user;
    private House house;
    private Rent rent;
    private House bookmark;
    private User dummy;
    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        dummy = new User("Mota","Mota","Mota",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        house = new House(
                "aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "houseName"
        );
        bookmark = new House(
                "aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "houseName"
        );
        dummy.addHouse(bookmark);
        bookmark.setOwner(dummy);
        userRepository.save(dummy);
        bookmark = houseRepository.save(bookmark);
        user.addHouse(house);
        house.setOwner(user);
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        user = userRepository.save(user);
        rent = new Rent(bookmark,user,start,end);
        user.addPurchasedRent(rent);
        user.addBookmarkedHouse(bookmark);
        user = userRepository.save(user);
    }

    @Test
    void whenGetUserById_thenReturnUserHouses() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/userHouses/user=" + user.getId())).
                andExpect(status().isOk()).
                andDo(MockMvcResultHandlers.print()).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].city").value("aveiro"));
    }

    @Test
    void whenGetUserByNoExistentId_thenReturnEmptyUserHouses() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void whenAddNewValidUser_thenReturnUser() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "20-12-1999","dummie");

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @Test
    void whenAddNewUserWithInvalidDate_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "1999-12-12","dummie");

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddNewUserWithInvalidUsername_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO(user.getUsername(), "joao", "miguel", "19-12-1999","dummie");

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddUserValidHouse_returnHouse() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, user.getId(), "casa do bairro", Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(jsonPath("$.city").value(houseDTO.getCity()))
                .andExpect(jsonPath("$.description").value(houseDTO.getDescription()))
                .andExpect(jsonPath("$.comodities").isEmpty());
    }

    @Test
    void whenAddUserValidHouse_withNoEmptyComodities_returnHouse() throws Exception {
        Set<Comodities> tempComodities = new HashSet<>();
        tempComodities.add(new Comodities("bed", "very large"));
        tempComodities.add(new Comodities("pool", "very wet"));

        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, user.getId(), "casa do bairro", tempComodities);
        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(jsonPath("$.city").value(houseDTO.getCity()))
                .andExpect(jsonPath("$.description").value(houseDTO.getDescription()))
                .andExpect(jsonPath("$.comodities.length()").value(2));
    }


    @Test
    void whenAddUserHouseWithInvalidParam_thenThrowException() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, -1, "casa do bairro", Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserInfoInvalidID() throws Exception{
        mockMvc.perform(get("/userInfo/user=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserInfoValidID() throws Exception{
        mockMvc.perform(get("/userInfo/user="+user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.bookmarkedHouses[0].city").value(bookmark.getCity()))
                .andExpect(jsonPath("$.bookmarkedHouses[0].description").value(bookmark.getDescription()))
                .andExpect(jsonPath("$.bookmarkedHouses[0].houseName").value(bookmark.getHouseName()))
                .andExpect(jsonPath("$.purchasedRents.length()").value(1))
                .andExpect(jsonPath("$.purchasedRents[0].pending").value(true))
                .andExpect(jsonPath("$.purchasedRents[0].house.id").value(bookmark.getId()))
                .andExpect(jsonPath("$.rating").value(0))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void loginWithRightPassword() throws Exception {
        mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_JSON).header("id",user.getId())
                .header("password",user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userID").value(user.getId()));
    }

    @Test
    void loginWithWrongPassword() throws Exception {
        mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_JSON).header("id",user.getId())
                .header("password","notright"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void loginWithInvalidID() throws Exception {
        mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_JSON).header("id",user.getId()+1)
                .header("password",user.getPassword()))
                .andExpect(status().is4xxClientError());
    }



}
