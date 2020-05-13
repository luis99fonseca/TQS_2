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
    @Query("Select h from House h LEFT JOIN h.timesRented r "+
            "WHERE h.maxNumberOfUsers >= :numberGuests and lower(h.city)=lower(:city) and " +
            "(((r.rentStart not between :begin and :end) or r.rentStart is null) and ((r.rentEnd not between :begin and :end) or r.rentEnd is null))")
    List<House> searchHouse(@Param("numberGuests") Integer numberGuests,
                            @Param("city") String city,
                            @Param("begin") Date begin,
                            @Param("end") Date end);
    House findById(long userId);

}