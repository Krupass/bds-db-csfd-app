package org.but.feec.csfd.api.person;

public class PersonEditView {

    private Long id;
    private String firstName;
    private String surname;
    private java.sql.Date birthday;
    private Long address;

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
        return "PersonEditView{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address +
                '}';
    }
}
