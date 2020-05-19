package tqs.justlikehome.repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RentRepository rentRepository;

    Rent rent01;
    Rent rent02;
    User user;
    House house;
    User owner;

    @BeforeEach
    void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        owner = new User("Owner","Luis","Fonseca2",new GregorianCalendar(1999, Calendar.JULY,20));

        testEntityManager.persistAndFlush(user);
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house03"
        );
        house.setOwner(owner);

        testEntityManager.persistAndFlush(owner);
        testEntityManager.persistAndFlush(house);

        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        rent01 = new Rent(house,user,start,end);
        rent02 = new Rent(house,user,start,end);
        testEntityManager.persistAndFlush(rent01);
        testEntityManager.persistAndFlush(rent02);
    }

    @Test
    void searchByIdThenGetRent(){
        Rent rent = rentRepository.findById(rent01.getId());
        assertThat(rent).isEqualToComparingFieldByField(rent01);
    }

    @Test
    void searchPendingRentsWhenThereAreZeroPending(){
        // No pending rents then expect empty list
        rent01.setPending(false);
        rent02.setPending(false);
        testEntityManager.persistAndFlush(rent01);
        testEntityManager.persistAndFlush(rent02);
        List<Rent> rent = rentRepository.findByIdAndPending(user.getId(),true);
        assertThat(rent.size()).isEqualTo(0);
    }

    @Test
    void searchPendingRentsWhenThereArePendingRents(){
        // two pending rent01 and rent02
        List<Rent> rent = rentRepository.findByIdAndPending(user.getId(),true);
        assertThat(rent.size()).isEqualTo(2);
    }

    @Test
    void searchPendingRentsWhenThereIsOnlyOnePending(){
        // one pending then get the other
        rent01.setPending(false);
        testEntityManager.persistAndFlush(rent01);
        List<Rent> rent = rentRepository.findByIdAndPending(user.getId(),true);
        assertThat(rent.size()).isEqualTo(1);
        assertThat(rent.get(0)).isEqualToComparingFieldByField(rent02);
    }

    @Test
    void searchPendingRentsWhenThereIsOnlyOneNotPending(){
        // one pending then get the other
        rent01.setPending(false);
        testEntityManager.persistAndFlush(rent01);
        List<Rent> rent = rentRepository.findByIdAndPending(user.getId(),false);
        assertThat(rent.size()).isEqualTo(1);
        assertThat(rent.get(0)).isEqualToComparingFieldByField(rent01);
    }

    @Test
    void searchByUserAndHouse(){
        List<Rent> rents = rentRepository.findByUserAndHouse(user.getId(), house.getId());
        assertEquals(2,rents.size());

        assertThat(rents).contains(rent01);
        assertThat(rents).contains(rent02);

    }

    @Test
    void searchByUserAndHouseShouldBeEmpty(){
        User user2 = new User("Fonsequini2","Luis2","Fonseca2",new GregorianCalendar(1999, Calendar.JULY,20));
        List<Rent> rents = rentRepository.findByUserAndHouse(user2.getId(), house.getId());
        assertEquals(0,rents.size());
    }

    
    @Test
    void searchByUserAndOwnerShouldBeEmpty(){
        User user2 = new User("Fonsequini2","Luis2","Fonseca2",new GregorianCalendar(1999, Calendar.JULY,20));
        List<Rent> rents = rentRepository.findByUserAndOwner(user2.getId(), owner.getId());
        assertEquals(0, rents.size());
    }
        
    @Test
    void searchByUserAndOwner(){
        List<Rent> rents = rentRepository.findByUserAndOwner(user.getId(), owner.getId());
        assertEquals(2, rents.size());

        assertThat(rents).contains(rent01);
        assertThat(rents).contains(rent02);
    }

}