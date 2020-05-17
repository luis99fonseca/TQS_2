package tqs.justlikehome.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;

@Repository
public interface HouseReviewRepository extends JpaRepository<HouseReviews,Integer>{

    HouseReviews findById(long review);

    List<HouseReviews> findByHouse(House house);

    List<HouseReviews> findByUser(User user);

}