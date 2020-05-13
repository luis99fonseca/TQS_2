package tqs.justlikehome.dtos;

public class UserReviewDTO {

    private long ReviewerId;
    private long ReviewedId;
    private double rating;
    private String description;

    public UserReviewDTO(long ReviewerId, long ReviewedId, double rating, String description) {
        this.ReviewerId = ReviewerId;
        this.ReviewedId = ReviewedId;
        this.rating = rating;
        this.description = description;
    }

    public long getReviewerId() {
        return this.ReviewerId;
    }

    public long getReviewedId() {
        return this.ReviewedId;
    }

    public double getRating() {
        return this.rating;
    }

    public String getDescription() {
        return this.description;
    }


}