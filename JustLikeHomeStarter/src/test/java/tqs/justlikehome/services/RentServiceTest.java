package tqs.justlikehome.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import static org.assertj.core.api.Assertions.assertThat;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.NoPermissionException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private HouseRepository houseRepository;

    @Mock(lenient = true)
    private RentRepository rentRepository;

    @InjectMocks
    private RentService rentService;

    private User user;
    private User userRenting;
    private House house;
    private Rent rentPending;
    private Rent rentOnGoing;
    private Rent rentPending2;

    @BeforeEach
    void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        user.setId(0);
        userRenting = new User("Mota","Mota","Mota",new GregorianCalendar(1999, Calendar.JULY,20),"dummie");
        userRenting.setId(1);
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house2"
        );
        house.setOwner(user);
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        rentPending = new Rent(house,user,start,end);
        rentPending2 = new Rent(house,user,start,end);
        rentOnGoing = new Rent(house,user,start,end);
        rentOnGoing.setPending(false);
        List<Rent> rentList = new ArrayList<>();
        rentList.add(rentPending);
        rentList.add(rentPending2);
        List<Rent> rentListNotPending = new ArrayList<>();
        rentListNotPending.add(rentOnGoing);
        Mockito.when(userRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(houseRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(userRepository.findById((long) 0)).thenReturn(user);
        Mockito.when(userRepository.findById((long) 1)).thenReturn(userRenting);
        Mockito.when(houseRepository.findById((long) 0)).thenReturn(house);
        Mockito.when(rentRepository.findById((long) 0)).thenReturn(rentPending);
        Mockito.when(rentRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(rentRepository.findByIdAndPending(0,true)).thenReturn(rentList);
        Mockito.when(rentRepository.findByIdAndPending(0,false)).thenReturn(rentListNotPending);
    }

    @Test
    void addRentToValidHouseUser(){
        RentDTO rentDTO = new RentDTO(0,1,"10-10-2019","11-10-2019");
        Rent newRent = rentService.askToRent(rentDTO);
        assertThat(newRent.getHouse().getId()).isEqualTo(rentDTO.getHouseID());
        assertThat(newRent.getHouse().getId()).isEqualTo(rentDTO.getHouseID());
        assertThat(newRent.getPending()).isEqualTo(true);
    }

    @Test
    void addRentToInvalidHouseOrUserExceptException(){
        RentDTO rentDTO = new RentDTO(50,50,"10-10-2019","11-10-2019");
        assertThrows(InvalidIdException.class,
                ()->rentService.askToRent(rentDTO));
    }

    @Test
    void addRentWithInvalidDate(){
        RentDTO rentDTO = new RentDTO(0,1,"2019-10-20","2019-11-10");
        assertThrows(InvalidDateInputException.class,
                ()->rentService.askToRent(rentDTO));
    }


    @Test
    void askToRentWithOwner(){
        RentDTO rentDTO = new RentDTO(0,0,"10-10-2019","11-10-2019");
        assertThrows(NoPermissionException.class,
                ()->rentService.askToRent(rentDTO));
    }

    @Test
    void whenGivenACorrectRentThenReturnRentAccepted(){
        Map<String,Long> map= new HashMap<>();
        map.put("rentID",(long) 0);
        Rent rent = rentService.acceptRent(map);
        assertThat(rent.getPending()).isEqualTo(false);
    }

    @Test
    void whenGivenACorrectRentThenReturnRentDenyed(){
        Map<String,Long> map= new HashMap<>();
        map.put("rentID",(long) 0);
        Rent rent = rentService.denyRent(map);
        assertThat(rent.getPending()).isEqualTo(false);
    }

    @Test
    void whenGivenAIncorrectRentThenReturnInvalidIDException(){
        Map<String,Long> map= new HashMap<>();
        map.put("rentID",(long) 50);
        assertThrows(InvalidIdException.class,
                ()->rentService.acceptRent(map));
    }

    @Test
    void whenGivenAIncorrectRentDenyThenReturnInvalidIDException(){
        Map<String,Long> map= new HashMap<>();
        map.put("rentID",(long) 50);
        assertThrows(InvalidIdException.class,
                ()->rentService.denyRent(map));
    }

    @Test
    void whenSearchForPendingAndThereAreReturnPendingList(){
        user = Mockito.spy(user);
        Mockito.when(user.getId()).thenReturn((long) 0);
        List<Rent> rents = rentService.pendingRents(user.getId());
        assertThat(rents.size()).isEqualTo(2);
        assertThat(rents).contains(rentPending);
        assertThat(rents).contains(rentPending2);
    }

    @Test
    void whenSearchForPendingAndThereArentReturnEmptyList(){
        user = Mockito.spy(user);
        Mockito.when(user.getId()).thenReturn((long) 0);
        List<Rent> rents = rentService.onGoingRents(user.getId());
        assertThat(rents.size()).isEqualTo(1);
        assertThat(rents.get(0)).isEqualToComparingFieldByField(rentOnGoing);
    }

}