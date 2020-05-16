package tqs.justlikehome.dtos;

import tqs.justlikehome.entities.House;
import tqs.justlikehome.entities.User;

public class HouseSearchDTO {
    private String city;
    private String description;
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;
    private double rating;
    private long userId;
    private String ownerName;

    public HouseSearchDTO(House house, User owner,double rating) {
        this.city = house.getCity();
        this.description = house.getDescription();
        this.kmFromCityCenter = house.getKmFromCityCenter();
        this.pricePerNight = house.getPricePerNight();
        this.numberOfBeds = house.getNumberOfBeds();
        this.maxNumberOfUsers = house.getMaxNumberOfUsers();
        this.userId=owner.getId();
        this.rating=rating;
        this.ownerName=owner.getUsername();
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
