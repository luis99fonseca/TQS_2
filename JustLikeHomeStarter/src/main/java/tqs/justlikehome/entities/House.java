package tqs.justlikehome.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="house")
public class House {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String city;
    private double kmFromCityCenter;
    private double pricePerNight;
    private int numberOfBeds;
    private int maxNumberOfUsers;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User owner;

}
