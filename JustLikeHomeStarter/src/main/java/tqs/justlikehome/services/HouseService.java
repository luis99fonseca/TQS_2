package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tqs.justlikehome.dtos.BookMarkDTO;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.Comodities;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.ComoditiesRepository;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HouseService {

    private static DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public HouseRepository houseRepository;

    @Autowired
    public ComoditiesRepository comoditiesRepository;

    @Autowired
    public UserRepository userRepository;

    public List<HouseSearchDTO> getHouse(String cityName, String start, String end, int numberOfGuests){
        try{
            Date startDate = Date.from(LocalDate.parse(start, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.parse(end, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
            List<HouseSearchDTO> houses = new ArrayList<>();
            List<House> housesWithPastRents = houseRepository.searchHouse(numberOfGuests, cityName, startDate, endDate);
            for(House house:housesWithPastRents){
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
            comoditie.setHouse(house);
            house.addComoditieToHouse(comoditie);
            house = houseRepository.save(house);
            return house;
        }catch(NullPointerException e){
           throw new InvalidIdException();
        }
    }

    public HouseSearchDTO getSpecificHouse(long houseID){
        House house = houseRepository.findById(houseID);
        if (house == null){
            throw new InvalidIdException();
        }
        User owner = house.getOwner();
        Double ratingHouse = houseRepository.getRating(houseID);
        HouseSearchDTO houseSearch = new HouseSearchDTO(house,owner,ratingHouse==null?0:ratingHouse);
        Double ratingOwner = userRepository.getUserAvgRating(owner.getId());
        houseSearch.setUserRating(ratingOwner==null?0:ratingOwner);
        return houseSearch;
    }

    public BookMarkDTO addBookmark(BookMarkDTO bookmark) {
        House house = houseRepository.findById(bookmark.getHouseId());
        User user = userRepository.findById(bookmark.getUserId());
        if (house == null || user == null){
            throw new InvalidIdException();
        }
        user.addBookmarkedHouse(house);
        house.addBookmarkedBy(user);
        return bookmark;
    }

    public BookMarkDTO deleteBookmark(long userId, long houseId) {
        House house = houseRepository.findById(houseId);
        User user = userRepository.findById(userId);
        if (house == null || user == null){
            throw new InvalidIdException();
        }
        user.getBookmarkedHouses().remove(house);
        house.getBookmarkedBy().remove(user);
        return new BookMarkDTO(userId, houseId);
    }
    public House updateHouse(HouseDTO houseDTO){
        try{
            House house = houseRepository.findById(houseDTO.getHouseId());
            house.updateHouse(houseDTO);
            houseRepository.save(house);
            return house;
        }catch(NullPointerException e){
            throw new InvalidIdException();
        }
    }

    public List<HouseSearchDTO> getTopHouses(){
        List<Object[]> objs = houseRepository.getTopHouses(PageRequest.of(0,5));
        List<HouseSearchDTO> houses = new ArrayList<>();
        for(Object[] obj:objs){
            HouseSearchDTO houseSearch = new HouseSearchDTO((House) obj[0],((House) obj[0]).getOwner(),obj[1]==null?0:Double.parseDouble(obj[1].toString()));
            Double ratingOwner = userRepository.getUserAvgRating(((House) obj[0]).getOwner().getId());
            houseSearch.setUserRating(ratingOwner==null?0:ratingOwner);
            houses.add(houseSearch);
        }
        return houses;
    }

}
