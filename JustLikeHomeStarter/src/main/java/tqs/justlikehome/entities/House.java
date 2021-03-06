package tqs.justlikehome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tqs.justlikehome.dtos.HouseDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="house")
public class House {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String city;
    private String description;
    private String houseName;
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "house",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Rent> timesRented = new HashSet<>();

    @OneToMany(mappedBy = "house",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<HouseReviews> houseReviews = new HashSet<>();

    public void setComodities(Set<Comodities> comodities) {
        this.comodities = comodities;
    }

    @ManyToMany(mappedBy = "bookmarkedHouses")
    @JsonIgnore
    private Set<User> bookmarkedBy = new HashSet<>();

    @OneToMany(mappedBy = "house",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Comodities> comodities = new HashSet<>();

    public Set<User> getBookmarkedBy() {
        return bookmarkedBy;
    }

    public House(){

    }

    public House(HouseDTO house) {
        this.city=house.getCity();
        this.houseName=house.getHouseName();
        this.description=house.getDescription();
        this.kmFromCityCenter=house.getKmFromCityCenter();
        this.pricePerNight=house.getPricePerNight();
        this.houseName=house.getHouseName();
        this.numberOfBeds=house.getNumberOfBeds();
        this.maxNumberOfUsers=house.getMaxNumberOfUsers();
        for(Comodities c:house.getComodities()){
            c.setHouse(this);
            this.addComoditieToHouse(c);
        }
    }


    public House(String city, String description, double kmFromCityCenter, double pricePerNight, int numberOfBeds, int maxNumberOfUsers,String houseName) {
        this.city = city;
        this.description = description;
        this.kmFromCityCenter = kmFromCityCenter;
        this.pricePerNight = pricePerNight;
        this.numberOfBeds = numberOfBeds;
        this.maxNumberOfUsers = maxNumberOfUsers;
        this.houseName=houseName;
    }

    public House(String city, String description, double kmFromCityCenter, double pricePerNight, int numberOfBeds, int maxNumberOfUsers,String houseName, Set<Comodities> comodities) {
        this.city = city;
        this.description = description;
        this.kmFromCityCenter = kmFromCityCenter;
        this.pricePerNight = pricePerNight;
        this.numberOfBeds = numberOfBeds;
        this.maxNumberOfUsers = maxNumberOfUsers;
        this.houseName=houseName;
        this.comodities = comodities;
    }

    public void addComoditieToHouse(Comodities comodities) {
        this.comodities.add(comodities);
    }

    public Set<Rent> getTimesRented() {
        return timesRented;
    }

    public void addReview(HouseReviews houseReview){
        this.houseReviews.add(houseReview);
    }

    public void addBookmarkedBy(User user) {
        this.bookmarkedBy.add(user);
    }

    public Set<HouseReviews> getHouseReviews() {
        return houseReviews;
    }

    public String getHouseName() {
        return houseName;
    }

    public void addRent(Rent rent){
        this.timesRented.add(rent);
    }

    public Set<Comodities> getComodities() {
        return this.comodities;
    }

    public long getId() {
        return this.id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public double getKmFromCityCenter() {
        return kmFromCityCenter;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getMaxNumberOfUsers() {
        return maxNumberOfUsers;
    }

    public User getOwner() {
        return owner;
    }

    public void updateHouse(HouseDTO house){
        this.city=house.getCity();
        this.houseName=house.getHouseName();
        this.description=house.getDescription();
        this.kmFromCityCenter=house.getKmFromCityCenter();
        this.pricePerNight=house.getPricePerNight();
        this.houseName=house.getHouseName();
        this.numberOfBeds=house.getNumberOfBeds();
        this.maxNumberOfUsers=house.getMaxNumberOfUsers();
    }
}
