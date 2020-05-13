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
import static org.mockito.ArgumentMatchers.any;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private HouseRepository houseRepository;

    @InjectMocks
    private RentService rentService;

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
        Mockito.when(userRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(houseRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(userRepository.findById((long) 0)).thenReturn(user);
        Mockito.when(houseRepository.findById((long) 0)).thenReturn(house);
    }

    @Test
    public void addRentToValidHouseUser(){
        RentDTO rentDTO = new RentDTO(0,0,"10-10-2019","11-10-2019");
        Rent newRent = rentService.askToRent(rentDTO);
        assertThat(newRent.getHouse().getId()).isEqualTo(rentDTO.getHouseID());
        assertThat(newRent.getHouse().getId()).isEqualTo(rentDTO.getHouseID());
        assertThat(newRent.getPending()).isEqualTo(true);
    }

    @Test
    public void addRentToInvalidHouseOrUserExceptException(){
        RentDTO rentDTO = new RentDTO(50,50,"10-10-2019","11-10-2019");
        assertThrows(InvalidIdException.class,
                ()->rentService.askToRent(rentDTO));
    }

    @Test
    public void addRentWithInvalidDate(){
        RentDTO rentDTO = new RentDTO(50,50,"2019-10-20","2019-11-10");
        assertThrows(InvalidDateInputException.class,
                ()->rentService.askToRent(rentDTO));
    }

}