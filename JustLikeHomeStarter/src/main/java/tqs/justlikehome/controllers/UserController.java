package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.services.UserService;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/newHouse")
    @ResponseBody
    public House addHouseToUser(@RequestBody HouseDTO house) {
        return userService.addHouseToUser(house);
    }

    @PostMapping(value = "/createUser")
    @ResponseBody
    public User createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping(value="/userHouses/user={userId}")
    public List<House> getUserHouses(@PathVariable long userId){
        System.out.println(">> " + userService.getUserHouses(userId));
        return userService.getUserHouses(userId);
    }

    // TODO: remove, here for debugging porpuses
    @GetMapping(value = "/getAll")
    public List<User> getAll() { return userService.getAll();}
}