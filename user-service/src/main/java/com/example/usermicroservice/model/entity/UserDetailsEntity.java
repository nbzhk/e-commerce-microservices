package com.example.usermicroservice.model.entity;

import com.example.usermicroservice.model.dto.UserDetailsDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "user_details")
public class UserDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(length = 12)
    private String phoneNumber;
    @Column(length = 50)
    private String address;
    @Column(length = 20)
    private String city;
    @Column(length = 15)
    private String country;
    @Column( length = 7)
    private String zip;

    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private UserEntity user;

    public UserDetailsEntity() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void updateDetails(UserDetailsDTO userDetailsDTO) {
        if (userDetailsDTO.getFirstName() != null) {
            this.setFirstName(userDetailsDTO.getFirstName());
        }
        if (userDetailsDTO.getLastName() != null) {
            this.setLastName(userDetailsDTO.getLastName());
        }
        if (userDetailsDTO.getPhoneNumber() != null) {
            this.setPhoneNumber(userDetailsDTO.getPhoneNumber());
        }
        if (userDetailsDTO.getAddress() != null) {
            this.setAddress(userDetailsDTO.getAddress());
        }
        if (userDetailsDTO.getCity() != null) {
            this.setCity(userDetailsDTO.getCity());
        }
        if (userDetailsDTO.getCountry() != null) {
            this.setCountry(userDetailsDTO.getCountry());
        }
        if (userDetailsDTO.getZip() != null) {
            this.setZip(userDetailsDTO.getZip());
        }
    }
}
