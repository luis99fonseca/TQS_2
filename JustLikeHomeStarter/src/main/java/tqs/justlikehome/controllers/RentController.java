package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.services.RentService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class RentController {

    @Autowired
    private RentService rentService;

    @PostMapping(value="/askToRent")
    public Rent askToRent(@RequestBody RentDTO rentDTO){
        return rentService.askToRent(rentDTO);
    }

    @PutMapping(value="/acceptRent")
    public Rent acceptRent(@RequestBody Map<String,Long> rentID){
        return rentService.acceptRent(rentID);
    }

    @PutMapping(value="/denyRent")
    public Rent denyRent(@RequestBody Map<String, Long> rentID){
        return rentService.denyRent(rentID);
    }

    @GetMapping(value="/pendingRents/user={userID}")
    public List<Rent> pendingRents(@PathVariable long userID){
        return rentService.pendingRents(userID);
    }

    @GetMapping(value="/onGoingRents/user={userID}")
    public List<Rent> onGoingRents(@PathVariable long userID){
        return rentService.onGoingRents(userID);
    }

}
