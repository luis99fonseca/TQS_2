package tqs.justlikehome.dtos;

public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String birthDate;

    public UserDTO(String username, String firstName, String lastName, String birthDate) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
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

    public String getBirthDate() {
        return birthDate;
    }
}
