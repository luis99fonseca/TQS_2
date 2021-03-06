package tqs.justlikehome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import tqs.justlikehome.dtos.UserDTO;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(nullable = false)
    @Length(min=5)
    private String password;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<House> ownedHouses = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Bookmarked_Houses",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="house_id")}
    )
    @OrderBy("id ASC")
    private Set<House> bookmarkedHouses = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Rent> purchasedRents = new HashSet<>();

    @OneToMany(mappedBy = "userReviewing",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserReviews> userReviews = new HashSet<>();

    @OneToMany(mappedBy = "userReviewed",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserReviews> userReviewed = new HashSet<>();

    // EAGER SHOULDN'T BE USED BUT SPRING IS NOT INITIALIZING IT ANY OTHER WAY ...
    @OneToMany(mappedBy = "reviewer",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<HouseReviews> houseReviews = new HashSet<>();

    public User(){

    }

    public User(UserDTO userDTO) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.username = userDTO.getUsername();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.birthDate = Date.from(LocalDate.parse(userDTO.getBirthDate(), parser).atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.password=userDTO.getPassword();
    }

    public User(String username, String firstName, String lastName, GregorianCalendar birthDate, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = Date.from(birthDate.toZonedDateTime().toInstant());
        this.password=password;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }
    public void addPurchasedRent(Rent rent){
        this.purchasedRents.add(rent);
    }

    public void addMyReview(HouseReviews houseReview){
        this.houseReviews.add(houseReview);
    }

    public void addMyReview(UserReviews userReview){
        this.userReviews.add(userReview);
    }

    public void addBookmarkedHouse(House house) {this.bookmarkedHouses.add(house);}

    public void addReview(UserReviews userReview){
        this.userReviewed.add(userReview);
    }

    public long getId() {
        return this.id;
    }

    public void addHouse(House house){
        ownedHouses.add(house);
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
