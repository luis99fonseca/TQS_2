package tqs.justlikehome.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.services.ReviewService;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(value = "/newHouseReview")
    @ResponseBody
    public HouseReviews newHouseReview(@RequestBody HouseReviewDTO houseReviewDTO){
        return reviewService.addReview(houseReviewDTO);
    }

    @PostMapping(value = "/newUserReview")
    @ResponseBody
    public UserReviews newUserReview(@RequestBody UserReviewDTO userReviewDTO){
        return reviewService.addReview(userReviewDTO);
    }

    @PostMapping(value = "/houseReviews")
    public List<HouseReviews> getHouseReviews(@RequestParam Long id){
        return reviewService.getReviewsForHouse(id);
    }

    @PostMapping(value = "/userReviewedReviews")
    public List<UserReviews> getuserReviewedReviews(@RequestParam Long id){
        return reviewService.getReviewsForUser(id);
    }

    @PostMapping(value = "/useruserReviewerReviews")
    public List<UserReviews> getuserReviewerReviews(@RequestParam Long id){
        return reviewService.getReviewsFromUser(id);
    }
    
}