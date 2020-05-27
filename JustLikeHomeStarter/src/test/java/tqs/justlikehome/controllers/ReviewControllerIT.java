package tqs.justlikehome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.JustlikehomeApplication;
import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.repositories.HouseRepository;

import tqs.justlikehome.repositories.HouseReviewRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JustlikehomeApplication.class)
@AutoConfigureMockMvc
class ReviewControllerIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private MockMvc mvc;


    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User owner;
    private House house;
    private User user2;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
        houseRepository.deleteAll();
        
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        owner = new User("JaoSiuba", "Joao", "Silva", new GregorianCalendar(1999, Calendar.OCTOBER,25),"dummie");
        user2 = new User("Dummy","dummy","dummy",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");

        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "Casinha do joao"
        );
        owner.addHouse(house);
        house.setOwner(owner);
        owner = userRepository.save(owner);
        user = userRepository.save(user);
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,user,start,end);
        rent.setPending(false);

        house.addRent(rent);
        user.addPurchasedRent(rent);

        owner = userRepository.save(owner);
        user = userRepository.save(user);
        house = houseRepository.save(house);
        user2 = userRepository.save(user2);
    }
    
    @Test
    void addHouseReview() throws Exception {
        HouseReviewDTO hrdto = new HouseReviewDTO(user.getId(), house.getId(), 3.0, "good");
        mvc.perform(post("/newHouseReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(hrdto)))
            .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.reviewer.username",is("Fonsequini")))
            .andExpect(jsonPath("$.house.description",is("Incredible House near Ria de Aveiro")))
            .andExpect(jsonPath("$.house.id",is((int) house.getId())))
            .andExpect(jsonPath("$.reviewer.id",is((int) user.getId())));
    }

    @Test
    void addHouseReviewNoRent() throws Exception{
        HouseReviewDTO hrdto = new HouseReviewDTO(user2.getId(), house.getId(), 3.0, "good");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(hrdto)))
        .andExpect(status().is4xxClientError());
    }

    @Test
    void addUserReview() throws Exception {
        UserReviewDTO urdto = new UserReviewDTO(owner.getId(), user.getId(), 3.0, "good");
        mvc.perform(post("/newUserReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(urdto)))
            .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.userReviewed.username",is("Fonsequini")))
            .andExpect(jsonPath("$.userReviewing.username",is("JaoSiuba")))
            .andExpect(jsonPath("$.userReviewing.id",is((int) owner.getId())))
            .andExpect(jsonPath("$.userReviewed.id",is((int) user.getId())));
    }

    @Test
    void addUserReviewNoRent() throws Exception{
        UserReviewDTO urdto = new UserReviewDTO(owner.getId(), user2.getId(), 3.0, "good");
        mvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(urdto)))
        .andExpect(status().is4xxClientError());
    }

    @Test
    void getHouseReviews() throws Exception {
        HouseReviews houseReview = new HouseReviews(user, house, 3.0, "good");

        user.addMyReview(houseReview);
        user = userRepository.save(user);

        mvc.perform(get("/houseReviews/house=" + house.getId()).contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$").isNotEmpty())
             .andExpect(jsonPath("$",hasSize(1)))
             .andExpect(jsonPath("$.[0].reviewer.username",is(user.getUsername())))
             .andExpect(jsonPath("$.[0].house.id",is((int)house.getId())));
    }

    @Test
    void getHouseReviewsFromUser() throws Exception {
        HouseReviews houseReview = new HouseReviews(user, house, 3.0, "good");

        user.addMyReview(houseReview);
        user = userRepository.save(user);

        mvc.perform(get("/HouseReviewsFromUser/user=" + user.getId()).contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$").isNotEmpty())
             .andExpect(jsonPath("$",hasSize(1)))
             .andExpect(jsonPath("$.[0].reviewer.username",is(user.getUsername())))
             .andExpect(jsonPath("$.[0].house.id",is((int)house.getId())));
    }

    @Test
    void getUserReviews() throws Exception {
        UserReviews userReview = new UserReviews(owner, user, 3.0, "good");

        user.addMyReview(userReview);
        user = userRepository.save(user);

        mvc.perform(get("/userReviews/user=" + user.getId()).contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$").isNotEmpty())
             .andExpect(jsonPath("$",hasSize(1)))
             .andExpect(jsonPath("$.[0].userReviewing.username",is(owner.getUsername())))
             .andExpect(jsonPath("$.[0].userReviewed.username",is(user.getUsername())));
    }

    @Test
    void getUserReviewsFromUser() throws Exception {
        UserReviews userReview = new UserReviews(owner, user, 3.0, "good");

        user.addMyReview(userReview);
        user = userRepository.save(user);

        mvc.perform(get("/UserReviewsFromUser/user=" + owner.getId()).contentType(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$").isNotEmpty())
             .andExpect(jsonPath("$",hasSize(1)))
             .andExpect(jsonPath("$.[0].userReviewing.username",is(owner.getUsername())))
             .andExpect(jsonPath("$.[0].userReviewed.username",is(user.getUsername())));
    }

    private String objectToJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }
    }
}