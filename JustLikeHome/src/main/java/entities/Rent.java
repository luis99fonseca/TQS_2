package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Rent {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="house_id")
    private House house;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private Date rentStart;

    @Temporal(TemporalType.DATE)
    private Date rentEnd;
}
