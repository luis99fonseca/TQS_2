package tqs.justlikehome.repositories;

import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReviews,Integer>{
    UserReviews findById(long review);

    List<UserReviews> findByUserReviewed(User user);

    List<UserReviews> findByUserReviewing(User user);

    List<UserReviews> findByUserReviewingAndUserReviewed(User userReviewing,User userReviewed);

}