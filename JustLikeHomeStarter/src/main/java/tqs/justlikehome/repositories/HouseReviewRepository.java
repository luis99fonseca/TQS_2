package tqs.justlikehome.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;

@Repository
public interface HouseReviewRepository extends JpaRepository<HouseReviews,Integer>{

    HouseReviews findById(long review);

    List<HouseReviews> findByHouse(House house);

    List<HouseReviews> findByReviewer(User user);


    List<HouseReviews> findByReviewerAndHouse(User reviewer, House house);

}