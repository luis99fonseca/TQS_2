package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="comodities")
public class Comodities {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String type;
    private String description;

    @ManyToMany(mappedBy ="comodities")
    private Set<House> houseWithComodity =new HashSet<>();
}
