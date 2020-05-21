package tqs.justlikehome.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.BookMarkDTO;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    public void whenGetSpecificHouse_withNoRatings_thenReturnHouseSearchDTO() throws Exception {

        mockMvc.perform(get("/specificHouse/houseId="+house.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Fonsequini"))
                .andExpect(jsonPath("$.userRating").value(0))
                .andExpect(jsonPath("$.rating").value(0))
                .andExpect(jsonPath("$.houseName").value(house.getHouseName()));
    }

    @Test
    public void whenGetInvalidSpecificHouse_withNoRatings_thenReturnHouseSearchDTO() throws Exception {

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
        mockMvc.perform(delete("/deleteBookmark/userId=" + (-1) +"&houseId=" + (-1)))
                .andExpect(status().is4xxClientError());
    }
}
