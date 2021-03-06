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
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;

@DataJpaTest
class UserReviewRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserReviewRepository userReviewRepository;

    User user1;
    User user2;
    User user3; //no review
    House house;
    UserReviews userReview;

    @BeforeEach
    void setup(){
        user1 = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        user2 = new User("JaoSiuba","Joao","Silva",new GregorianCalendar(1999, Calendar.OCTOBER,25),"dummie");
        user3 = new User("Dummy","Dummy","Dummy",new GregorianCalendar(1999, Calendar.OCTOBER,25),"dummie");
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house03"
        );

        house.setOwner(user1);
        user1.addHouse(house);

        userReview = new UserReviews(4, "Toppp");
        userReview.setUserReviewed(user2);
        userReview.setUserReviewing(user1);

        user1.addMyReview(userReview);
        user2.addReview(userReview);

        testEntityManager.persist(user2);
        testEntityManager.persist(user1);
        testEntityManager.persist(house);
        testEntityManager.persist(user3);

    }

    @Test
    void getUserReviews(){
        List<UserReviews> userReviews = userReviewRepository.findByUserReviewed(user2);
        
        assertEquals(1, userReviews.size());
        assertEquals(userReviews.get(0), userReview);
    }

    @Test
    void getUserReviewsForUserWithNoReviews(){
        List<UserReviews> userReviews = userReviewRepository.findByUserReviewed(user1);
        assertEquals(0, userReviews.size());
    }

    @Test
    void getUserReviewingAndUserReviewed(){
        List<UserReviews> houseReviews = userReviewRepository.findByUserReviewingAndUserReviewed(user1, user2);
        
        assertEquals(1, houseReviews.size());
        assertEquals(houseReviews.get(0), userReview);
    }

    @Test
    void getUserReviewingAndUserReviewedNoUserREnt(){
        List<UserReviews> houseReviews = userReviewRepository.findByUserReviewingAndUserReviewed(user1, user3);
        
        assertEquals(0, houseReviews.size());
    }

    @Test
    void getUserDoneReviews(){
        List<UserReviews> userReviews = userReviewRepository.findByUserReviewing(user1);
        
        assertEquals(1, userReviews.size());
        assertEquals(userReviews.get(0), userReview);
    }

    @Test
    void getUserDoneReviewsForUserWithNoReviews(){
        List<UserReviews> userReviews = userReviewRepository.findByUserReviewing(user2);
        
        assertEquals(0, userReviews.size());
    }
}