package application;

import java.time.LocalDate;

public class User {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String phone;
    private String email;
    private String password;

    public User(String firstName, String lastName, LocalDate dob, String phone, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Getters and setters

    @Override
    public String toString() {
        return firstName + "," + lastName + "," + dob + "," + phone + "," + email + "," + password;
    }
}