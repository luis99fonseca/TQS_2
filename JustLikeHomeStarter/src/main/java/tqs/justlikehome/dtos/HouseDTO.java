package tqs.justlikehome.dtos;

import tqs.justlikehome.entities.Comodities;

import java.util.Set;

public class HouseDTO {
    private String city;
    private String description;
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;
    private long userId;
    private String houseName;
    private Set<Comodities> comodities;
    private long houseId;

    public HouseDTO(String city, String description, double kmFromCityCenter, double pricePerNight, int numberOfBeds, int maxNumberOfUsers,long userId,String houseName, Set<Comodities> comodities) {
        this.city = city;
        this.description = description;
        this.kmFromCityCenter = kmFromCityCenter;
        this.pricePerNight = pricePerNight;
        this.numberOfBeds = numberOfBeds;
        this.maxNumberOfUsers = maxNumberOfUsers;
        this.userId=userId;
        this.houseName=houseName;
        this.comodities = comodities;
    }

    public void setHouseId(long houseId){
        this.houseId = houseId;

    }

    public long getHouseId(){
        return houseId;
    }

    public void setHouseId(long houseId){
        this.houseId = houseId;

    }

    public long getHouseId(){
        return houseId;
    }

    public String getHouseName() {
        return houseName;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id){
        this.userId=id;
    }

    public Set<Comodities> getComodities() {
        return comodities;
    }
}
