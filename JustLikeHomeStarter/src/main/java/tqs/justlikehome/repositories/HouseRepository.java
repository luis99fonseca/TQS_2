
package tqs.justlikehome.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tqs.justlikehome.entities.House;

import java.util.Date;
import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House,Long> {
    @Query("SELECT distinct(h) from House h " +
            "WHERE h.maxNumberOfUsers >= :numberGuests and lower(h.city)=lower(:city) and h.id not in " +
            "(" +
            "SELECT h FROM Rent tr LEFT JOIN tr.house h "+
            "WHERE h.maxNumberOfUsers >= :numberGuests and lower(h.city)=lower(:city) and " +
            "(tr.pending=false and ((:begin  between tr.rentStart and tr.rentEnd) or (:end between tr.rentStart and tr.rentEnd)))" +
            ")")
    List<House> searchHouse(@Param("numberGuests") Integer numberGuests,
                            @Param("city") String city,
                            @Param("begin") Date begin,
                            @Param("end") Date end);

    House findById(long houseId);

    @Query(value="Select h,avg(hr.rating) as rating from House h LEFT JOIN h.houseReviews hr " +
            "group by h.id order by rating desc")
    List<Object[]> getTopHouses(Pageable pageable);

    // FOR SOME REASON THIS DOESNT WORK WITH THE QUERY ABOVE
    @Query("SELECT AVG(hr.rating) FROM House h LEFT JOIN h.houseReviews hr " +
            "WHERE h.id=:houseID")
    Double getRating(@Param("houseID") long houseID);

}