package tqs.justlikehome.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock(lenient=true)
    private HouseRepository houseRepository;

    @Mock(lenient=true)
    private UserRepository userRepository;

    @InjectMocks
    private HouseService houseService;

    private House house;
    private User user;
    private Set<Comodities> comodities = new HashSet<>();

    @BeforeEach
    void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        Comodities comoditie = new Comodities("fun","Pool with jacuzzi");
        comodities.add(comoditie);
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house3"
        );
        house.setOwner(user);
        List<House> houses = new ArrayList<>();
        houses.add(house);
        Mockito.when(houseRepository.searchHouse(any(Integer.class),any(String.class),any(Date.class),any(Date.class))).thenReturn(houses);
        Mockito.when(houseRepository.findById(house.getId())).thenReturn(house);
        Mockito.when(houseRepository.getRating(house.getId())).thenReturn(4.5);
        Mockito.when(userRepository.getUserAvgRating(user.getId())).thenReturn(10.0);
    }

    @Test
    void searchForHouseWithReverseDate(){
        assertThrows(InvalidDateInputException.class,
                ()->houseService.getHouse("Aveiro","2019-11-02","2019-11-02",3));
    }

    @Test
    void searchForHouseWithInvalidDate(){
        assertThrows(InvalidDateInputException.class,
                ()->houseService.getHouse("Aveiro","2019-ab-2","as2019-11-022",3));
    }

    @Test
    void searchForHouseWithValidDate(){
        List<HouseSearchDTO> houses = houseService.getHouse("Aveiro","11-02-2013","11-03-2014",3);
        assertThat(houses.size()).isEqualTo(1);
        assertThat(houses.get(0).getCity()).isEqualTo(house.getCity());
        assertThat(houses.get(0).getDescription()).isEqualTo(house.getDescription());
        assertThat(houses.get(0).getKmFromCityCenter()).isEqualTo(house.getKmFromCityCenter());
        assertThat(houses.get(0).getNumberOfBeds()).isEqualTo(house.getNumberOfBeds());
        assertThat(houses.get(0).getPricePerNight()).isEqualTo(house.getPricePerNight());
        assertThat(houses.get(0).getOwnerName()).isEqualTo(house.getOwner().getUsername());
        assertThat(houses.get(0).getRating()).isEqualTo(4.5);
    }

    @Test
    void addComoditieToRightID() {
        // Spy because we need to make sure the ID is correct
        house = Mockito.spy(house);
        Mockito.when(house.getId()).thenReturn((long) 0);
        ComoditiesDTO comoditiesDTO = new ComoditiesDTO("Pool","Pool 20m by 30m",house.getId());
        House testHouse = houseService.addComoditieToHouse(comoditiesDTO);
        assertThat(testHouse.getComodities().size()).isEqualTo(1);
    }

    @Test
    void addComoditieToWrongID(){
        ComoditiesDTO comoditiesDTO = new ComoditiesDTO("Pool","Pool 20m by 30m",10);
        assertThrows(InvalidIdException.class,
                ()->houseService.addComoditieToHouse(comoditiesDTO));
    }

    @Test
    void updateWithRightID(){
        house = Mockito.spy(house);
        Mockito.when(house.getId()).thenReturn((long) 0);
        HouseDTO houseDTO = new HouseDTO("Aveiro", "desc", 3.0, 75, 2, 5,0,"casa das conchas");
        houseDTO.setHouseId(house.getId());
        House testHouse = houseService.updateHouse(houseDTO);
        assertEquals(testHouse.getCity(),"Aveiro");
        assertEquals(testHouse.getDescription(),"desc");
        assertEquals(testHouse.getPricePerNight(),75);
        assertEquals(testHouse.getNumberOfBeds(),2);
    }

    @Test
    void updateWithWrongID(){
        HouseDTO houseDTO = new HouseDTO("Aveiro", "desc", 3.0, 75, 2, 5, 0, "casa das conchas");
        houseDTO.setHouseId((long) 50);
        assertThrows(InvalidIdException.class,
        ()->houseService.updateHouse(houseDTO));
    }

    @Test
    public void whenSearchSpecificExistingHouse_returnHouseSearchDTO(){
        HouseSearchDTO houseSearchDTO = houseService.getSpecificHouse(house.getId());
        assertThat(houseSearchDTO.getCity()).isEqualTo(house.getCity());
        assertThat(houseSearchDTO.getOwnerName()).isEqualTo(user.getUsername());
        assertThat(houseSearchDTO.getRating()).isEqualTo(4.5);
        assertThat(houseSearchDTO.getUserRating()).isEqualTo(10.0);
    }

    @Test
    public void whenSearchSpecificExistingHouse_withNoRatings_returnHouseSearchDTO(){
        Mockito.when(houseRepository.getRating(house.getId())).thenReturn(null);
        Mockito.when(userRepository.getUserAvgRating(user.getId())).thenReturn(null);

        HouseSearchDTO houseSearchDTO = houseService.getSpecificHouse(house.getId());
        assertThat(houseSearchDTO.getCity()).isEqualTo(house.getCity());
        assertThat(houseSearchDTO.getOwnerName()).isEqualTo(user.getUsername());
        assertThat(houseSearchDTO.getRating()).isEqualTo(0);
        assertThat(houseSearchDTO.getUserRating()).isEqualTo(0);
    }
}