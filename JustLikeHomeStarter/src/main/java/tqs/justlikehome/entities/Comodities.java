package tqs.justlikehome.entities;

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

    @ManyToMany(mappedBy = "comodities")
    private Set<House> houses = new HashSet<>();

    public Comodities(){

    }

    public Comodities(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
