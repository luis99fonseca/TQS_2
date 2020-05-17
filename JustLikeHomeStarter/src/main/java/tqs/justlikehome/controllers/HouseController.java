package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.services.HouseService;

import java.util.List;

@RestController
@CrossOrigin
public class HouseController {
    @Autowired
    private HouseService houseService;

    public HouseController(HouseService houseService){
        this.houseService=houseService;
    }

    @GetMapping(value = "/houses/city={city}&start={start}&end={end}&guests={guests}")
    @ResponseBody
    public List<HouseSearchDTO> getHouse(@PathVariable String city,
                                         @PathVariable String start,
                                         @PathVariable String end,
                                         @PathVariable int guests){
        return houseService.getHouse(city,start,end,guests);
    }

    @PostMapping(value = "/addComoditie")
    @ResponseBody
    public House addComoditieToHouse(@RequestBody ComoditiesDTO comoditiesDTO){
        return houseService.addComoditieToHouse(comoditiesDTO);
    }
}