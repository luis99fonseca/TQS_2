package tqs.justlikehome.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import tqs.justlikehome.dtos.HouseReviewDTO;

@Entity
@Table(name = "houseReviews")
public class HouseReviews {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="house_id")
    private House house;

    @Min(0)
    @Max(5)
    private double rating;

    @Size(max=300)
    private String description;

    public HouseReviews(){

    }

    public HouseReviews(User reviewer, House house, @Min(0) @Max(5) double rating, @Size(max = 300) String description) {
        this.reviewer = reviewer;
        this.house = house;
        this.rating=rating;
        this.description=description;
    }
    public HouseReviews(HouseReviewDTO houseReviewDTO){
        this.rating = houseReviewDTO.getRating();
        this.description = houseReviewDTO.getDescription();
    }

    public HouseReviews(double rating, String description){
        this.rating = rating;
        this.description = description;
    }

    public void setReviewer(User user){
        this.reviewer =user;
    }

    public void setHouse(House house){
        this.house=house;
    }

    public User getReviewer(){
        return this.reviewer;
    }

    public House getHouse() {
        return this.house;
    }

    public double getRating() {
        return this.rating;
    }

    public String getDescription() {
        return this.description;
    }
    
}
