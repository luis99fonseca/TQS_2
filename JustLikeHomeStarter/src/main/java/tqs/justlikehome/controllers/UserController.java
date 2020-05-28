package tqs.justlikehome.controllers;

import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="Create and add a house to a existing user")
    public House addHouseToUser(@Valid  @RequestBody HouseDTO house) {
        return userService.addHouseToUser(house);
    }

    @PostMapping(value = "/createUser")
    @ResponseBody
    @ApiOperation(value="Create a new user")
    public User createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping(value="/userHouses/user={userId}")
    @ApiOperation(value="Return all user houses")
    public List<House> getUserHouses(@Valid @PathVariable long userId){
        return userService.getUserHouses(userId);
    }

    @GetMapping(value="/userInfo/user={userId}")
    @ApiOperation(value="Returns all user information")
    public UserInfoDTO getUserInfo(@Valid @PathVariable long userId){
        return userService.getUserInfo(userId);
    }

    @GetMapping(value = "/getAll")
    @ApiOperation(value="Returns all users")
    public List<User> getAll() { return userService.getAll();}

    @GetMapping(value="/login")
    @ApiOperation(value="User login")
    public Map<String,Long> login(@Valid @RequestHeader("username") String username,@Valid @RequestHeader("password") String password){
        return userService.login(username,password);
    }
}