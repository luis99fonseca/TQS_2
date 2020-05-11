package tqs.justlikehome.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private Date rentStart;

    @Temporal(TemporalType.DATE)
    private Date rentEnd;

    public Rent(){

    }

    public Rent(House house, User user, Date rentStart, Date rentEnd) {
        this.house = house;
        this.user = user;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
    }

    public House getHouse() {
        return house;
    }

    public User getUser() {
        return user;
    }

    public Date getRentStart() {
        return rentStart;
    }

    public Date getRentEnd() {
        return rentEnd;
    }
}
