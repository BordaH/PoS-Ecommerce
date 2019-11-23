package com.pos.ecommerce.client.entitites;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;

    private String email = "";
    private String lastName = "";
    private String firstName = "";
    private String phone = "";
    private String dom = "";
    private String dni = "";
    private boolean guest = false;

    public Client() {

    }

    public Client(String firstName, String lastName, String email, String dni, String dom, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dni = dni;
        this.dom = dom;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public boolean getGuest() {
        return guest;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }
}
