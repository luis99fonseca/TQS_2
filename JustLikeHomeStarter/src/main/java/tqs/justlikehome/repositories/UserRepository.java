package tqs.justlikehome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(long id);

    @Query("SELECT u.ownedHouses FROM User u " +
            "WHERE u.id=:userId")
    List<House> getUserHouses(@Param("userId") long userId);

    @Query("SELECT AVG(ur.rating) FROM User u LEFT JOIN u.userReviewed ur WHERE u.id=:userId")
    Double getUserAvgRating(@Param("userId") long userId);

    @Query("SELECT u.userReviews from User u WHERE u.id=:userId")
    Set<UserReviews> getUserReviews(@Param("userId") long userId);
}
