package com.pizzashop.pizzashopexample.Model;

public class Users {
    private String Name,phone,Password,image,address;

    public Users(){

    }

    public Users(String name, String phone, String password, String image, String address) {
        Name = name;
        this.phone = phone;
        Password = password;
        this.image = image;
        this.address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
