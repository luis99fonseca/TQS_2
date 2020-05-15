package tqs.justlikehome.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.UserReviews;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

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

    @Test
    public void getUserWithNoReviewsAveragedRating(){
        Double avg = userRepository.getUserAvgRating(user.getId());
        assertThat(avg).isNull();
    }

    @Test
    public void getUserWithReviewsAveragedRating(){
        User tempUser01 = new User("Motinhas","Migalhas","Motas",new GregorianCalendar(1980, Calendar.MARCH,20));
        testEntityManager.persistAndFlush(tempUser01);

        UserReviews uRev01 = new UserReviews(user, tempUser01, 4, "Bom Hospede");
        UserReviews uRev02 = new UserReviews(user, tempUser01, 5, "Bom Hospede");

        user.addReview(uRev01);
        user.addReview(uRev02);

        Double avg = userRepository.getUserAvgRating(user.getId());
        assertThat(avg).isEqualTo(4.5);
    }

    @Test
    public void checkUserReviews_whenHasNone(){
        Set<UserReviews> reviewsList = userRepository.getUserReviews(user.getId());
        assertThat(reviewsList.size()).isEqualTo(0);
    }

    @Test
    public void checkUserReviews_whenHasReviews(){
        User tempUser01 = new User("Motinhas","Migalhas","Motas",new GregorianCalendar(1980, Calendar.MARCH,20));
        testEntityManager.persistAndFlush(tempUser01);

        UserReviews uRev01 = new UserReviews(user, tempUser01, 4, "Bom Hospede");
        UserReviews uRev02 = new UserReviews(user, tempUser01, 4, "Bom Hospede");

        user.addReview(uRev01);
        user.addReview(uRev02);
        
        Set<UserReviews> reviewsList = userRepository.getUserReviews(user.getId());
        assertThat(reviewsList.size()).isEqualTo(2);
        assertThat(reviewsList).contains(uRev01);

    }
}