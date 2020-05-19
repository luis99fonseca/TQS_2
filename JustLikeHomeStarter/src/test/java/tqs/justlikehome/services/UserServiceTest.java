package tqs.justlikehome.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.repositories.UserRepository;
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
    private HouseDTO houseDTO;

    @BeforeEach
    void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        houseDTO = new HouseDTO(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                0, //We are testing so default value is always 0
                "house2"
        );
        Mockito.when(userRepository.findById((long) 0)).thenReturn(user);
        Mockito.when(userRepository.findById((long) 1)).thenThrow(InvalidDateInputException.class);
        Mockito.when(userRepository.save(user)).thenReturn(user);
    }

    @Test
    void addHouseToExistingUser(){
        House house = userService.addHouseToUser(houseDTO);
        assertThat(house.getOwner()).isEqualTo(user);
    }

    @Test
    void addHouseToInvalidUser(){
        houseDTO.setUserId(1);
        assertThrows(InvalidDateInputException.class,
                ()->userService.addHouseToUser(houseDTO));
    }

    @Test
    void createUserValidDTO(){
        UserDTO userDTO = new UserDTO("josi","Joao","Silva","02-10-2019");
        User newUser = userService.createUser(userDTO);
        // Not comparing birthday because if it fails it throws exception
        assertThat(newUser.getUsername()).isEqualTo(userDTO.getUsername());
        assertThat(newUser.getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(newUser.getLastName()).isEqualTo(userDTO.getLastName());
    }

    @Test
    void createUserInvalidDTO(){
        UserDTO userDTO = new UserDTO("josi","Joao","Silva","2019-10-02");
        assertThrows(InvalidDateInputException.class,
                ()->userService.createUser(userDTO));
    }

}