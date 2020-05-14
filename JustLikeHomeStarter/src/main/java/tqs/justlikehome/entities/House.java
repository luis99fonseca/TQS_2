package tqs.justlikehome.entities;

import tqs.justlikehome.dtos.ComoditiesDTO;
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
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User owner;

    @OneToMany(mappedBy = "house",cascade = CascadeType.PERSIST)
    private Set<Rent> timesRented = new HashSet<>();

    @OneToMany(mappedBy = "house",cascade = CascadeType.PERSIST)
    private Set<HouseReviews> houseReviews = new HashSet<>();

    @ManyToMany(mappedBy = "bookmarkedHouses")
    private Set<User> bookmarkedBy = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name="house_comodities",
            joinColumns = {@JoinColumn(name="house_id")},
            inverseJoinColumns = {@JoinColumn(name="comodities_id")}
    )
    private Set<Comodities> comodities = new HashSet<>();



    public House(){

    }

    public House(HouseDTO house){
        this.city=house.getCity();
        this.description=house.getDescription();
        this.kmFromCityCenter=house.getKmFromCityCenter();
        this.pricePerNight=house.getPricePerNight();
        this.numberOfBeds=house.getNumberOfBeds();
        this.maxNumberOfUsers=house.getMaxNumberOfUsers();
    }

    public House(String city, String description, double kmFromCityCenter, double pricePerNight, int numberOfBeds, int maxNumberOfUsers) {
        this.city = city;
        this.description = description;
        this.kmFromCityCenter = kmFromCityCenter;
        this.pricePerNight = pricePerNight;
        this.numberOfBeds = numberOfBeds;
        this.maxNumberOfUsers = maxNumberOfUsers;
    }

    public void addComoditieToHouse(Comodities comodities) {
        this.comodities.add(comodities);
    }

    public void addRent(Rent rent){
        this.timesRented.add(rent);
    }

    public Set<Comodities> getComodities() {
        return comodities;
    }

    public long getId() {
        return this.id;
    }

    public User getOwner() {
        return owner;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
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

    public Set<Rent> getTimesRented() {
        return timesRented;
    }

    public Set<HouseReviews> getHouseReviews() {
        return houseReviews;
    }

    public Set<User> getBookmarkedBy() {
        return bookmarkedBy;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addReview(HouseReviews houseReviews) {
        this.houseReviews.add(houseReviews);
    }
}
