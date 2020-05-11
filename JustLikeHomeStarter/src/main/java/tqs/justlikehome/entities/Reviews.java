package tqs.justlikehome.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name="reviews")
public class Reviews {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Max(5)
    private double rating;

    @Size(max=300)
    private String description;
}
