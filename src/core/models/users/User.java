package cms.core.models.users;

import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Created by Leonardo on 9/19/2016.
 */
public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String middleName;
    protected String phone;
    protected String email;
    protected String userName;
    protected String password;

    protected String UUID;

    public User(String firstName, String lastName, String email, String phone, String id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;

        this.UUID = id;
        if (this.UUID == null){
            UUID _UUID =  randomUUID();
            this.UUID =  _UUID.toString();
        }
    }

    public String getUUID() {
        return this.UUID.toString();
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.firstName);

        if (this.middleName != null && this.middleName.length() > 0) {
            sb.append(" " + this.middleName);
        }

        sb.append(" " + this.lastName);
        return sb.toString();
    }


    // Setters / Getters
    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setMiddleName(String name) {
        this.middleName = name;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

}