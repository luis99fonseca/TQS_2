package tqs.justlikehome.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;

@DataJpaTest
public class HouseReviewRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HouseReviewRepository houseReviewRepository;

    User user1;
    User user2;
    House house;
    HouseReviews houseReview;

    @BeforeEach
    public void setup(){
        user1 = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        user2 = new User("JaoSiuba","Joao","Silva",new GregorianCalendar(1999, Calendar.OCTOBER,25));

        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house3"
        );

        house.setOwner(user1);
        user1.addHouse(house);

        houseReview = new HouseReviews(4, "Toppp");
        houseReview.setHouse(house);
        houseReview.setUser(user2);

        house.addReview(houseReview);
        user2.addMyReview(houseReview);

        testEntityManager.persist(user2);
        testEntityManager.persist(user1);
        testEntityManager.persist(house);

    }

    @Test
    public void getHouseReviews(){
        List<HouseReviews> houseReviews = houseReviewRepository.findByHouse(house);
        
        assertEquals(houseReviews.size(), 1);
        assertEquals(houseReviews.get(0), houseReview);
    }

    @Test
    public void getHouseReviewsForHouseWithNoReviews(){

        House house2 = new House(
            "Aveiro2",
            "near Ria de Aveiro",
            3.0,
            50.0,
            2,
            5,
                "house3"
        );
        
        testEntityManager.persist(house2);

        List<HouseReviews> houseReviews = houseReviewRepository.findByHouse(house2);
        
        assertEquals(houseReviews.size(), 0);

    }


    
    
}