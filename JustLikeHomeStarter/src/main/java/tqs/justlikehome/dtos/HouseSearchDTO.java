package tqs.justlikehome.dtos;

import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;

public class HouseSearchDTO {
    private String city;
    private String description;
    private String houseName;
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;
    private double rating;
    private double userRating;
    private long userId;
    private String ownerName;

    public HouseSearchDTO(House house, User owner,double rating) {
        this.city = house.getCity();
        this.description = house.getDescription();
        this.kmFromCityCenter = house.getKmFromCityCenter();
        this.pricePerNight = house.getPricePerNight();
        this.houseName = house.getHouseName();
        this.numberOfBeds = house.getNumberOfBeds();
        this.maxNumberOfUsers = house.getMaxNumberOfUsers();
        this.userId=owner.getId();
        this.rating=rating;
        this.ownerName =owner.getUsername();
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
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

    public String getHouseName() {
        return houseName;
    }

    public double getRating() {
        return rating;
    }

    public long getUserId() {
        return userId;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
