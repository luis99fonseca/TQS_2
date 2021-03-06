package tqs.justlikehome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.justlikehome.entities.Rent;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent,Integer>{
    Rent findById(long rent);

    @Query("SELECT r FROM Rent r LEFT JOIN r.user u LEFT JOIN r.house h " +
            "WHERE r.pending=:pending and h.owner.id =:ownerID")
    List<Rent> findByIdAndPending(@Param("ownerID") long ownerID,
                                  @Param("pending") boolean pending);

    @Query("SELECT r FROM Rent r LEFT JOIN r.user u LEFT JOIN r.house h " +
        "WHERE r.pending=false and u.id=:userID and h.id=:houseID")
    List<Rent> findByUserAndHouse(@Param("userID") long userID,
                                  @Param("houseID") long houseID);
    
    @Query("SELECT r FROM Rent r LEFT JOIN r.user u LEFT JOIN r.house h " +
        "WHERE r.pending=false and u.id=:userID and h.owner.id =:ownerID")
    List<Rent> findByUserAndOwner(@Param("userID") long userID,
                                  @Param("ownerID") long ownerID);

}