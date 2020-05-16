package tqs.justlikehome.entities;

import tqs.justlikehome.dtos.UserDTO;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user",uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    private Set<House> ownedHouses = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Bookmarked_Houses",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="house_id")}
    )
    private Set<House> bookmarkedHouses = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Rent> purchasedRents = new HashSet<>();

    @OneToMany(mappedBy = "userReviewing",cascade = CascadeType.ALL)
    private Set<UserReviews> userReviews = new HashSet<>();

    @OneToMany(mappedBy = "userReviewed",cascade = CascadeType.ALL)
    private Set<UserReviews> userReviewed = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<HouseReviews> houseReviews = new HashSet<>();

    public User(){

    }

    public User(UserDTO userDTO) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.username = userDTO.getUsername();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.birthDate = Date.from(LocalDate.parse(userDTO.getBirthDate(), parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Set<House> getOwnedHouses() {
        return ownedHouses;
    }

    public Set<House> getBookmarkedHouses() {
        return bookmarkedHouses;
    }

    public Set<Rent> getPurchasedRents() {
        return purchasedRents;
    }

    public Set<UserReviews> getUserReviews() {
        return userReviews;
    }

    public Set<UserReviews> getUserReviewed() {
        return userReviewed;
    }

    public Set<HouseReviews> getHouseReviews() {
        return houseReviews;
    }

    public User(String username, String firstName, String lastName, GregorianCalendar birthDate) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = Date.from(birthDate.toZonedDateTime().toInstant());
    }

    public void addPurchasedRent(Rent rent){
        this.purchasedRents.add(rent);
    }

    public Long getId() {
        return this.id;
    }

    public void addHouse(House house){
        ownedHouses.add(house);
    }

    public void addReview(UserReviews userReviews){
        this.userReviews.add(userReviews);
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
