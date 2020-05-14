package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidIdException;
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
    public House addHouseToUser(@RequestBody HouseDTO house) throws InvalidIdException {
        return userService.addHouseToUser(house);
    }

    @PostMapping(value="/createUser")
    @ResponseBody
    public User createUser(@RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @GetMapping(value="/userHouses/user={userId}")
    public List<House> getUserHouses(@PathVariable Long userId){
        return userService.getUserHouses(userId);
    }

}