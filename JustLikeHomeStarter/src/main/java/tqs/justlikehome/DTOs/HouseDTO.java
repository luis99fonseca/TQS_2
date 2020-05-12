package tqs.justlikehome.DTOs;

public class HouseDTO {
    private String city;
    private String description;
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;
    private long userId;

    public HouseDTO(String city, String description, double kmFromCityCenter, double pricePerNight, int numberOfBeds, int maxNumberOfUsers,long userId) {
        this.city = city;
        this.description = description;
        this.kmFromCityCenter = kmFromCityCenter;
        this.pricePerNight = pricePerNight;
        this.numberOfBeds = numberOfBeds;
        this.maxNumberOfUsers = maxNumberOfUsers;
        this.userId=userId;
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
}
