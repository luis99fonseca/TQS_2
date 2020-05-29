package tqs.justlikehome.controllers;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.services.ReviewService;

import javax.validation.Valid;

@RestController
@CrossOrigin("http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(value = "/newHouseReview")
    @ResponseBody
    @ApiOperation(value="Creates a new house Review")
    public HouseReviews newHouseReview(@Valid  @RequestBody HouseReviewDTO houseReviewDTO){
        return reviewService.addReview(houseReviewDTO);
    }

    @PostMapping(value = "/newUserReview")
    @ResponseBody
    @ApiOperation(value="Creates a new user Review")
    public UserReviews newUserReview(@Valid @RequestBody UserReviewDTO userReviewDTO){
        return reviewService.addReview(userReviewDTO);
    }

    @GetMapping(value = "/houseReviews/house={houseID}")
    @ResponseBody
    @ApiOperation(value="Returns all reviews of a given house")
    public List<HouseReviews> getHouseReviews(@Valid @PathVariable long houseID){
        return reviewService.getReviewsForHouse(houseID);
    }

    @GetMapping(value = "/userReviews/user={userID}")
    @ResponseBody
    @ApiOperation(value="Returns all reviews of a given user")
    public List<UserReviews> getuserReviewedReviews(@Valid @PathVariable long userID){
        return reviewService.getReviewsForUser(userID);
    }

    @GetMapping(value = "/UserReviewsFromUser/user={userID}")
    @ResponseBody
    @ApiOperation(value="Returns all User Reviews made by a given user")
    public List<UserReviews> getuserAsReviewerUserReviews(@Valid @PathVariable long userID){
        return reviewService.getUserReviewsFromUser(userID);
    }
    
    @GetMapping(value = "/HouseReviewsFromUser/user={userID}")
    @ResponseBody
    @ApiOperation(value="Returns all House Reviews made by a given user")
    public List<HouseReviews> getuserAsReviewerHouseReviews(@Valid @PathVariable long userID){
        return reviewService.getHouseReviewsFromUser(userID);
    }
}