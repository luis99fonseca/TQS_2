package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.services.RentService;
import tqs.justlikehome.services.UserService;

@RestController
public class RentController {

    @Autowired
    private RentService rentService;

    @PostMapping(value="/askToRent")
    @ResponseBody
    public Rent askToRent(@RequestBody RentDTO rentDTO) throws InvalidIdException {
        return rentService.askToRent(rentDTO);
    }

}
