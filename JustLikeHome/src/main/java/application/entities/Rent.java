package application.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Rent {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="house_id")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private Date rentStart;

    @Temporal(TemporalType.DATE)
    private Date rentEnd;

    public Rent(){

    }

    public Rent(Long id, House house, User user, Date rentStart, Date rentEnd) {
        this.id = id;
        this.house = house;
        this.user = user;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
    }
}
