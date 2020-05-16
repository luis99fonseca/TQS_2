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
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
//        houseRepository.deleteAll();
//        houseRepository.save(new House("aveiro", "boa casa", 2.0, 30.5, 4, 6, "Casa de Tabua"));;
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
        ComoditiesDTO comoditiesDto = new ComoditiesDTO("pool", "pool to swim", 1);

        mockMvc.perform(post("/addComoditie").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(comoditiesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("aveiro"))
                .andExpect(jsonPath("$.comodities").isArray())
                .andExpect(jsonPath("$.comodities[0].type").value("pool"));
    }

    private String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
