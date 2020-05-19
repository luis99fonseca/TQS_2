package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HouseService {

    private static DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public HouseRepository houseRepository;

    @Autowired
    public UserRepository userRepository;

    public List<HouseSearchDTO> getHouse(String cityName, String start, String end, int numberOfGuests){
        try{
            Date startDate = Date.from(LocalDate.parse(start, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.parse(end, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
            List<HouseSearchDTO> houses = new ArrayList<>();
            for(House house:houseRepository.searchHouse(numberOfGuests, cityName, startDate, endDate)){
                Double rating = houseRepository.getRating(house.getId());
                houses.add(new HouseSearchDTO(house,house.getOwner(),rating==null?0:rating));
            }
            return houses;
        }catch(DateTimeParseException e){
            throw new InvalidDateInputException();
        }
    }

    public House addComoditieToHouse(ComoditiesDTO comoditiesDTO){
        try{
            Comodities comoditie = new Comodities(comoditiesDTO);
            House house = houseRepository.findById(comoditiesDTO.getHouse());
            house.addComoditieToHouse(comoditie);
            houseRepository.save(house);
            return house;
        }catch(NullPointerException e){
           throw new InvalidIdException();
        }
    }

    public HouseSearchDTO getSpecificHouse(long houseID){
        House house = houseRepository.findById(houseID);
        User owner = house.getOwner();
        Double ratingHouse = houseRepository.getRating(houseID);
        HouseSearchDTO houseSearch = new HouseSearchDTO(house,owner,ratingHouse==null?0:ratingHouse);
        Double ratingOwner = userRepository.getUserAvgRating(owner.getId());
        houseSearch.setUserRating(ratingOwner==null?0:ratingOwner);
        return houseSearch;
    }

}
