package tqs.justlikehome.controllers;

import static org.hamcrest.Matchers.hasSize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tqs.justlikehome.dtos.RentDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.Rent;
import tqs.justlikehome.entities.User;
import tqs.justlikehome.exceptions.InvalidDateInputException;
import tqs.justlikehome.exceptions.InvalidIdException;
import tqs.justlikehome.services.RentService;
import tqs.justlikehome.utils.ObjectJsonHelper;


import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentController.class)
class RentControllerTest {

    @MockBean
    private RentService rentService;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private House house;
    private Rent rent;

    @BeforeEach
    private void setup(){
        user = new User("Fonsequini","Luis","Fonseca",new GregorianCalendar(1999, Calendar.JULY,20));
        house = new House(
                "Aveiro",
                "Incredible House near Ria de Aveiro",
                3.0,
                50.0,
                2,
                5,
                "house3"
        );
        Date start = Date.from(new GregorianCalendar(2019, Calendar.JULY,20).toZonedDateTime().toInstant());
        Date end = Date.from(new GregorianCalendar(2019, Calendar.JULY,22).toZonedDateTime().toInstant());
        rent = new Rent(house,user,start,end);
    }

    @Test
    public void whenPendingRentsWithValidID() throws Exception{
        List<Rent> rentList = new ArrayList<>();
        rentList.add(rent);
        given(rentService.pendingRents((long) 0)).willReturn(rentList);
        mockMvc.perform(get("/pendingRents/user="+0).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].user.username",is("Fonsequini")))
                .andExpect(jsonPath("$[0].user.firstName",is("Luis")))
                .andExpect(jsonPath("$[0].pending",is(true)));
    }

    @Test
    public void whenPendingRentsWithInvalidID() throws Exception{
        given(rentService.pendingRents((long) 50)).willReturn(Collections.emptyList());
        mockMvc.perform(get("/pendingRents/user="+50).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenNotPendingRentsWithValidID() throws Exception{
        rent.setPending(false);
        List<Rent> rentList = new ArrayList<>();
        rentList.add(rent);
        given(rentService.onGoingRents((long) 0)).willReturn(rentList);
        mockMvc.perform(get("/onGoingRents/user="+0).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].user.username",is("Fonsequini")))
                .andExpect(jsonPath("$[0].user.firstName",is("Luis")))
                .andExpect(jsonPath("$[0].pending",is(false)));
    }

    @Test
    public void whenNotPendingRentsWithInvalidID() throws Exception{
        given(rentService.pendingRents((long) 50)).willReturn(Collections.emptyList());
        mockMvc.perform(get("/onGoingRents/user="+50).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void acceptRentWithValidID() throws Exception{
        Map<String,Long> rentId = new HashMap<>();
        rentId.put("rentID",(long) 0);
        rent.setPending(false);
        given(rentService.acceptRent(rentId)).willReturn(rent);
        mockMvc.perform(put("/acceptRent").content(objectToJson(rentId)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.user.firstName",is("Luis")))
                .andExpect(jsonPath("$.pending",is(false)));
    }

    @Test
    public void acceptRentWithInvalidID() throws Exception{
        Map<String,Long> rentId = new HashMap<>();
        rentId.put("rentID",(long) 200);
        given(rentService.acceptRent(rentId)).willThrow(InvalidIdException.class);
        mockMvc.perform(put("/acceptRent").content(ObjectJsonHelper.objectToJson(rentId)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    }

    @Test
    public void addRentWithValidParams() throws Exception{
        RentDTO rentDTO = new RentDTO(0,0,"10-10-2019","10-10-2019");
        given(rentService.askToRent(any(RentDTO.class))).willReturn(rent);
        mockMvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(ObjectJsonHelper.objectToJson(rentDTO)))
                .andExpect(jsonPath("$.id",is(0)))
                .andExpect(jsonPath("$.user.username",is("Fonsequini")))
                .andExpect(jsonPath("$.pending",is(true)))
                .andExpect(status().isOk());
    }

    @Test
    public void addRentWithInvalidRentDTO() throws Exception{
        RentDTO rentDTO = new RentDTO(0,0,"2019-10-10","10-10-2019");
        given(rentService.askToRent(any(RentDTO.class))).willThrow(InvalidDateInputException.class);
        mockMvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(ObjectJsonHelper.objectToJson(rentDTO)))

                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addRentWithInvalidIDDTO() throws Exception{
        RentDTO rentDTO = new RentDTO(500,500,"10-10-2019","10-10-2019");
        given(rentService.askToRent(any(RentDTO.class))).willThrow(InvalidIdException.class);
        mockMvc.perform(post("/askToRent").contentType(MediaType.APPLICATION_JSON).content(objectToJson(rentDTO)))
                .andExpect(status().is4xxClientError());
    }

    private String objectToJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }
    }


}