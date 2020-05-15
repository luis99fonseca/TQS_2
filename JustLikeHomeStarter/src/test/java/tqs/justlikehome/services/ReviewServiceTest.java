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
import tqs.justlikehome.entities.UserReviews;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    
    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private HouseRepository houseRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Spy
    private User user1; //will be owner

    @Spy
    private User user2; //will be client

    @Spy
    private House house;


    @BeforeEach
    public void setup(){

        Mockito.when(user1.getId()).thenReturn((long)0);
        Mockito.when(user2.getId()).thenReturn((long)1);
        Mockito.when(house.getId()).thenReturn((long)0);

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(user1);
        Mockito.when(userRepository.findById(user2.getId())).thenReturn(user2);
        Mockito.when(userRepository.findById((long) 2)).thenThrow(InvalidIdException.class);

        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        Mockito.when(userRepository.save(user2)).thenReturn(user2);
        Mockito.when(houseRepository.save(house)).thenReturn(house);

        Mockito.when(houseRepository.findById(house.getId())).thenReturn(house);
        house.setOwner(user1);

    }

    @Test
    public void addReviewToExistingHouse(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(this.user2.getId(), this.house.getId(),5,"topp");
        HouseReviews hr = reviewService.addReview(houseReviewDTO);

        assertEquals(hr.getUser().getId(),houseReviewDTO.getReviewerId());
        assertEquals(hr.getHouse().getId(),houseReviewDTO.getHouseId());
        assertEquals(hr.getRating(),houseReviewDTO.getRating());
        assertEquals(hr.getDescription(),houseReviewDTO.getDescription());

    }

    @Test
    public void addReviewToExistingUser(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(this.user1.getId(), this.user2.getId(),5,"topp");
        UserReviews ur = reviewService.addReview(userReviewDTO);

        assertEquals(ur.getUserReviewing().getId(),userReviewDTO.getReviewerId());
        assertEquals(ur.getUserReviewed().getId(),userReviewDTO.getReviewedId());
        assertEquals(ur.getRating(),userReviewDTO.getRating());
        assertEquals(ur.getDescription(),userReviewDTO.getDescription());

    }

    @Test
    public void addReviewToNonExistingUser(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(this.user1.getId(), 2, 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(userReviewDTO));

    }

    @Test
    public void addReviewToNonExistingHouse(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(this.user2.getId(), 1, 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(houseReviewDTO));

    }

    @Test
    public void addHouseReviewfromNonExistingUser(){

        HouseReviewDTO houseReviewDTO = new HouseReviewDTO(2, this.house.getId(), 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(houseReviewDTO));

    }

    @Test
    public void addUserReviewfromNonExistingUser(){

        UserReviewDTO userReviewDTO = new UserReviewDTO(2, this.user2.getId(), 5,"topp");

        assertThrows(InvalidIdException.class,
                ()->reviewService.addReview(userReviewDTO));

    }

}