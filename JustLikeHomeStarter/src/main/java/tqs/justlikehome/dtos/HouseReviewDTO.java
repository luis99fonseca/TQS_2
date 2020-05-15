package tqs.justlikehome.dtos;

public class HouseReviewDTO {

    private long ReviewerId;
    private long houseId;
    private double rating;
    private String description;


    public HouseReviewDTO(long ReviewerId, long houseId, double rating, String description) {
        this.ReviewerId = ReviewerId;
        this.houseId = houseId;
        this.rating = rating;
        this.description = description;
    }

    public long getReviewerId() {
        return this.ReviewerId;
    }

    public long getHouseId() {
        return this.houseId;
    }

    public double getRating() {
        return this.rating;
    }

    public String getDescription() {
        return this.description;
    }

}