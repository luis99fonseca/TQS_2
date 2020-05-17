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
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.services.UserService;
import tqs.justlikehome.utils.ObjectJsonHelper;

import java.time.format.DateTimeParseException;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void whenGetUserById_thenReturnUserHouses() throws Exception {
        List<House> houseList = new ArrayList<>(Arrays.asList(new House("aveiro", "very good house", 2.0, 23, 2, 2, "casa da barra"),
                new House("viseu", "very as house", 3.0, 23, 2, 2, "casa da mae")));

        given(userService.getUserHouses(anyLong())).willReturn(
                houseList
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].city").value("aveiro")).
                andExpect(jsonPath("$[1].city").value("viseu"));
    }

    @Test
    public void whenGetUserByNoExistentId_thenReturnEmptyUserHouses() throws Exception {

        given(userService.getUserHouses(anyLong())).willReturn(
                Collections.emptyList()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenAddNewValidUser_thenReturnUser() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "20-12-1999");

        given(userService.createUser(any(UserDTO.class))).willReturn(new User(userDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(ObjectJsonHelper.objectToJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @Test
    public void whenAddNewUserWithInvalidDate_thenThrowException() throws Exception {
        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "1999-12-12");

        given(userService.createUser(any(UserDTO.class))).willThrow(InvalidDateInputException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(ObjectJsonHelper.objectToJson(userDTO)))
                .andExpect(status().is4xxClientError());
    }

    //@Test //TODO: can't import such Exception
//    public void whenAddNewUserWithIUsername_thenThrowException() throws Exception {
//        UserDTO userDTO = new UserDTO("joao123", "joao", "miguel", "19-12-1999");
//
//        given(userService.createUser(any(UserDTO.class))).willThrow(DateTimeParseException.class);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON)
//                .content(ObjectJsonHelper.objectToJson(userDTO)))
//                .andExpect(status().is4xxClientError())
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    public void whenAddUserValidHouse_returnHouse() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, 0, "casa do bairro");

        given(userService.addHouseToUser(any(HouseDTO.class))).willReturn(new House(houseDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(ObjectJsonHelper.objectToJson(houseDTO)))
        .andExpect(jsonPath("$.city").value(houseDTO.getCity()))
        .andExpect(jsonPath("$.description").value(houseDTO.getDescription()));
    }

    @Test
    public void whenAddUserHouseWithInvalidParam_thenThrowException() throws Exception {
        HouseDTO houseDTO = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, 0, "casa do bairro");

        given(userService.addHouseToUser(any(HouseDTO.class))).willThrow(InvalidIdException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/newHouse").contentType(MediaType.APPLICATION_JSON)
                .content(ObjectJsonHelper.objectToJson(houseDTO)))
                .andExpect(status().is4xxClientError());
    }
}
