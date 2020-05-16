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
@CrossOrigin
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

    @GetMapping(value="/pendingRents/owner={ownerID}")
    @ResponseBody
    public List<Rent> pendingRents(@PathVariable long ownerID){
        return rentService.pendingRents(ownerID);
    }

    @GetMapping(value="/onGoingRents/owner={ownerID}")
    @ResponseBody
    public List<Rent> onGoingRents(@PathVariable long ownerID){
        System.out.println(rentService.onGoingRents(ownerID));
        return rentService.onGoingRents(ownerID);
    }

}
