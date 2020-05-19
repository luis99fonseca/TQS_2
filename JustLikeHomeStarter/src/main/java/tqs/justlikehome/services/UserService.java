package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    public UserRepository userRepository;

    // DTO for safety reasons , it is automatically created in the request then we need to map to the real object
    public House addHouseToUser(HouseDTO houseDTO){
        try {
            User owner = userRepository.findById(houseDTO.getUserId());
            House house = new House(houseDTO);
            owner.addHouse(house);
            house.setOwner(owner);
            User user = userRepository.save(owner);
            // stupid but spring doesn't update the house id :'(
            // I Call this the fuck you programmer I will not update the value of the house id while cascading
            // because i'm a framework of a framework and i do whatever I want and you have to find a way don't you
            // because you need this don't you, go ahead and ruin the performance of your database
            List list = Arrays.asList(user.getOwnedHouses().toArray());
            list.sort((o1, o2)-> (int) (((House) o2).getId()-((House) o1).getId()));
            return (House) list.get(0);
        }catch (NullPointerException e){
            throw new InvalidIdException();
        }
    }

    // DTO for safety reasons , it is automatically created in the request then we need to map to the real object
    public User createUser(UserDTO userDTO) {
        try {
            if (userRepository.findByUsername(userDTO.getUsername()) != null){
                throw new InvalidIdException();
            }
            User user = new User(userDTO);
            userRepository.save(user);
            return user;
        }catch(DateTimeParseException e) {
            throw new InvalidDateInputException();
        }
    }

    public List<House> getUserHouses(long userId){
        return userRepository.getUserHouses(userId);
    }
}