package org.but.feec.csfd.api.person;

import java.util.Arrays;
import java.util.Date;

public class PersonCreateView {

    private String firstName;
    private String surname;
    private java.sql.Date birthday;
    private Long address;

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

    public java.sql.Date getBirthday() {
        return birthday;
    }

    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }

    public Long getAddress(){
        return address;
    }

    public void setAddress(Long address){
        if(address == null){
            address = null;
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
