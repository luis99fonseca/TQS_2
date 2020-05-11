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

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "HouseComodities",
            joinColumns = {@JoinColumn(name="house_id")},
            inverseJoinColumns = {@JoinColumn(name="comodities_id")}
    )
    private Set<Comodities> comodities = new HashSet<>();

    @ManyToMany(mappedBy = "house")
    private Set<User> bookmarkedBy = new HashSet<>();

    @OneToMany(targetEntity = Rent.class,mappedBy = "house",fetch = FetchType.LAZY)
    private Set<Rent> timesRented;

}
