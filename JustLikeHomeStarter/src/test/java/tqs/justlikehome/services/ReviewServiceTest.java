package tqs.justlikehome.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import tqs.justlikehome.dtos.HouseReviewDTO;
import tqs.justlikehome.dtos.UserReviewDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.HouseReviews;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.NoPermitionException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.HouseReviewRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;
import tqs.justlikehome.repositories.UserReviewRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    
    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private HouseRepository houseRepository;

    @Mock(lenient = true)
    private RentRepository rentRepository;

    @Mock(lenient = true)
    private HouseReviewRepository houseReviewRepository;
    
    @Mock(lenient = true)
    private UserReviewRepository userReviewRepository;
    
    @InjectMocks
    private ReviewService reviewService;

    @Spy
    private User user1; //will be owner

    @Spy
    private User user2; //will be client

    @Spy
    private User user3;

    @Spy
    private House house;


    @BeforeEach
    void setup(){

        Mockito.when(user1.getId()).thenReturn((long)0);
        Mockito.when(user2.getId()).thenReturn((long)1);
        Mockito.when(user3.getId()).thenReturn((long)2);
        Mockito.when(house.getId()).thenReturn((long)0);

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(user1);
        Mockito.when(userRepository.findById(user2.getId())).thenReturn(user2);
        Mockito.when(userRepository.findById(user3.getId())).thenReturn(user3);
        Mockito.when(userRepository.findById((long) 3)).thenReturn(null);

        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        Mockito.when(userRepository.save(user2)).thenReturn(user2);
        Mockito.when(userRepository.save(user2)).thenReturn(user3);
        Mockito.when(houseRepository.save(house)).thenReturn(house);

        List<Rent> emptyRents= new ArrayList<>();
        Mockito.when(rentRepository.findByUserAndHouse(anyLong(), anyLong())).thenReturn(emptyRents);
        Mockito.when(rentRepository.findByUserAndOwner(anyLong(), anyLong())).thenReturn(emptyRents);

        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        Rent r = new Rent(house, user2, start, end);
        List<Rent> rents= new ArrayList<>();
        rents.add(r);
        Mockito.when(rentRepository.findByUserAndHouse(user2.getId(), house.getId())).thenReturn(rents);
        Mockito.when(rentRepository.findByUserAndOwner(user2.getId(), user1.getId())).thenReturn(rents);

        List<UserReviews> ur= new ArrayList<>();
        Mockito.when(userReviewRepository.findByUserReviewingAndUserReviewed(user1, user2)).thenReturn(ur);

        List<HouseReviews> hr= new ArrayList<>();
        Mockito.when(houseReviewRepository.findByReviewerAndHouse(user2, house)).thenReturn(hr);

        Mockito.when(houseRepository.findById(house.getId())).thenReturn(house);
        Mockito.when(houseRepository.findById((long)2)).thenReturn(null);

        house.setOwner(user1);

    }

    @Test
    void addReviewToExistingHouse(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(this.user2.getId(), this.house.getId(),5,"topp");
        HouseReviews hr = reviewService.addReview(houseReviewDTO);

        assertEquals(hr.getReviewer().getId(),houseReviewDTO.getReviewerId());
        assertEquals(hr.getHouse().getId(),houseReviewDTO.getHouseId());
        assertEquals(hr.getRating(),houseReviewDTO.getRating());
        assertEquals(hr.getDescription(),houseReviewDTO.getDescription());

    }

    @Test
    void addReviewToExistingHouseTwice(){

        HouseReviews houseReview = reviewService.addReview(new HouseReviewDTO(this.user2.getId(), this.house.getId(),5,"topp"));
        
        List<HouseReviews> hr= new ArrayList<>();
        hr.add(houseReview);
        Mockito.when(houseReviewRepository.findByReviewerAndHouse(user2, house)).thenReturn(hr);

        assertThrows(NoPermitionException.class,
                ()->reviewService.addReview(new HouseReviewDTO(this.user2.getId(), this.house.getId(),5,"topp")));
    }

    @Test
    void addReviewToExistingUser(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(this.user1.getId(), this.user2.getId(),5,"topp");
        UserReviews ur = reviewService.addReview(userReviewDTO);

        assertEquals(ur.getUserReviewing().getId(),userReviewDTO.getReviewerId());
        assertEquals(ur.getUserReviewed().getId(),userReviewDTO.getReviewedId());
        assertEquals(ur.getRating(),userReviewDTO.getRating());
        assertEquals(ur.getDescription(),userReviewDTO.getDescription());

    }

    @Test
    void addReviewToExistingUserTwice(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(this.user1.getId(), this.user2.getId(),5,"topp");
        UserReviews userReview = reviewService.addReview(userReviewDTO);
        
        List<UserReviews> ur= new ArrayList<>();
        ur.add(userReview);
        Mockito.when(userReviewRepository.findByUserReviewingAndUserReviewed(user1, user2)).thenReturn(ur);

        assertThrows(NoPermitionException.class,
                ()->reviewService.addReview(new UserReviewDTO(this.user1.getId(), this.user2.getId(),5,"topp")));
    }

    @Test
    void addReviewToNonExistingUser(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(this.user1.getId(), 3, 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(userReviewDTO));

    }

    @Test
    void addReviewToNonExistingHouse(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(this.user2.getId(), 2, 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(houseReviewDTO));

    }

    @Test
    void addHouseReviewfromNonExistingUser(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(3, this.house.getId(), 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(houseReviewDTO));

    }

    @Test
    void addUserReviewfromNonExistingUser(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(3, this.user2.getId(), 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(userReviewDTO));

    }

    @Test
    void addReviewToNonRentedHouse(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(this.user3.getId(), this.house.getId(), 5, "topp");

        assertThrows(NoPermitionException.class,
                ()->reviewService.addReview(houseReviewDTO));
    }

    @Test
    void addReviewToUserThatDidntRent(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(this.user1.getId(), this.user3.getId(), 5, "topp");

        assertThrows(NoPermitionException.class,
                ()->reviewService.addReview(userReviewDTO));
    }

    @Test
    void getReviewsForHouse(){
        HouseReviews hr = new HouseReviews(user2,house,4.5,"topp");
        List<HouseReviews> hrs = new ArrayList<>();
        hrs.add(hr);
        Mockito.when(houseReviewRepository.findByHouse(house)).thenReturn(hrs);

        assertEquals(reviewService.getReviewsForHouse((long)0), hrs);
    } 
    
    
    @Test
    void getUserReviewsFromUser(){
        UserReviews ur = new UserReviews(user1,user2,4.5,"topp");
        List<UserReviews> urs = new ArrayList<>();
        urs.add(ur);
        Mockito.when(userReviewRepository.findByUserReviewing(user1)).thenReturn(urs);

        assertEquals(reviewService.getUserReviewsFromUser((long)0), urs);
    }   

    @Test
    void getReviewsForUser(){
        UserReviews ur = new UserReviews(user1,user2,4.5,"topp");
        List<UserReviews> urs = new ArrayList<>();
        urs.add(ur);
        Mockito.when(userReviewRepository.findByUserReviewed(user2)).thenReturn(urs);

        assertEquals(reviewService.getReviewsForUser((long)1), urs);
    }   

    @Test
    void getHouseReviewsFromUser(){
        HouseReviews hr = new HouseReviews(user2,house,4.5,"topp");
        List<HouseReviews> hrs = new ArrayList<>();
        hrs.add(hr);
        Mockito.when(houseReviewRepository.findByReviewer(user2)).thenReturn(hrs);

        assertEquals(reviewService.getHouseReviewsFromUser((long)1), hrs);
    }   

}