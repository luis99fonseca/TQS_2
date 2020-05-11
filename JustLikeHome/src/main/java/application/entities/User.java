package entities;
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

    public User(String username, String firstName, String lastName, GregorianCalendar birthDate) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = Date.from(birthDate.toZonedDateTime().toInstant());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<House> getOwnedHouses() {
        return ownedHouses;
    }

    public void setOwnedHouses(Set<House> ownedHouses) {
        this.ownedHouses = ownedHouses;
    }

    public Set<House> getBookmarkedHouses() {
        return bookmarkedHouses;
    }

    public void setBookmarkedHouses(Set<House> bookmarkedHouses) {
        this.bookmarkedHouses = bookmarkedHouses;
    }

    public Set<Rent> getPurchasedRents() {
        return purchasedRents;
    }

    public void setPurchasedRents(Set<Rent> purchasedRents) {
        this.purchasedRents = purchasedRents;
    }
}
