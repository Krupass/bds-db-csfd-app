package org.but.feec.csfd.api.person;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class PersonBasicView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty birthday = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private String find;
    private String choice;

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getGivenName() {
        return givenNameProperty().get();
    }

    public void setGivenName(String givenName) {
        this.givenNameProperty().setValue(givenName);
    }

    public String getFamilyName() {
        return familyNameProperty().get();
    }

    public void setFamilyName(String familyName) {
        this.familyNameProperty().setValue(familyName);
    }

    public String getBirthday() {
        return birthdayProperty().get();
    }

    public void setBirthday(String birthday) {
        this.birthdayProperty().set(birthday);
    }

    public String getCity() {
        return cityProperty().get();
    }

    public void setCity(String city) {
        this.cityProperty().setValue(city);
    }

    public String getAddress(){
        return  addressProperty().get();
    }

    public void setAddress(String address){
        this.addressProperty().set(address);
    }

    public String getFind(){
        return find;
    }

    public void setFind(String find){
        this.find = find;
    }

    public String getChoice(){
        return choice;
    }

    public void setChoice(String choice){
        this.choice = choice;
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public StringProperty birthdayProperty() {
        return birthday;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty addressProperty() {
        return address;
    }

}
