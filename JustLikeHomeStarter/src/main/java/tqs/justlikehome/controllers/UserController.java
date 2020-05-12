package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidOwnerIdException;
import tqs.justlikehome.services.HouseService;
import tqs.justlikehome.services.UserService;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping(value="/newHouse")
    @ResponseBody
    public House addHouseToUser(@RequestBody House house,
                                @RequestHeader(value="Owner") int ownerId) throws InvalidOwnerIdException {
        return userService.addHouseToUser(house,ownerId);
    }

    @PostMapping(value="/createUser")
    @ResponseBody
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping(value="/userHouses?user={userId}")
    public List<House> getUserHouses(String userId){
        return userService.getUserHouses(Long.parseLong(userId));
    }

}
