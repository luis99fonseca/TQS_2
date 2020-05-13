package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.format.DateTimeParseException;

@Service
@Transactional
public class RentService {

    @Autowired
    public HouseRepository houseRepository;

    @Autowired
    public UserRepository userRepository;

    public Rent askToRent(RentDTO rentDTO){
        try{
            User user = userRepository.findById(rentDTO.getUserID());
            House house = houseRepository.findById(rentDTO.getHouseID());
            Rent newRent = new Rent(house,user,rentDTO);
            user.addPurchasedRent(newRent);
            house.addRent(newRent);
            userRepository.save(user);
            houseRepository.save(house);
            return newRent;
        }catch(NullPointerException e){
            throw new InvalidIdException();
        }catch(DateTimeParseException e){
            throw new InvalidDateInputException();
        }
    }

}
