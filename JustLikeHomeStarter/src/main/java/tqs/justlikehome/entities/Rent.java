package tqs.justlikehome.entities;

import tqs.justlikehome.dtos.RentDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private Date rentStart;

    @Temporal(TemporalType.DATE)
    private Date rentEnd;

    private boolean pending;

    public Rent(){

    }

    public Rent(House house,User user,RentDTO rentDTO){
        this.house = house;
        this.user = user;
        this.rentStart = this.dateFromString(rentDTO.getStartDate());
        this.rentEnd = this.dateFromString(rentDTO.getEndDate());
        this.pending = true;
    }

    private Date dateFromString(String date){
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return Date.from(LocalDate.parse(date, parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Rent(House house, User user, Date rentStart, Date rentEnd) {
        this.house = house;
        this.user = user;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        this.pending = true;
    }

    public House getHouse() {
        return house;
    }

    public User getUser() {
        return user;
    }

    public boolean getPending(){
        return this.pending;
    }

    public Date getRentStart() {
        return rentStart;
    }

    public Date getRentEnd() {
        return rentEnd;
    }
}
