package tqs.justlikehome.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user",uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.PERSIST)
    private Set<House> ownedHouses = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Bookmarked_Houses",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="house_id")}
    )
    private Set<House> bookmarkedHouses = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    private Set<Rent> purchasedRents = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    private Set<UserReviews> userReviews = new HashSet<>();

    public User(){

    }

    public User(String username, String firstName, String lastName, GregorianCalendar birthDate) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = Date.from(birthDate.toZonedDateTime().toInstant());
    }

    public Long getId() {
        return id;
    }

    public void addHouse(House house){
        ownedHouses.add(house);
    }
}
