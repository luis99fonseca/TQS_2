package tqs.justlikehome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.services.HouseService;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseController.class)
public class HouseControllerTest {

    @MockBean
    private HouseService houseService;

    @Autowired
    private MockMvc mockMvc;

    private House house;
    private ComoditiesDTO comoditiesDto;

    @BeforeEach
    private void setUp() {
        house = new House("aveiro", "boa casa", 2.0, 30.5, 4, 6, "Casa de Tabua");
        comoditiesDto = new ComoditiesDTO("pool", "pool to swim around", 0);
        house.addComoditieToHouse(new Comodities(comoditiesDto));
    }

    @Test
    public void whenAddValidComoditiesToHouse_thenReturnHouse() throws Exception {
        given(houseService.addComoditieToHouse(any(ComoditiesDTO.class))).willReturn(house);

        mockMvc.perform(post("/addComoditie").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(comoditiesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("aveiro"))
                .andExpect(jsonPath("$.comodities").isArray())
                .andExpect(jsonPath("$.comodities[0].type").value("pool"));
    }

    @Test
    public void whenAddInvalidComoditiesToHouse_thenThrowException() throws Exception {
        given(houseService.addComoditieToHouse(any(ComoditiesDTO.class))).willThrow(InvalidIdException.class);

        mockMvc.perform(post("/addComoditie").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(comoditiesDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenGetHouseByParameters_thenReturnOfMatchingHouses() throws Exception {
        List<HouseSearchDTO> searchDTOList = new ArrayList<>();
        searchDTOList.add(new HouseSearchDTO(house, new User("Fonsequini", "Luis", "Fonseca", new GregorianCalendar(1999, Calendar.JULY, 20)), 5));
        given(houseService.getHouse("aveiro", "12-10-1999", "12-10-1999", 4))
                .willReturn(searchDTOList);

        mockMvc.perform(get("/houses/city=aveiro&start=12-10-1999&end=12-10-1999&guests=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("aveiro"))
                .andExpect(jsonPath("$[0].ownerName").value("Fonsequini"));
    }

    @Test
    public void whenGetHouseByInvalidDate_thenThrowException() throws Exception {
        List<HouseSearchDTO> searchDTOList = new ArrayList<>();
        searchDTOList.add(new HouseSearchDTO(house, new User("Fonsequini", "Luis", "Fonseca", new GregorianCalendar(1999, Calendar.JULY, 20)), 5));
        given(houseService.getHouse("aveiro", "12-10-1999", "12-10-1999", 4))
                .willThrow(InvalidDateInputException.class);

        mockMvc.perform(get("/houses/city=aveiro&start=12-10-1999&end=12-10-1999&guests=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
