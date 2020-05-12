package tqs.justlikehome.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.repositories.UserRepository;
import static org.mockito.ArgumentMatchers.any;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private House house;
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
        Mockito.when(userRepository.findById((long) 0)).thenReturn(user);
        Mockito.when(userRepository.findById((long) 1)).thenThrow(InvalidDateInputException.class);
        Mockito.when(userRepository.save(user)).thenReturn(user);
    }

    @Test
    public void addHouseToExistingUser(){
        house = userService.addHouseToUser(house,0);
        assertThat(house.getOwner()).isEqualTo(user);
    }

    @Test
    public void addHouseToInvalidUser(){
        assertThrows(InvalidDateInputException.class,
                ()->userService.addHouseToUser(house,1));
    }

    @Test
    public void createUser(){
        User newUser = userService.createUser(user);
        assertThat(newUser).isEqualToComparingFieldByField(user);
    }

}