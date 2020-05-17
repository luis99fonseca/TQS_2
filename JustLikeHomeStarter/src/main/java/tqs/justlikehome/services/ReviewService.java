package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.HouseReviewRepository;
import tqs.justlikehome.repositories.UserRepository;
import tqs.justlikehome.repositories.UserReviewRepository;

import java.util.List;

import javax.transaction.Transactional;
import tqs.justlikehome.exceptions.InvalidIdException;

@Service
@Transactional
public class ReviewService {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public HouseRepository houseRepository;
    @Autowired
    public HouseReviewRepository houseReviewRepository;
    @Autowired
    public UserReviewRepository userReviewRepository;

    public HouseReviews addReview(HouseReviewDTO houseReviewDTO){
        House house = houseRepository.findById(houseReviewDTO.getHouseId());
        if(house==null){
            throw new InvalidIdException();
        }
        
        User user = userRepository.findById(houseReviewDTO.getReviewerId());
        if (user==null){
            throw new InvalidIdException();
        }

        //TODO check if rent user and house existed

        HouseReviews houseReview = new HouseReviews(houseReviewDTO);
        house.addReview(houseReview);
        user.addMyReview(houseReview);

        houseReview.setHouse(house);
        houseReview.setUser(user);

        houseRepository.save(house);
        userRepository.save(user);

        return houseReview;
    }

    public UserReviews addReview(UserReviewDTO userReviewDTO){
        User reviwedUser = userRepository.findById(userReviewDTO.getReviewedId());
        if(reviwedUser==null){
            throw new InvalidIdException();
        }

        User reviwerUser = userRepository.findById(userReviewDTO.getReviewerId());
        if (reviwerUser==null){
            throw new InvalidIdException();
        }

        //TODO check if rent user and house existed

        UserReviews userReview = new UserReviews(userReviewDTO);
        reviwedUser.addReview(userReview);
        reviwerUser.addMyReview(userReview);

        userReview.setUserReviewed(reviwedUser);
        userReview.setUserReviewing(reviwerUser);

        userRepository.save(reviwedUser);
        userRepository.save(reviwerUser);

        return userReview;
    }

    public List<HouseReviews> getReviewsForHouse(long id){
        House house = houseRepository.findById(id);
        return houseReviewRepository.findByHouse(house);
    }

    public List<UserReviews> getReviewsForUser(long id){
        User user = userRepository.findById(id);
        return userReviewRepository.findByUserReviewed(user);
    }

    public List<UserReviews> getReviewsFromUser(long id){
        User user = userRepository.findById(id);
        return userReviewRepository.findByUserReviewing(user);
    }

}