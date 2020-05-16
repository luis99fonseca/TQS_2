package tqs.justlikehome.dtos;

public class ComoditiesDTO {
    private String type;
    private String description;

    public ComoditiesDTO(String type, String description){
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription(){
        return description;
    }

}
