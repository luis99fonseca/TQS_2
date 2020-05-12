package tqs.justlikehome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.repositories.HouseRepository;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HouseService {

    private static DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public HouseRepository houseRepository;

    public List<House> getHouse(String cityName, String start, String end, int numberOfGuests) throws InvalidDateInputException {
        try {
            Date startDate = Date.from(LocalDate.parse(start, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.parse(end, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
            return houseRepository.searchHouse(numberOfGuests, cityName, startDate, endDate);
        }catch(DateTimeParseException e){
            throw new InvalidDateInputException();
        }
    }
}
