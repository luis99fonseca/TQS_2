package tqs.justlikehome.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.dtos.UserInfoDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.services.UserService;
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


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void whenGetUserById_thenReturnUserHouses() throws Exception {
        List<House> houseList = new ArrayList<>(Arrays.asList(new House("aveiro", "very good house", 2.0, 23, 2, 2, "casa da barra"),
                new House("viseu", "very as house", 3.0, 23, 2, 2, "casa da mae")));

        given(userService.getUserHouses(anyLong())).willReturn(
                houseList
        );

        mockMvc.perform(get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].city").value("aveiro")).
                andExpect(jsonPath("$[1].city").value("viseu"));
    }

    @Test
    void whenGetUserByNoExistentId_thenReturnEmptyUserHouses() throws Exception {

        given(userService.getUserHouses(anyLong())).willReturn(
                Collections.emptyList()
        );

        mockMvc.perform(get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void whenAddNewValidUser_thenReturnUser() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "20-12-1999");

        given(userService.createUser(any(UserDTO.class))).willReturn(new User(userDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @Test
    void whenAddNewUserWithInvalidDate_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "1999-12-12");

        given(userService.createUser(any(UserDTO.class))).willThrow(InvalidDateInputException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddNewUserWithInvalidUsername_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "19-12-1999");

        given(userService.createUser(any(UserDTO.class))).willThrow(InvalidIdException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddUserValidHouse_returnHouse() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, 0, "casa do bairro", Collections.emptySet());

        given(userService.addHouseToUser(any(HouseDTO.class))).willReturn(new House(houseDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(jsonPath("$.city").value(houseDTO.getCity()))
                .andExpect(jsonPath("$.description").value(houseDTO.getDescription()))
                .andExpect(jsonPath("$.comodities").isEmpty());
        ;
    }

    @Test
    void whenAddUserValidHouse_withNoEmptyComodities_returnHouse() throws Exception {
        Set<Comodities> tempComodities = new HashSet<>();
        tempComodities.add(new Comodities("bed", "very large"));
        tempComodities.add(new Comodities("pool", "very wet"));

        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, 1, "casa do bairro", tempComodities);

        given(userService.addHouseToUser(any(HouseDTO.class))).willReturn(new House(houseDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(jsonPath("$.city").value(houseDTO.getCity()))
                .andExpect(jsonPath("$.description").value(houseDTO.getDescription()))
                .andExpect(jsonPath("$.comodities", hasSize(2)));
    }

    @Test
    void whenAddUserHouseWithInvalidParam_thenThrowException() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, 0, "casa do bairro", Collections.emptySet());

        given(userService.addHouseToUser(any(HouseDTO.class))).willThrow(InvalidIdException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserInfoInvalidID() throws Exception{
        given(userService.getUserInfo(-1)).willThrow(InvalidIdException.class);
        mockMvc.perform(get("/userInfo/user=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserInfoValidID() throws Exception{
        User user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        House bookmarked = new House("Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "Hello", //We are testing so default value is always 0
                Collections.emptySet());
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent rent = new Rent(bookmarked,user,start,end);
        user.addPurchasedRent(rent);
        user.addBookmarkedHouse(bookmarked);
        UserInfoDTO userInfo = new UserInfoDTO(user,5);
        given(userService.getUserInfo(any(long.class))).willReturn(userInfo);
        mockMvc.perform(get("/userInfo/user=0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.username").value("Fonsequini"))
                .andExpect(jsonPath("$.bookmarkedHouses[0].city").value("Aveiro"))
                .andExpect(jsonPath("$.bookmarkedHouses[0].description").value("Incredible House near Ria de Aveiro"))
                .andExpect(jsonPath("$.bookmarkedHouses[0].houseName").value("Hello"))
                .andExpect(jsonPath("$.purchasedRents.length()").value(1))
                .andExpect(jsonPath("$.purchasedRents[0].pending").value(true))
                .andExpect(jsonPath("$.rating").value(5));
    }
}
