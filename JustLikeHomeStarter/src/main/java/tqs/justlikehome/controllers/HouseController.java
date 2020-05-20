package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.BookMarkDTO;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.services.HouseService;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
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

    @GetMapping(value="/specificHouse/houseId={houseId}")
    public HouseSearchDTO getSpecificHouse(@PathVariable long houseId){
        return houseService.getSpecificHouse(houseId);
    }

    // maybe change pa PUT as it is idempotent
    @PostMapping(value = "/addBookmark")
    @ResponseBody
    public BookMarkDTO addBookmark(@RequestBody BookMarkDTO bookmark) {
        return houseService.addBookmark(bookmark);
    }
}
