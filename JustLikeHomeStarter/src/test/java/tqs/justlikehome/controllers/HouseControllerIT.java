package tqs.justlikehome.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tqs.justlikehome.utils.ObjectJsonHelper.objectToJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
class HouseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private House house;

    @BeforeEach
    void resetDb(){
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
                "house2"
        );
        user.addHouse(house);
        house.setOwner(user);
        user = userRepository.save(user);
    }

    @Test
    void whenAddValidComoditiesToHouse_thenReturnHouse() throws Exception {
        // TODO: check here, as ID starts at 1, which i'm not sure if its intended as its the first house saved ever
        // the way to make the ID dinamic was getting it directly, which i'm not sure if its good practice
        ComoditiesDTO comoditiesDto = new ComoditiesDTO("pool", "pool to swim", houseRepository.findAll().get(0).getId());

        mockMvc.perform(post("/addComoditie").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(comoditiesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("aveiro"))
                .andExpect(jsonPath("$.comodities").isArray())
                .andExpect(jsonPath("$.comodities[0].type").value("pool"));
    }

    @Test
    void whenAddInvalidComoditiesToHouse_thenThrowException() throws Exception {
        ComoditiesDTO comoditiesDto = new ComoditiesDTO("pool", "pool to swim", -1);

        mockMvc.perform(post("/addComoditie").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(comoditiesDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetHouseByParameters_thenReturnOfMatchingHouses() throws Exception {

        mockMvc.perform(get("/houses/city=AVEIro&start=12-10-1999&end=12-10-1999&guests=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("aveiro"))
                .andExpect(jsonPath("$[0].ownerName").value("Fonsequini"));
    }

    @Test
    void whenGetHouseByInvalidDate_thenThrowException() throws Exception {
        mockMvc.perform(get("/houses/city=aveiro&start=1999-10-19&end=12-10-1999&guests=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenGetSpecificHouse_withNoRatings_thenReturnHouseSearchDTO() throws Exception {

        mockMvc.perform(get("/specificHouse/houseId="+house.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Fonsequini"))
                .andExpect(jsonPath("$.userRating").value(0))
                .andExpect(jsonPath("$.rating").value(0))
                .andExpect(jsonPath("$.houseName").value(house.getHouseName()));

        mockMvc.perform(get("/specificHouse/houseId="+house.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Fonsequini"))
                .andExpect(jsonPath("$.userRating").value(0))
                .andExpect(jsonPath("$.rating").value(0))
                .andExpect(jsonPath("$.houseName").value(house.getHouseName()));
    }

    @Test
    void updateHouse() throws Exception {

        HouseDTO housedto = new HouseDTO("aveiro", "boa casa", 2.0, 50.0, 4, 6, user.getId(), "Casa de Tabua", Collections.emptySet());
        housedto.setHouseId(house.getId());

        mockMvc.perform(put("/updateHouse").contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(housedto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.houseName").value("Casa de Tabua"));

        assertEquals("Casa de Tabua",houseRepository.findById(house.getId()).getHouseName());

    }

    @Test
    void updateHouse_Invalid() throws Exception {
        HouseDTO housedto = new HouseDTO("aveiro", "boa casa", 2.0, 50.0, 4, 6, user.getId(), "Casa de Tabua",Collections.emptySet());
        housedto.setHouseId((long)50);

        mockMvc.perform(put("/updateHouse").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(housedto)))
                .andExpect(status().is4xxClientError());
    }
}
