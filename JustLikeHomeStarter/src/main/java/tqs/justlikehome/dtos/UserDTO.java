package tqs.justlikehome.dtos;

public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String password;

    public UserDTO(String username, String firstName, String lastName, String birthDate, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

}
