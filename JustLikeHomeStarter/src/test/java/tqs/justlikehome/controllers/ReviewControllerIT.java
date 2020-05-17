package tqs.justlikehome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
public class ReviewControllerIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private MockMvc mvc;


    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentRepository rentRepository;

    private User user;
    private User owner;
    private House house;


    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        owner = new User("JaoSiuba", "Joao", "Silva", new GregorianCalendar(1999, Calendar.OCTOBER,25));
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
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,user,start,end);

        house.addRent(rent);
        user.addPurchasedRent(rent);

        owner = userRepository.save(owner);
        user = userRepository.save(user);
        house = houseRepository.save(house);
    }
    
    @Test
    public void addHouseReview() throws Exception {
        HouseReviewDTO hrdto = new HouseReviewDTO(user.getId(), house.getId(), 3.0, "good");
        mvc.perform(post("/newHouseReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(hrdto)))
            .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.user.username",is("Fonsequini")))
            .andExpect(jsonPath("$.house.description",is("Incredible House near Ria de Aveiro")))
            .andExpect(jsonPath("$.house.id",is((int) house.getId())))
            .andExpect(jsonPath("$.user.id",is((int) user.getId())));
    }

    private String objectToJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }
    }
}