package tqs.justlikehome.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.NoPermitionException;
import tqs.justlikehome.services.ReviewService;
import static org.mockito.BDDMockito.given;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {
    
    @MockBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    private User owner;
    private User user;
    private House house;
    private HouseReviews houseReview;
    private UserReviews userReview;

    @BeforeEach
    private void setup(){
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
        house.setOwner(owner);

        houseReview = new HouseReviews(user, house, 5, "topp");
        userReview = new UserReviews(owner, user, 5, "arrumado");
    }

    @Test
    public void newHouseReview() throws Exception {
        HouseReviewDTO hrdto= new HouseReviewDTO(0, 0, 5, "topp");
        given(reviewService.addReview(any(HouseReviewDTO.class))).willReturn(houseReview);
        mockMvc.perform(post("/newHouseReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(hrdto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.user.username",is("Fonsequini")))
            .andExpect(jsonPath("$.house.description",is("Incredible House near Ria de Aveiro")))
            .andExpect(jsonPath("$.house.id",is(0)));
    }

    @Test
    public void newHouseReviewInvalid() throws Exception{
        HouseReviewDTO hrdto = new HouseReviewDTO(0, 0, 5, "topp");
        given(reviewService.addReview(any(HouseReviewDTO.class))).willThrow(InvalidIdException.class);
        mockMvc.perform(post("/newHouseReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(hrdto)))
        .andExpect(status().is4xxClientError());
    }

    @Test
    public void newHouseReviewNoPermission() throws Exception{
        HouseReviewDTO hrdto = new HouseReviewDTO(0, 0, 5, "topp");
        given(reviewService.addReview(any(HouseReviewDTO.class))).willThrow(NoPermitionException.class);
        mockMvc.perform(post("/newHouseReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(hrdto)))
        .andExpect(status().is4xxClientError());
    }


    @Test
    public void newUserReview() throws Exception {
        UserReviewDTO urdto= new UserReviewDTO(0, 0, 5, "topp");
        given(reviewService.addReview(any(UserReviewDTO.class))).willReturn(userReview);
        mockMvc.perform(post("/newUserReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(urdto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.userReviewed.username",is("Fonsequini")))
            .andExpect(jsonPath("$.userReviewing.username",is("JaoSiuba")));
    }

    @Test
    public void newUserReviewInvalid() throws Exception{
        UserReviewDTO urdto = new UserReviewDTO(0, 0, 5, "topp");
        given(reviewService.addReview(any(UserReviewDTO.class))).willThrow(InvalidIdException.class);
        mockMvc.perform(post("/newUserReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(urdto)))
        .andExpect(status().is4xxClientError());
    }

    @Test
    public void newUserReviewNoPermission() throws Exception{
        UserReviewDTO urdto = new UserReviewDTO(0, 0, 5, "topp");
        given(reviewService.addReview(any(UserReviewDTO.class))).willThrow(NoPermitionException.class);
        mockMvc.perform(post("/newUserReview").contentType(MediaType.APPLICATION_JSON).content(objectToJson(urdto)))
        .andExpect(status().is4xxClientError());
    }

    @Test
    public void getHouseReviews() throws Exception{
        List<HouseReviews> revList = new ArrayList<>();
        revList.add(houseReview);
        given(reviewService.getReviewsForHouse((long) 0)).willReturn(revList);
        mockMvc.perform(get("/houseReviews/house="+0).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$.[0].user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.[0].house.description",is("Incredible House near Ria de Aveiro")))
                .andExpect(jsonPath("$.[0].house.id",is(0)));
    }

    @Test
    public void getUserReviews() throws Exception{
        List<UserReviews> revList = new ArrayList<>();
        revList.add(userReview);
        given(reviewService.getReviewsForUser((long) 0)).willReturn(revList);
        mockMvc.perform(get("/userReviews/user="+0).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$.[0].userReviewed.username",is("Fonsequini")))
                .andExpect(jsonPath("$.[0].userReviewing.username",is("JaoSiuba")));
    }

    @Test
    public void getUserDoneHouseReviews() throws Exception{
        List<HouseReviews> revList = new ArrayList<>();
        revList.add(houseReview);
        given(reviewService.getHouseReviewsFromUser((long) 0)).willReturn(revList);
        mockMvc.perform(get("/HouseReviewsFromUser/user="+0).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$.[0].user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.[0].house.description",is("Incredible House near Ria de Aveiro")))
                .andExpect(jsonPath("$.[0].house.id",is(0)));
    }

    @Test
    public void getUserDoneUserReviews() throws Exception{
        List<UserReviews> revList = new ArrayList<>();
        revList.add(userReview);
        given(reviewService.getUserReviewsFromUser((long) 1)).willReturn(revList);
        mockMvc.perform(get("/UserReviewsFromUser/user="+1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$.[0].userReviewed.username",is("Fonsequini")))
                .andExpect(jsonPath("$.[0].userReviewing.username",is("JaoSiuba")));
    }

    private String objectToJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }
    }
}