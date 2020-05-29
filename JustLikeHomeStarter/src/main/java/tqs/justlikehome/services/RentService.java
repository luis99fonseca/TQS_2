package tqs.justlikehome.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.exceptions.NoPermissionException;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.repositories.RentRepository;
import tqs.justlikehome.repositories.UserRepository;

@Service
@Transactional
public class RentService {

    @Autowired
    public HouseRepository houseRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RentRepository rentRepository;

    public Rent askToRent(RentDTO rentDTO){
        try{
            User user = userRepository.findById(rentDTO.getUserID());
            House house = houseRepository.findById(rentDTO.getHouseID());
            System.out.println(user.getId());
            System.out.println(house.getOwner().getId());
            if(user.getId()==house.getOwner().getId()){
                throw new NoPermissionException();
            }
            Rent newRent = new Rent(house,user,rentDTO);
            user.addPurchasedRent(newRent);
            userRepository.save(user);
            return newRent;
        }catch(NullPointerException e){
            throw new InvalidIdException();
        }catch(DateTimeParseException e){
            throw new InvalidDateInputException();
        }
    }

    public Rent acceptRent(Map<String,Long> rentID){
        try {
            Rent rent = rentRepository.findById(rentID.get("rentID"));
            rent.setPending(false);
            rentRepository.save(rent);
            return rent;
        }catch (NullPointerException e){
            throw new InvalidIdException();
        }
    }

    public Rent denyRent(Map<String, Long> rentID){
        try{
            Rent rent = rentRepository.findById(rentID.get("rentID"));
            rent.setPending(false);
            rentRepository.delete(rent);
            return rent;
        }catch (NullPointerException e){
            throw new InvalidIdException();
        }
    }

    public List<Rent> pendingRents(long userID){
        return rentRepository.findByIdAndPending(userID,true);
    }

    public List<Rent> onGoingRents(long userID){
        return rentRepository.findByIdAndPending(userID,false);
    }

}
