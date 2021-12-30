package org.but.feec.csfd.api.user;

import java.util.Arrays;

public class UserCreateView {

    private String firstName;
    private String surname;
    private String nickname;
    private String email;
    private char[] pwd;
    private Long address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char[] getPwd() {
        return pwd;
    }

    public void setPwd(char[] pwd) {
        this.pwd = pwd;
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
        return "UserCreateView{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", surname='" + surname + '\'' +
                ", pwd=" + Arrays.toString(pwd) +
                '}';
    }
}
