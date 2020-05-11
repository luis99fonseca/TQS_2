package entities;
import javax.persistence.*;
import java.util.Date;
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

    @OneToMany(targetEntity = House.class,mappedBy = "user",fetch = FetchType.LAZY)
    private Set<House> ownedHouses;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "BookmarkedHouses",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="house_id")}
    )
    private Set<House> bookmarkedHouses = new HashSet<>();

    @OneToMany(targetEntity = Rent.class,mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Rent> purchasedRents;
}
