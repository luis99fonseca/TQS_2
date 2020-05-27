package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.UserDTO;
import tqs.justlikehome.dtos.UserInfoDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/newHouse")
    @ResponseBody
    public House addHouseToUser(@Valid  @RequestBody HouseDTO house) {
        return userService.addHouseToUser(house);
    }

    @PostMapping(value = "/createUser")
    @ResponseBody
    public User createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping(value="/userHouses/user={userId}")
    public List<House> getUserHouses(@Valid @PathVariable long userId){
        return userService.getUserHouses(userId);
    }

    @GetMapping(value="/userInfo/user={userId}")
    public UserInfoDTO getUserInfo(@PathVariable long userId){
        return userService.getUserInfo(userId);
    }

    @GetMapping(value="/login")
    public Map<String,Long> login(@RequestHeader("id") long userId,@RequestHeader("password") String password){
        return userService.login(userId,password);
    }
}