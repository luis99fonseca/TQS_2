package tqs.justlikehome.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.justlikehome.entities.*;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@DataJpaTest
class HouseRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HouseRepository houseRepository;

    User user;
    House house;
    House house2;
    Set<Comodities> comodities;

    @BeforeEach
    void setup(){
        testEntityManager.clear();
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        testEntityManager.persistAndFlush(user);
        Comodities comoditie = new Comodities("fun","Pool with jacuzzi");
        testEntityManager.persistAndFlush(comoditie);
        comodities = new HashSet<>();
        comodities.add(comoditie);
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house3"
        );
        HouseReviews review = new HouseReviews(user,house,5,"BERY GOOD HOUSE");
        HouseReviews review2 = new HouseReviews(user,house,4,"BERY GOOD HOUSE");
        house.setOwner(user);
        house.addReview(review);
        house.addReview(review2);
        testEntityManager.persistAndFlush(house);
    }

    @Test
    void searchForHouseWithCorrectValuesAndNoPastRent(){
        Date fromTime = Date.from(new GregorianCalendar(2010, Calendar.MARCH,1).toZonedDateTime().toInstant());
        Date toTime = Date.from(new GregorianCalendar(2010, Calendar.MARCH,3).toZonedDateTime().toInstant());
        List<House> houses = houseRepository.searchHouse(4,"Aveiro",fromTime,toTime);
        assertThat(houses.get(0)).isEqualToComparingFieldByField(house);
        assertThat(houses.get(0).getOwner()).isEqualToComparingFieldByField(user);
    }

    @Test
    void searchForHouseWithCorrectValuesWithPastRent(){
        // Create user to rent the house
        User userRenting = new User("Motita","Miguel","Mota",new GregorianCalendar(1999, Calendar.MARCH,10));
        testEntityManager.persistAndFlush(userRenting);
        // Create rent
        Date fromTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,25).toZonedDateTime().toInstant());
        Date toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,27).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,userRenting,fromTime,toTime);
        house.addRent(rent);
        testEntityManager.persistAndFlush(rent);
        fromTime = Date.from(new GregorianCalendar(2010, Calendar.MARCH,1).toZonedDateTime().toInstant());
        toTime = Date.from(new GregorianCalendar(2010, Calendar.MARCH,3).toZonedDateTime().toInstant());
        List<House> houses = houseRepository.searchHouse(5,"Aveiro",fromTime,toTime);

        // Should return the same house because it still is available
        assertThat(houses.get(0)).isEqualToComparingFieldByField(house);
        assertThat(houses.get(0).getOwner()).isEqualToComparingFieldByField(user);
    }

    @Test
    void searchForHouseWithCorrectValuesWithOnGoingRent(){
        // Create user to rent the house
        User userRenting = new User("Motita","Miguel","Mota",new GregorianCalendar(1999,2,10));
        testEntityManager.persistAndFlush(userRenting);
        // Create rent
        Date fromTime = Date.from(new GregorianCalendar(2010, Calendar.JANUARY,28).toZonedDateTime().toInstant());
        Date toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,3).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,userRenting,fromTime,toTime);
        rent.setPending(false);
        house.addRent(rent);
        testEntityManager.persistAndFlush(house);
        fromTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,1).toZonedDateTime().toInstant());
        toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,4).toZonedDateTime().toInstant());
        List<House> houses = houseRepository.searchHouse(3,"Aveiro",fromTime,toTime);
        // Should return no house because no house is available
        assertThat(houses.size()).isEqualTo(0);
    }

    @Test
    void searchForHouseWithCorrectValuesWithOnGoingRentWithAnotherHouseAvailable(){
        // Create user to rent the house
        User userRenting = new User("Motita","Miguel","Mota",new GregorianCalendar(1999,Calendar.FEBRUARY,10));
        testEntityManager.persistAndFlush(userRenting);
        // Create rent
        Date fromTime = Date.from(new GregorianCalendar(2010, Calendar.JANUARY,31).toZonedDateTime().toInstant());
        Date toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,4).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,userRenting,fromTime,toTime);
        rent.setPending(false);
        house.addRent(rent);
        testEntityManager.persistAndFlush(house);
        house2 = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro V2",
                4.0,
                75.0,
                2,
                3,
                "house3"
        );
        house2.setOwner(user);
        testEntityManager.persistAndFlush(house2);
        fromTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,1).toZonedDateTime().toInstant());
        toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,4).toZonedDateTime().toInstant());
        List<House> allHouses = houseRepository.searchHouse(3,"Aveiro",fromTime,toTime);
        // Should return house2 because it still is available, since house1 is not available
        assertThat(allHouses.size()).isEqualTo(1);
        assertThat(allHouses.get(0)).isEqualToComparingFieldByField(house2);
        assertThat(allHouses.get(0).getOwner()).isEqualToComparingFieldByField(user);

    }

    @Test
    void searchForHouseWithCorrectValuesWithPendingRentWithAnotherHouseAvailable() {
        // Create user to rent the house
        User userRenting = new User("Motita","Miguel","Mota",new GregorianCalendar(1999,Calendar.FEBRUARY,10));
        testEntityManager.persistAndFlush(userRenting);
        // Create rent
        Date fromTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,2).toZonedDateTime().toInstant());
        Date toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,4).toZonedDateTime().toInstant());
        Rent rent = new Rent(house,userRenting,fromTime,toTime);
        house.addRent(rent);
        testEntityManager.persistAndFlush(house);
        house2 = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro V2",
                4.0,
                75.0,
                2,
                3,
                "house3"
        );
        house2.setOwner(user);
        testEntityManager.persistAndFlush(house2);
        fromTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,1).toZonedDateTime().toInstant());
        toTime = Date.from(new GregorianCalendar(2010, Calendar.FEBRUARY,4).toZonedDateTime().toInstant());
        List<House> allHouses = houseRepository.searchHouse(3,"Aveiro",fromTime,toTime);
        // Should return house2 because it still is available, since house1 is not available
        assertThat(allHouses.size()).isEqualTo(2);
        assertThat(allHouses.get(0)).isEqualToComparingFieldByField(house);
        assertThat(allHouses.get(0).getOwner()).isEqualToComparingFieldByField(user);
        assertThat(allHouses.get(1)).isEqualToComparingFieldByField(house2);
        assertThat(allHouses.get(1).getOwner()).isEqualToComparingFieldByField(user);
    }


    @Test
    void searchForHouseByValidID(){
        House tempHouse = houseRepository.findById(house.getId());
        assertThat(tempHouse).isEqualToComparingFieldByField(house);
    }

    @Test
    void searchForHouseByInvalidId(){
        House tempHouse = houseRepository.findById(50);
        assertThat(tempHouse).isNull();
    }

    @Test
    void searchForHouseRating(){
        assertThat(houseRepository.getRating(house.getId())).isEqualTo(4.5);
    }

    @Test
    void searchForRatingWithHouseWithNoReviews(){
        assertThat(houseRepository.getRating(50)).isNull();
    }
}
