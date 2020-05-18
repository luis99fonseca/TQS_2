package tqs.justlikehome.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.services.ReviewService;

@RestController
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(value = "/newHouseReview")
    @ResponseBody
    public HouseReviews newHouseReview(@RequestBody HouseReviewDTO houseReviewDTO) throws InvalidIdException{
        return reviewService.addReview(houseReviewDTO);
    }

    @PostMapping(value = "/newUserReview")
    @ResponseBody
    public UserReviews newUserReview(@RequestBody UserReviewDTO userReviewDTO) throws InvalidIdException{
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