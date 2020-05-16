package tqs.justlikehome.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.services.HouseService;
import tqs.justlikehome.services.RentService;

import static org.mockito.BDDMockito.given;

@WebMvcTest(RentController.class)
public class HouseControllerTest {

    @MockBean
    private HouseService houseService;

    @Autowired
    private MockMvc mockMvc;

    House house;
    Comodities comodities;

    @BeforeEach
    private void setUp(){
        house = new House("aveiro", "boa casa", 2.0, 30.5, 4, 6);

    }

    @Test
    private void whenAddValidComoditiestoHouse_thenReturnHouse(){
        given(houseService.addComoditieToHouse(new ComoditiesDTO("pool", "pool to swim around")))
                .willReturn(house);

    }

}
