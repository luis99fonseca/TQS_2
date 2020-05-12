package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.services.HouseService;

import java.util.List;

@RestController
public class HouseControler {
    @Autowired
    private HouseService houseService;

    public HouseControler(HouseService houseService){
        this.houseService=houseService;
    }

    @GetMapping(value = "/houses/city=<city>&start=<start>&end=<end>&guests=<guests>")
    @ResponseBody
    public List<House> getCity(@PathVariable String name,
                                               @PathVariable String start,
                                               @PathVariable String end,
                                               @PathVariable int guests) throws InvalidDateInputException {

        return houseService.getHouse(name,start,end,guests);
    }
}
