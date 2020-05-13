package tqs.justlikehome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(Long id);

    @Query("SELECT u.ownedHouses FROM User u " +
            "WHERE u.id=:userId")
    List<House> getUserHouses(@Param("userId") long userId);
}
