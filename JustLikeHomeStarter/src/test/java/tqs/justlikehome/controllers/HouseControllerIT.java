package tqs.justlikehome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
public class HouseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private House house;

    @BeforeEach
    public void resetDb(){
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
    public void whenAddValidComoditiesToHouse_thenReturnHouse() throws Exception {
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
    public void whenAddInvalidComoditiesToHouse_thenThrowException() throws Exception {
        ComoditiesDTO comoditiesDto = new ComoditiesDTO("pool", "pool to swim", -1);

        mockMvc.perform(post("/addComoditie").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(comoditiesDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenGetHouseByParameters_thenReturnOfMatchingHouses() throws Exception {

        mockMvc.perform(get("/houses/city=AVEIro&start=12-10-1999&end=12-10-1999&guests=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("aveiro"))
                .andExpect(jsonPath("$[0].ownerName").value("Fonsequini"));
    }

    @Test
    public void whenGetHouseByInvalidDate_thenThrowException() throws Exception {
        mockMvc.perform(get("/houses/city=aveiro&start=1999-10-19&end=12-10-1999&guests=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenGetSpecificHouse_withNoRatings_thenReturnHouseSearchDTO() throws Exception {

        mockMvc.perform(get("/specificHouse/houseId="+house.getId())).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.ownerName").value("Fonsequini"))
                .andExpect(jsonPath("$.userRating").value(0))
                .andExpect(jsonPath("$.rating").value(0))
                .andExpect(jsonPath("$.houseName").value(house.getHouseName()));
    }

    //TODO: Check if there is a way to inject Ratings easily :)


    private String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
