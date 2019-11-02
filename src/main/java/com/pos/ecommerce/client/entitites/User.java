package com.pos.ecommerce.client.entitites;

import javax.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id") })
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected Long id;

    protected String email = "";
    protected String lastName = "";
    protected String firstName = "";
    protected String phone = "";
    protected String dom = "";
    private String password = "";
    protected String dni = "";
    protected boolean guest = false;

    public User() {

    }

    public User(String firstName, String lastName, String email, String password, String dni, String dom, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.dom = dom;
        this.phone = phone;
    }

    public User(String name, String email, String dom, String phone) {
        this.firstName = name;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
