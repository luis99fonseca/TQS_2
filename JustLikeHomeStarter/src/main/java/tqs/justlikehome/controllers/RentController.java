package tqs.justlikehome.controllers;

import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="Ask to rent a certain house")
    public Rent askToRent(@Valid  @RequestBody RentDTO rentDTO){
        return rentService.askToRent(rentDTO);
    }

    @PutMapping(value="/acceptRent")
    @ResponseBody
    @ApiOperation(value="Owner accept a user rent request on a given house")
    public Rent acceptRent(@Valid @RequestBody Map<String,Long> rentID){
        return rentService.acceptRent(rentID);
    }

    @PutMapping(value="/denyRent")
    @ResponseBody
    @ApiOperation(value="Owner deny a user rent request on a given house")
    public Rent denyRent(@Valid @RequestBody Map<String, Long> rentID){
        return rentService.denyRent(rentID);
    }

    @GetMapping(value="/pendingRents/user={userID}")
    @ResponseBody
    @ApiOperation(value="Return all pending rents on all houses of a given user")
    public List<Rent> pendingRents(@Valid @PathVariable long userID){
        return rentService.pendingRents(userID);
    }

    @GetMapping(value="/onGoingRents/user={userID}")
    @ResponseBody
    @ApiOperation(value="Return all on going rents on all houses of a given user")
    public List<Rent> onGoingRents(@Valid @PathVariable long userID){
        return rentService.onGoingRents(userID);
    }

}
