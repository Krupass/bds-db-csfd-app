package org.but.feec.csfd.api.user;

public class UserEditView {

    private Long id;
    private String firstName;
    private String surname;
    private String nickname;
    private String email;
    private Long address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "UserEditView{" +
                "firstName='" + firstName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address +
                '}';
    }
}
