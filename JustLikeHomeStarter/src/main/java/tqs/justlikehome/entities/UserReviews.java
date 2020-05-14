package tqs.justlikehome.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import tqs.justlikehome.dtos.UserReviewDTO;

@Entity
@Table(name = "user_reviews")
public class UserReviews {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="from_user_id")
    private User userReviewing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="to_user_id")
    private User userReviewed;


    @Min(0)
    @Max(5)
    private double rating;

    @Size(max=300)
    private String description;

    public UserReviews(UserReviewDTO userReviewDTO){
        this.rating = userReviewDTO.getRating();
        this.description = userReviewDTO.getDescription();
    }

    public void setUserReviewing(User user){
        this.userReviewing=user;
    }

    public UserReviews(double rating, String description) {
        this.rating = rating;
        this.description = description;
    }

    public void setUserReviewed(User user){
        this.userReviewed=user;
    }


    public User getUserReviewing() {
        return this.userReviewing;
    }

    public User getUserReviewed() {
        return this.userReviewed;
    }

    public double getRating() {
        return this.rating;
    }

    public String getDescription() {
        return this.description;
    }

}
