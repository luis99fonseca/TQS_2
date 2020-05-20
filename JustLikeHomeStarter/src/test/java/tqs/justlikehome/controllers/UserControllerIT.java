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
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;
import tqs.justlikehome.utils.ObjectJsonHelper;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        house = new House(
                "aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "houseName"
        );
        user.addHouse(house);
        house.setOwner(user);
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
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "20-12-1999");

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @Test
    void whenAddNewUserWithInvalidDate_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "1999-12-12");

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddNewUserWithInvalidUsername_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO(user.getUsername(), "joao", "miguel", "19-12-1999");

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
                .andExpect(jsonPath("$.comodities", hasSize(2)));
    }


    @Test
    void whenAddUserHouseWithInvalidParam_thenThrowException() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, -1, "casa do bairro", Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(houseDTO)))
                .andExpect(status().is4xxClientError());
    }
}
