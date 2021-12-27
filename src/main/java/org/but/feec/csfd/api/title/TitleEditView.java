package org.but.feec.csfd.api.title;

public class TitleEditView {

    private Long id;
    private String firstName;
    private String nickname;
    private String surname;
    private String email;
    private String address;

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
        return "TitleEditView{" +
                "firstName='" + firstName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address +
                '}';
    }
}
