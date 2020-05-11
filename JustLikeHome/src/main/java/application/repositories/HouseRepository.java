package application.repositories;

import application.entities.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House,Long> {
    /*@Query("Select h from House h where h.maxNumberOfUsers > :numberGuests and lower(h.city)=lower(:city)")
    List<House> searchHouse(@Param("numberGuests") Integer numberGuests,
                                   @Param("city") String city);*/
}
