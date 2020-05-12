package tqs.justlikehome.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    User user;
    House house;

    @BeforeEach
    public void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5
        );
        house.setOwner(user);
        user.addHouse(house);
        testEntityManager.persistAndFlush(user);
    }

    @Test
    public void getHouseFromUserWithOneHouse(){
        List<House> houses = userRepository.getUserHouses(user.getId());
        assertThat(houses.size()).isEqualTo(1);
    }

    @Test
    public void getHouseFromUserWithNoHouse(){
        List<House> houses = userRepository.getUserHouses(50);
        assertThat(houses.size()).isEqualTo(0);
    }

    @Test
    public void getHouseFromUserWithTwoHouse(){
        House house2 = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro V2",
                4.0,
                75.0,
                2,
                3
        );
        house2.setOwner(user);
        user.addHouse(house2);
        testEntityManager.persistAndFlush(user);
        List<House> houses = userRepository.getUserHouses(user.getId());
        assertThat(houses.size()).isEqualTo(2);
    }
}