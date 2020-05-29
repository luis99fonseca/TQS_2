package tqs.justlikehome.dtos;

import tqs.justlikehome.entities.*;

import java.util.Date;
import java.util.Set;

public class UserInfoDTO {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Set<House> bookmarkedHouses;
    private Set<Rent> purchasedRents;
    private double rating;



    public UserInfoDTO(User user, double rating,Set<Rent> rents) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthDate = user.getBirthDate();
        this.bookmarkedHouses = user.getBookmarkedHouses();
        this.purchasedRents = rents;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Set<House> getBookmarkedHouses() {
        return bookmarkedHouses;
    }

    public Set<Rent> getPurchasedRents() {
        return purchasedRents;
    }

    public double getRating() {
        return rating;
    }
}
