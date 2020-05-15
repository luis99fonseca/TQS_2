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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
public class RentControllerIT {
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
    private User owner;

    @BeforeEach
    private void setup(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        rentRepository.deleteAll();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        owner = new User("Owner","Luis","Fonseca2",new GregorianCalendar(1999, Calendar.JULY,20));
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5
        );
        owner.addHouse(house);
        house.setOwner(owner);
        owner = userRepository.save(owner);
        user = userRepository.save(user);
    }

    @Test
    public void askToRentWithRightValues() throws Exception{
        RentDTO rentDTO = new RentDTO(((House) owner.getOwnedHouses().toArray()[0]).getId(),user.getId(),"10-10-2019","10-10-2019");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.house.description",is("Incredible House near Ria de Aveiro")))
                .andExpect(jsonPath("$.house.pricePerNight",is(50.0)))
                .andExpect(jsonPath("$.house.numberOfBeds",is(2)))
                .andExpect(jsonPath("$.user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.pending",is(true)));
    }

    @Test
    public void askToRentWithInvalidValues() throws Exception{
        RentDTO rentDTO = new RentDTO(((House) owner.getOwnedHouses().toArray()[0]).getId(),user.getId(),"2019-10-20","2019-10-21");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void ownerTriesToRentOwnHouseThenException() throws Exception{
        RentDTO rentDTO = new RentDTO(((House) owner.getOwnedHouses().toArray()[0]).getId(),owner.getId(),"2019-10-20","2019-10-21");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void askToRentWithInvalidID() throws Exception{
        RentDTO rentDTO = new RentDTO(50,50,"2019-10-20","2019-10-21");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void acceptRentWithValidValues() throws Exception{
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
    public void acceptRentWithInvalidValuesThenClientError() throws Exception{
        Map<String,Long> rentID = new HashMap<>();
        rentID.put("rentID",(long) 70);
        mvc.perform(put("/acceptRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentID)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getPendingFromOwnerWithHouses() throws Exception{
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,user,start,end);
        owner.addPurchasedRent(rent);
        userRepository.save(owner);
        mvc.perform(get("/pendingRents/owner="+owner.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].pending",is(true)))
                .andExpect(jsonPath("$[0].user.username",is("Fonsequini")))
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getPendingFromOwnerWithoutHouses() throws Exception{
        mvc.perform(get("/pendingRents/owner="+user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }
    
    @Test
    public void getOnGoingFromOwnerWithHouses() throws Exception{
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,user,start,end);
        rent.setPending(false);
        owner.addPurchasedRent(rent);
        userRepository.save(owner);
        mvc.perform(get("/onGoingRents/owner="+owner.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].pending",is(false)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].user.username",is("Fonsequini")));
    }

    @Test
    public void getOnGoingFromOwnerWithoutHouses() throws Exception{
        mvc.perform(get("/onGoingRents/owner="+user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }


    private String objectToJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }
    }
}
