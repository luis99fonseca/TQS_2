package tqs.justlikehome.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.repositories.HouseRepository;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock(lenient=true)
    private HouseRepository houseRepository;

    @InjectMocks
    private HouseService houseService;

    private House house;

    @BeforeEach
    public void setup(){
        User user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        Comodities comoditie = new Comodities("fun","Pool with jacuzzi");
        Set<Comodities> comodities = new HashSet<>();
        comodities.add(comoditie);
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                user
        );
        List<House> houses = new ArrayList<>();
        houses.add(house);
        Mockito.when(houseRepository.searchHouse(any(Integer.class),any(String.class),any(Date.class),any(Date.class))).thenReturn(houses);
    }

    @Test
    public void searchForHouseWithReverseDate(){
        assertThrows(InvalidDateInputException.class,
                ()->houseService.getHouse("Aveiro","2019-11-02","2019-11-02",3));
    }

    @Test
    public void searchForHouseWithInvalidDate(){
        assertThrows(InvalidDateInputException.class,
                ()->houseService.getHouse("Aveiro","2019-ab-2","as2019-11-022",3));
    }

    @Test
    public void searchForHouseWithValidDate(){
        List<House> houses = houseService.getHouse("Aveiro","11-02-2013","11-03-2014",3);
        assertThat(houses.size()).isEqualTo(1);
        assertThat(houses.get(0)).isEqualToComparingFieldByField(house);
    }
}