package tqs.justlikehome.dtos;

public class ComoditiesDTO {
    private String type;
    private String description;
    private long house;

    public ComoditiesDTO(String type, String description, long house){
        this.type = type;
        this.description = description;
        this.house = house;
    }

    public String getType() {
        return type;
    }

    public String getDescription(){
        return description;
    }

    public long getHouse() {
        return house;
    }
}
