package com.pos.ecommerce.client.dto;

import java.io.Serializable;
import java.util.List;

/**
 * UserDTO
 *
 * @author Michal Vlček<mychalvlcek@gmail.com>
 * @date 28.03.15
 */
public class UserDTO implements Serializable {
    private Long id;

    private String firstName="";
    private String lastName="";
    private String email="";
    private String phone="";
    private String dom="";
    private String password="";
    private String dni="";
    private boolean guest=false;

    public UserDTO(){

    }

    public UserDTO(String firstName, String lastName, String email, String password, String dni, String dom, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.dom = dom;
        this.phone = phone;
    }

    public UserDTO(String name, String lastName,String email, String dom, String phone) {
        this.firstName = name;
        this.lastName = lastName;
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
        if (lastName!=null){
            this.lastName = lastName;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email!=null){
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password!=null){
            this.password = password;
        }
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni!=null){
            this.dni = dni;
        }
    }

    public void setGuest(boolean guest) {
        if (new Boolean(guest)!=null){
            this.guest = guest;
        }
    }

    public boolean getGuest() {
        return guest;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        if (dom!=null){
            this.dom = dom;
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone!=null){
            this.phone = phone;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id!=null){
            this.id = id;
        }
    }

    public String getFullName() {
        return firstName.concat(" ").concat(lastName);
    }
}