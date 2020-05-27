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
import tqs.justlikehome.dtos.UserInfoDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.InvalidPasswordException;
import tqs.justlikehome.repositories.UserRepository;

import java.util.*;

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
    private House bookmarked;
    private Rent rent;

    @BeforeEach
    void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        houseDTO = new HouseDTO(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                0, //We are testing so default value is always 0
                "house2",
                Collections.emptySet()
        );
        bookmarked = new House("Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "Hello", //We are testing so default value is always 0
                Collections.emptySet());
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        rent = new Rent(bookmarked,user,start,end);
        user.addPurchasedRent(rent);
        user.addBookmarkedHouse(bookmarked);
        Mockito.when(userRepository.findById((long) 0)).thenReturn(user);
        Mockito.when(userRepository.findById((long) 1)).thenThrow(InvalidDateInputException.class);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findById(-1)).thenReturn(null);
        Mockito.when(userRepository.getUserAvgRating((long) 0)).thenReturn((double)5);
    }

    @Test
    void addHouseToExistingUser(){
        House house = userService.addHouseToUser(houseDTO);
        assertThat(house.getOwner()).isEqualTo(user);
        assertThat(house.getComodities()).isEmpty();
    }

    @Test
    void addHouseToExistingUser_withSomeComodities(){
        Set<Comodities> tempComodities = new HashSet<>();
        tempComodities.add(new Comodities("bed", "very large"));
        tempComodities.add(new Comodities("pool", "very wet"));

        HouseDTO houseDTO2 = new HouseDTO("viseu", "very as house", 3.0, 23, 2, 2, user.getId(), "casa do bairro", tempComodities);

        House house = userService.addHouseToUser(houseDTO2);
        assertThat(house.getOwner()).isEqualTo(user);
        assertThat(house.getComodities()).hasSize(2);
    }

    @Test
    void addHouseToInvalidUser(){
        houseDTO.setUserId(1);
        assertThrows(InvalidDateInputException.class,
                ()->userService.addHouseToUser(houseDTO));
    }

    @Test
    void createUserValidDTO(){
        UserDTO userDTO = new UserDTO("josi","Joao","Silva","02-10-2019","dummie");
        User newUser = userService.createUser(userDTO);
        // Not comparing birthday because if it fails it throws exception
        assertThat(newUser.getUsername()).isEqualTo(userDTO.getUsername());
        assertThat(newUser.getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(newUser.getLastName()).isEqualTo(userDTO.getLastName());
    }

    @Test
    void createUserInvalidDTO(){
        UserDTO userDTO = new UserDTO("josi","Joao","Silva","2019-10-02","dummie");
        assertThrows(InvalidDateInputException.class,
                ()->userService.createUser(userDTO));
    }

    @Test
    void getUserInfoInvalidID(){
        assertThrows(InvalidIdException.class,
                ()->userService.getUserInfo(-1));
    }

    @Test
    void getUserInfoValidId(){
        UserInfoDTO userInfo = userService.getUserInfo(0);
        assertThat(userInfo.getBirthDate()).isEqualTo(user.getBirthDate());
        assertThat((House) userInfo.getBookmarkedHouses().toArray()[0]).isEqualToComparingFieldByField(bookmarked);
        assertThat((Rent) userInfo.getPurchasedRents().toArray()[0]).isEqualToComparingFieldByField(rent);
        assertThat(userInfo.getId()).isEqualTo(user.getId());
        assertThat(userInfo.getRating()).isEqualTo(5);
    }

    @Test
    void loginWithRightPassword(){
        Map<String,Long> logged = userService.login(0,"dummie");
        assertThat(logged.size()).isEqualTo(1);
        assertThat(logged.get("userID")).isEqualTo(0);
    }

    @Test
    void loginWithWrongPassword(){
        assertThrows(InvalidPasswordException.class,
                ()->userService.login(0,"bla"));
    }

    @Test
    void loginWithInvalidID(){
        assertThrows(InvalidIdException.class,
                ()->userService.login(-1,"bla"));
    }

}