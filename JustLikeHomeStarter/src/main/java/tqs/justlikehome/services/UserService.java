package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidOwnerIdException;
import tqs.justlikehome.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    public UserRepository userRepository;

    // DTO for safety reasons , it is automatically created in the request then we need to map to the real object
    public House addHouseToUser(HouseDTO houseDTO){
        User owner = userRepository.findById(houseDTO.getUserId());
        if(owner==null){
            throw new InvalidOwnerIdException();
        }
        House house = new House(houseDTO);
        owner.addHouse(house);
        house.setOwner(owner);
        userRepository.save(owner);
        return house;
    }

    // DTO for safety reasons , it is automatically created in the request then we need to map to the real object
    public User createUser(UserDTO userDTO) {
        try {
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
