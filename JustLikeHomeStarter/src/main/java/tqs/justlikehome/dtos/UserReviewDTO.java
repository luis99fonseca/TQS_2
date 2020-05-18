package tqs.justlikehome.dtos;

public class UserReviewDTO {

    private long reviewerId;
    private long reviewedId;
    private double rating;
    private String description;

    public UserReviewDTO(long reviewerId, long reviewedId, double rating, String description) {
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.rating = rating;
        this.description = description;
    }

    public long getReviewerId() {
        return this.reviewerId;
    }

    public long getReviewedId() {
        return this.reviewedId;
    }

    public double getRating() {
        return this.rating;
    }

    public String getDescription() {
        return this.description;
    }


}