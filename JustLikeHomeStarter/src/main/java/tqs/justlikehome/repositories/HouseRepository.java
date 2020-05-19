package tqs.justlikehome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.justlikehome.entities.House;

import java.util.Date;
import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House,Long> {
    @Query("SELECT distinct(h) FROM House h LEFT JOIN h.timesRented tr "+
            "WHERE h.maxNumberOfUsers >= :numberGuests and lower(h.city)=lower(:city) and " +
            "(((tr.rentStart not between :begin and :end) or tr.rentStart is null) and ((tr.rentEnd not between :begin and :end) or tr.rentEnd is null)) or "+
            "(((tr.rentStart between :begin and :end and tr.pending=true) or tr.rentStart is null) and ((tr.rentEnd between :begin and :end and tr.pending=true) or tr.rentEnd is null))")
    List<House> searchHouse(@Param("numberGuests") Integer numberGuests,
                            @Param("city") String city,
                            @Param("begin") Date begin,
                            @Param("end") Date end);
    House findById(long userId);

    // FOR SOME REASON THIS DOESNT WORK WITH THE QUERY ABOVE
    @Query("SELECT AVG(hr.rating) FROM House h LEFT JOIN h.houseReviews hr " +
            "WHERE h.id=:houseID")
    Double getRating(@Param("houseID") long houseID);
}