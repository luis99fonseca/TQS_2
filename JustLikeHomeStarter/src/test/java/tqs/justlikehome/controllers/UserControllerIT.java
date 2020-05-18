package tqs.justlikehome.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    private User user;
    private House house;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        // TODO: must fail - house has no houseName
        house = new House(
                "aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5
        );
        user.addHouse(house);
        house.setOwner(user);
        user = userRepository.save(user);
    }

    @Test
    public void whenGetUserById_thenReturnUserHouses() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/userHouses/user=" + user.getId())).
                andExpect(status().isOk()).
                andDo(MockMvcResultHandlers.print()).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].city").value("aveiro")).
                andExpect(jsonPath("$[1].city").value("viseu"));
    }
}
