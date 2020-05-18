package tqs.justlikehome.dtos;

public class HouseReviewDTO {

    private long reviewerId;
    private long houseId;
    private double rating;
    private String description;


    public HouseReviewDTO(long reviewerId, long houseId, double rating, String description) {
        this.reviewerId = reviewerId;
        this.houseId = houseId;
        this.rating = rating;
        this.description = description;
    }

    public long getReviewerId() {
        return this.reviewerId;
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