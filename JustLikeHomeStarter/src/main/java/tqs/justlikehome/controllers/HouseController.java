package tqs.justlikehome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tqs.justlikehome.dtos.BookMarkDTO;
import tqs.justlikehome.dtos.ComoditiesDTO;
import tqs.justlikehome.dtos.HouseDTO;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.services.HouseService;

import javax.validation.Valid;
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
    public List<HouseSearchDTO> getHouse(@Valid @PathVariable String city,
                                         @Valid @PathVariable String start,
                                         @Valid @PathVariable String end,
                                         @Valid @PathVariable int guests){
        return houseService.getHouse(city,start,end,guests);
    }

    @PostMapping(value = "/addComoditie")
    @ResponseBody
    public House addComoditieToHouse(@Valid @RequestBody ComoditiesDTO comoditiesDTO){
        return houseService.addComoditieToHouse(comoditiesDTO);
    }

    @PutMapping(value = "/updateHouse")
    @ResponseBody
    public House updateHouse(@Valid @RequestBody HouseDTO houseDTO){   //need to do HouseDTO.setHouseID()
        return houseService.updateHouse(houseDTO);
    }

    @GetMapping(value="/specificHouse/houseId={houseId}")
    public HouseSearchDTO getSpecificHouse(@Valid @PathVariable long houseId){
        return houseService.getSpecificHouse(houseId);
    }

    // maybe change pa PUT as it is idempotent
    @PostMapping(value = "/addBookmark")
    @ResponseBody
    public BookMarkDTO addBookmark(@Valid @RequestBody BookMarkDTO bookmark) {
        return houseService.addBookmark(bookmark);
    }

    @DeleteMapping(value = "/deleteBookmark/userId={userId}&houseId={houseId}")
    public BookMarkDTO deleteBookmark(@Valid @PathVariable long userId, @PathVariable long houseId) {
        return houseService.deleteBookmark(userId, houseId);
    }

}
