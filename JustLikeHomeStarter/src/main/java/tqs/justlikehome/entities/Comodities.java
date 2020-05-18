package tqs.justlikehome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tqs.justlikehome.dtos.ComoditiesDTO;

import javax.persistence.*;

@Entity
@Table(name="comodities")
public class Comodities {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String type;
    private String description;

    @ManyToOne
    @JoinColumn(name="house_id")
    @JsonIgnore
    private House house;

    public Comodities(){

    }

    public Comodities(ComoditiesDTO comoditiesDTO){
        this.type = comoditiesDTO.getType();
        this.description = comoditiesDTO.getDescription();
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
