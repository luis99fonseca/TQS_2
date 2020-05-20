package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import tqs.justlikehome.repositories.UserReviewRepository;

import java.util.List;

import javax.transaction.Transactional;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.NoPermitionException;


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
    @Autowired
    public RentRepository rentRepository;

    public HouseReviews addReview(HouseReviewDTO houseReviewDTO){
        House house= houseRepository.findById(houseReviewDTO.getHouseId());
        User user= userRepository.findById(houseReviewDTO.getReviewerId());
        if (house==null || user ==null)
            throw new InvalidIdException();

        List<Rent> rent = rentRepository.findByUserAndHouse(houseReviewDTO.getReviewerId(), houseReviewDTO.getHouseId());   //user was in house
        List<HouseReviews> housereviews = houseReviewRepository.findByReviewerAndHouse(user, house);

        if (housereviews.size() >= rent.size()){
            throw new NoPermitionException();
        }

        HouseReviews houseReview = new HouseReviews(houseReviewDTO);
        user.addMyReview(houseReview);

        houseReview.setHouse(house);
        houseReview.setReviewer(user);

        houseRepository.save(house);
        userRepository.save(user);

        return houseReview;
    }

    public UserReviews addReview(UserReviewDTO userReviewDTO){

        User reviwedUser= userRepository.findById(userReviewDTO.getReviewedId());
        User reviwerUser= userRepository.findById(userReviewDTO.getReviewerId());


        if (reviwedUser==null || reviwerUser == null){
            throw new InvalidIdException();
        }

        List<Rent> rent = rentRepository.findByUserAndOwner(userReviewDTO.getReviewedId(), userReviewDTO.getReviewerId()); //owner had user as client
        List<UserReviews> usereviews = userReviewRepository.findByUserReviewingAndUserReviewed(reviwerUser, reviwedUser);

        if (usereviews.size() >= rent.size()){
            throw new NoPermitionException();
        }

        UserReviews userReview = new UserReviews(userReviewDTO);
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

    public List<UserReviews> getUserReviewsFromUser(long id){
        User user = userRepository.findById(id);
        return userReviewRepository.findByUserReviewing(user);
    }

    public List<HouseReviews> getHouseReviewsFromUser(long id){
        User user = userRepository.findById(id);
        return houseReviewRepository.findByReviewer(user);
    }

}