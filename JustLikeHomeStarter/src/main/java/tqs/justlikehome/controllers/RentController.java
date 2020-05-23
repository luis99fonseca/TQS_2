package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.services.RentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class RentController {

    @Autowired
    private RentService rentService;

    @PostMapping(value="/askToRent")
    @ResponseBody
    public Rent askToRent(@Valid  @RequestBody RentDTO rentDTO){
        return rentService.askToRent(rentDTO);
    }

    @PutMapping(value="/acceptRent")
    @ResponseBody
    public Rent acceptRent(@Valid @RequestBody Map<String,Long> rentID){
        return rentService.acceptRent(rentID);
    }

    @PutMapping(value="/denyRent")
    @ResponseBody
    public Rent denyRent(@Valid @RequestBody Map<String, Long> rentID){
        return rentService.denyRent(rentID);
    }

    @GetMapping(value="/pendingRents/user={userID}")
    @ResponseBody
    public List<Rent> pendingRents(@Valid @PathVariable long userID){
        return rentService.pendingRents(userID);
    }

    @GetMapping(value="/onGoingRents/user={userID}")
    @ResponseBody
    public List<Rent> onGoingRents(@Valid @PathVariable long userID){
        return rentService.onGoingRents(userID);
    }

}
