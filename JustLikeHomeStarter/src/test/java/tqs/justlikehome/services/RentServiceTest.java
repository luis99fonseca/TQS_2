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
import tqs.justlikehome.exceptions.InvalidRentRequestException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
    private User owner;
    private House house;
    private Rent rentPending;
    private Rent rentOnGoing;
    private Rent rentPending2;

    @BeforeEach
    public void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        owner = new User("Owner","Luis","Fonseca2",new GregorianCalendar(1999, Calendar.JULY,20));
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house2"
        );
        house.setOwner(owner);
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        rentPending = new Rent(house,owner,start,end);
        rentPending2 = new Rent(house,owner,start,end);
        rentOnGoing = new Rent(house,owner,start,end);
        rentOnGoing.setPending(false);
        List<Rent> rentList = new ArrayList<>();
        rentList.add(rentPending);
        rentList.add(rentPending2);
        List<Rent> rentListNotPending = new ArrayList<>();
        rentListNotPending.add(rentOnGoing);
        Mockito.when(userRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(houseRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(userRepository.findById((long) 0)).thenReturn(owner);
        Mockito.when(userRepository.findById((long) 1)).thenReturn(user);
        Mockito.when(houseRepository.findById((long) 0)).thenReturn(house);
        Mockito.when(rentRepository.findById((long) 0)).thenReturn(rentPending);
        Mockito.when(rentRepository.findById((long) 50)).thenReturn(null);
        Mockito.when(rentRepository.findByIdAndPending(0,true)).thenReturn(rentList);
        Mockito.when(rentRepository.findByIdAndPending(0,false)).thenReturn(rentListNotPending);
    }

    @Test
    public void addRentToValidHouseUser(){
        RentDTO rentDTO = new RentDTO(0,1,"10-10-2019","11-10-2019");
        Rent newRent = rentService.askToRent(rentDTO);
        assertThat(newRent.getHouse().getId()).isEqualTo(rentDTO.getHouseID());
        assertThat(newRent.getHouse().getId()).isEqualTo(rentDTO.getHouseID());
        assertThat(newRent.getPending()).isEqualTo(true);
    }

    @Test
    public void OwnerTriesToRentOwnHouseThenException(){
        RentDTO rentDTO = new RentDTO(0,0,"10-10-2019","11-10-2019");
        assertThrows(InvalidRentRequestException.class,
                ()->rentService.askToRent(rentDTO));
    }

    @Test
    public void addRentToInvalidHouseOrUserExceptException(){
        RentDTO rentDTO = new RentDTO(50,50,"10-10-2019","11-10-2019");
        assertThrows(InvalidIdException.class,
                ()->rentService.askToRent(rentDTO));
    }

    @Test
    public void addRentWithInvalidDate(){
        RentDTO rentDTO = new RentDTO(0,1,"2019-10-20","2019-11-10");
        assertThrows(InvalidDateInputException.class,
                ()->rentService.askToRent(rentDTO));
    }

    @Test
    public void whenGivenACorrectRentThenReturnRentAccepted(){
        Map<String,Long> map= new HashMap<>();
        map.put("rentID",(long) 0);
        Rent rent = rentService.acceptRent(map);
        assertThat(rent.getPending()).isEqualTo(false);
    }

    @Test
    public void whenGivenAIncorrectRentThenReturnInvalidIDException(){
        Map<String,Long> map= new HashMap<>();
        map.put("rentID",(long) 50);
        assertThrows(InvalidIdException.class,
                ()->rentService.acceptRent(map));
    }

    @Test
    public void whenSearchForPendingAndThereAreReturnPendingList(){
        owner = Mockito.spy(owner);
        Mockito.when(owner.getId()).thenReturn((long) 0);
        List<Rent> rents = rentService.pendingRents(owner.getId());
        assertThat(rents.size()).isEqualTo(2);
        assertThat(rents).contains(rentPending);
        assertThat(rents).contains(rentPending2);
    }

    @Test
    public void whenSearchForPendingAndThereArentReturnEmptyList(){
        owner = Mockito.spy(owner);
        Mockito.when(owner.getId()).thenReturn((long) 0);
        List<Rent> rents = rentService.onGoingRents(owner.getId());
        assertThat(rents.size()).isEqualTo(1);
        assertThat(rents.get(0)).isEqualToComparingFieldByField(rentOnGoing);
    }

}