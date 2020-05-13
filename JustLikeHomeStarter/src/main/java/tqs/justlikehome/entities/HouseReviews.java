package tqs.justlikehome.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import tqs.justlikehome.dtos.HouseReviewDTO;

@Entity
@Table(name = "house_reviews")
public class HouseReviews {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="house_id")
    private House house;

    @Min(0)
    @Max(5)
    private double rating;

    @Size(max=300)
    private String description;

    public HouseReviews(HouseReviewDTO houseReviewDTO){
        this.rating = houseReviewDTO.getRating();
        this.description = houseReviewDTO.getDescription();
    }

    public void setUser(User user){
        this.user=user;
    }

    public void setHouse(House house){
        this.house=house;
    }
}
