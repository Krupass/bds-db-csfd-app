package org.but.feec.csfd.api;

public class PersonEditView {

    private Long id;
    private String firstName;
    private String surname;
    private String birthday;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "PersonEditView{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address +
                '}';
    }
}
