package tqs.justlikehome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.services.RentService;

import java.util.List;
import java.util.Map;

@RestController
public class RentController {

    @Autowired
    private RentService rentService;

    @PostMapping(value="/askToRent")
    @ResponseBody
    public Rent askToRent(@RequestBody RentDTO rentDTO){
        return rentService.askToRent(rentDTO);
    }

    @PutMapping(value="/acceptRent")
    @ResponseBody
    public Rent acceptRent(@RequestBody Map<String,Long> rentID){
        System.out.println(rentService.acceptRent(rentID).toString());
        return rentService.acceptRent(rentID);
    }

    @GetMapping(value="/pendingRents/user={userID}")
    @ResponseBody
    public List<Rent> pendingRents(@PathVariable long userID){
        return rentService.pendingRents(userID);
    }

    @GetMapping(value="/onGoingRents/user={userID}")
    @ResponseBody
    public List<Rent> onGoingRents(@PathVariable long userID){
        return rentService.onGoingRents(userID);
    }

}
