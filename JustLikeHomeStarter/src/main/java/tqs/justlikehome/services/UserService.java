package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidOwnerIdException;
import tqs.justlikehome.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public House addHouseToUser(House house,int ownerId){
        User owner = userRepository.findById((long) ownerId);
        if(owner==null){
            throw new InvalidOwnerIdException();
        }
        owner.addHouse(house);
        house.setOwner(owner);
        userRepository.save(owner);
        return house;
    }

    public User createUser(User user){
        userRepository.save(user);
        return user;
    }

    public List<House> getUserHouses(long userId){
        return userRepository.getUserHouses(userId);
    }
}
