package tqs.justlikehome.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.BookMarkDTO;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private User user2;
    private House house;
    private House house2;
    @BeforeEach
    void resetDb(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        house = new House(
                "aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house2"
        );
        HouseReviews review = new HouseReviews(user2,house,5,"BERY GOOD HOUSE");
        HouseReviews review2 = new HouseReviews(user2,house,4,"BERY GOOD HOUSE");
        house.addReview(review);
        house.addReview(review2);
        user.addHouse(house);
        house.setOwner(user);
        house2 = new House(
                "aveiro",
                "boa casa",
                2.0,
                30.5,
                4,
                6,
                "Casa de Tabua"
        );
        review = new HouseReviews(user2,house2,3,"BERY GOOD HOUSE");
        review2 = new HouseReviews(user2,house2,3,"BERY GOOD HOUSE");
        house2.addReview(review);
        house2.addReview(review2);
        user.addHouse(house2);
        house2.setOwner(user);
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
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.houseName").value(house.getHouseName()));
    }

    @Test
    void whenGetInvalidSpecificHouse_withNoRatings_thenReturnHouseSearchDTO() throws Exception {
        mockMvc.perform(get("/specificHouse/houseId="+(-1))).andExpect(status().is4xxClientError());
    }

    @Test
    void whenAddBookmark_ifValid_returnBookmark() throws Exception {
        BookMarkDTO bookMarkDTO = new BookMarkDTO(user.getId(), house.getId());

        mockMvc.perform(post("/addBookmark").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(bookMarkDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.houseId").value(house.getId()))
        .andExpect(jsonPath("$.userId").value(user.getId()));

        //TODO: check this, cause now there ain't a way to verify if the add was actually done;
    }

    @Test
    void whenAddBookmark_ifInvalid_thenThrowException() throws Exception {
        BookMarkDTO bookMarkDTO = new BookMarkDTO(-1, -1);

        mockMvc.perform(post("/addBookmark").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(bookMarkDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenDeleteBookmark_ifValid_returnBookmark() throws Exception {
        BookMarkDTO bookMarkDTO = new BookMarkDTO(user.getId(), house.getId());

        // adding a bookmark previously
        mockMvc.perform(post("/addBookmark").contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(bookMarkDTO)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/deleteBookmark/userId=" + user.getId() +"&houseId=" + house.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.houseId").value(house.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()));

        //TODO: check this, cause now there ain't a way to verify if the delete was actually done;
    }

    @Test
    void whenDeleteBookmark_ifInvalid_thenThrowException() throws Exception {
        mockMvc.perform(delete("/deleteBookmark/userId=" + (-1) + "&houseId=" + (-1)))
                .andExpect(status().is4xxClientError());
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

    @Test
    void searchForTopHouses() throws Exception {
        // Checks the order and the names of the houses
        mockMvc.perform(get("/topHouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].rating").value(4.5))
                .andExpect(jsonPath("$.[0].houseName").value(house.getHouseName()))
                .andExpect(jsonPath("$.[1].rating").value(3))
                .andExpect(jsonPath("$.[1].houseName").value(house2.getHouseName()));
    }
}
