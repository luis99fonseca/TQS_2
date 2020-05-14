package tqs.justlikehome.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.services.UserService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc servlet;

    @MockBean
    UserService userService;

    @Test
    public void whenGetUserById_thenReturnUserHouses() throws Exception {
        List<House> houseList = new ArrayList<>(Arrays.asList(new House("aveiro", "very good house", 2.0, 23, 2, 2),
                new House("viseu", "very as house", 3.0, 23, 2, 2)));

        given(userService.getUserHouses(anyLong())).willReturn(
                houseList
        );

        servlet.perform(MockMvcRequestBuilders.get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].city").value("aveiro")).
                andExpect(jsonPath("$[1].city").value("viseu"));
    }

    @Test
    public void whenGetUserByNoExistentId_thenReturnUserHouses() throws Exception {

        given(userService.getUserHouses(anyLong())).willReturn(
                Collections.emptyList()
        );

        servlet.perform(MockMvcRequestBuilders.get("/userHouses/user=" + "123")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenAddNewValidUser_thenReturnUser() {
        UserDTO user = new UserDTO("joao123", "joao", "miguel", "20-12-1999");

        given(userService.createUser(user)).willReturn(new User(user));

//        servlet.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON));
    }

}