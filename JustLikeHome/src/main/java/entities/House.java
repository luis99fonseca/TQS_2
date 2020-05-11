package entities;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    @OneToMany(targetEntity = Comodities.class,mappedBy = "house",fetch = FetchType.LAZY)
    private Set<Comodities> comodities;

    @ManyToMany(mappedBy = "house")
    private Set<User> bookmarkedBy = new HashSet<>();
}
