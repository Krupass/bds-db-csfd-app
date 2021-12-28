package org.but.feec.csfd.api;

import java.util.Arrays;

public class PersonCreateView {

    private String firstName;
    private String surname;
    private String birthday;
    private String address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        if(address == null || address == ""){
            address = "NULL";
        }
        this.address = address;
    }

    @Override
    public String toString() {
        return "PersonCreateView{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + birthday +
                '}';
    }
}
