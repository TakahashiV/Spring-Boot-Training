package com.internship.training.users.models.entities;

public class Address {

    private String zipcode;
    private String phone;
    private String location;

    // Construtor padrão (necessário para o Spring Data / Jackson)
    public Address() {
    }

    // Construtor completo
    public Address(String zipcode, String phone, String location) {
        this.zipcode = zipcode;
        this.phone = phone;
        this.location = location;
    }

    // Getters e Setters
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
