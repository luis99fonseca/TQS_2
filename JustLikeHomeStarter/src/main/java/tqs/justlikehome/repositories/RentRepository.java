package tqs.justlikehome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent,Integer>{
    Rent findById(long rent);

    @Query("SELECT r FROM Rent r LEFT JOIN r.user u " +
            "WHERE u.id=:userID and r.pending=:pending")
    List<Rent> findByIdAndPending(@Param("userID") long userID,
                                  @Param("pending") boolean pending);

}