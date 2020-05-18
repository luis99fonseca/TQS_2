package tqs.justlikehome.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.exceptions.InvalidIdException;
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

    @GetMapping(value = "/houseReviews/house={houseID}")
    @ResponseBody
    public List<HouseReviews> getHouseReviews(@PathVariable long houseID){
        return reviewService.getReviewsForHouse(houseID);
    }

    @GetMapping(value = "/userReviews/user={userID}")
    @ResponseBody
    public List<UserReviews> getuserReviewedReviews(@PathVariable long userID){
        return reviewService.getReviewsForUser(userID);
    }

    @GetMapping(value = "/UserReviewsFromUser/user={userID}")
    @ResponseBody
    public List<UserReviews> getuserAsReviewerUserReviews(@PathVariable long userID){
        return reviewService.getUserReviewsFromUser(userID);
    }
    
    @GetMapping(value = "/HouseReviewsFromUser/user={userID}")
    @ResponseBody
    public List<HouseReviews> getuserAsReviewerHouseReviews(@PathVariable long userID){
        return reviewService.getHouseReviewsFromUser(userID);
    }
}