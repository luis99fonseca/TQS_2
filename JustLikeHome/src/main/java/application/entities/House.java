package application.entities;

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
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User owner;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "House_Comodities",
            joinColumns = {@JoinColumn(name="house_id")},
            inverseJoinColumns = {@JoinColumn(name="comodities_id")}
    )
    private Set<Comodities> comodities = new HashSet<>();

    @ManyToMany(mappedBy = "bookmarkedHouses")
    private Set<User> bookmarkedBy = new HashSet<>();

    @OneToMany(mappedBy = "house")
    private Set<Rent> timesRented;

    public House(){

    }

    public House(String city, double kmFromCityCenter, double pricePerNight, int numberOfBeds, int maxNumberOfUsers, User owner, Set<Comodities> comodities) {
        this.city = city;
        this.kmFromCityCenter = kmFromCityCenter;
        this.pricePerNight = pricePerNight;
        this.numberOfBeds = numberOfBeds;
        this.maxNumberOfUsers = maxNumberOfUsers;
        this.owner = owner;
        this.comodities = comodities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getKmFromCityCenter() {
        return kmFromCityCenter;
    }

    public void setKmFromCityCenter(double kmFromCityCenter) {
        this.kmFromCityCenter = kmFromCityCenter;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public int getMaxNumberOfUsers() {
        return maxNumberOfUsers;
    }

    public void setMaxNumberOfUsers(int maxNumberOfUsers) {
        this.maxNumberOfUsers = maxNumberOfUsers;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Comodities> getComodities() {
        return comodities;
    }

    public void setComodities(Set<Comodities> comodities) {
        this.comodities = comodities;
    }

    public Set<User> getBookmarkedBy() {
        return bookmarkedBy;
    }

    public void setBookmarkedBy(Set<User> bookmarkedBy) {
        this.bookmarkedBy = bookmarkedBy;
    }

    public Set<Rent> getTimesRented() {
        return timesRented;
    }

    public void setTimesRented(Set<Rent> timesRented) {
        this.timesRented = timesRented;
    }
}
