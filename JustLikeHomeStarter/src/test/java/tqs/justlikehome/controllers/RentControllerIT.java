package tqs.justlikehome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
class RentControllerIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;

    // Repositories to generate entities required for a rent to be created
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentRepository rentRepository;

    private User user;
    private House house;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        rentRepository.deleteAll();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        house = new House(
                "Aveiro",
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
    void askToRentWithRightValues() throws Exception{
        RentDTO rentDTO = new RentDTO(((House) user.getOwnedHouses().toArray()[0]).getId(),user.getId(),"10-10-2019","10-10-2019");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.house.description",is("Incredible House near Ria de Aveiro")))
                .andExpect(jsonPath("$.house.pricePerNight",is(50.0)))
                .andExpect(jsonPath("$.house.numberOfBeds",is(2)))
                .andExpect(jsonPath("$.user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.pending",is(true)));
    }

    @Test
    void askToRentWithInvalidValues() throws Exception{
        RentDTO rentDTO = new RentDTO(((House) user.getOwnedHouses().toArray()[0]).getId(),user.getId(),"2019-10-20","2019-10-21");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void askToRentWithInvalidID() throws Exception{
        RentDTO rentDTO = new RentDTO(50,50,"2019-10-20","2019-10-21");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void acceptRentWithValidValues() throws Exception{
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,user,start,end);
        user.addPurchasedRent(rent);
        user = userRepository.save(user);
        long idRent = ((Rent)user.getPurchasedRents().toArray()[0]).getId();
        Map<String,Long> rentID = new HashMap<>();
        rentID.put("rentID",idRent);
        mvc.perform(put("/acceptRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.house.description",is("Incredible House near Ria de Aveiro")))
                .andExpect(jsonPath("$.house.pricePerNight",is(50.0)))
                .andExpect(jsonPath("$.house.numberOfBeds",is(2)))
                .andExpect(jsonPath("$.user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.pending",is(false)));
    }

    @Test
    void acceptRentWithInvalidValuesThenClientError() throws Exception{
        Map<String,Long> rentID = new HashMap<>();
        rentID.put("rentID",(long) 70);
        mvc.perform(put("/acceptRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentID)))
                .andExpect(status().is4xxClientError());
    }

    private String objectToJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }
    }
}
