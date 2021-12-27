package org.but.feec.csfd.api.title;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TitleDetailView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty nickname = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty street = new SimpleStringProperty();
    private StringProperty houseNumber = new SimpleStringProperty();
    private StringProperty created = new SimpleStringProperty();

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getEmail() {
        return emailProperty().get();
    }

    public void setEmail(String email) {
        this.emailProperty().setValue(email);
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

    public String getNickname() {
        return nicknameProperty().get();
    }

    public void setNickname(String nickname) {
        this.nicknameProperty().set(nickname);
    }

    public String getCity() {
        return cityProperty().get();
    }

    public void setCity(String city) {
        this.cityProperty().setValue(city);
    }

    public String gethouseNumber() {
        return houseNumberProperty().get();
    }

    public void sethouseNumber(String houseNumber) {
        this.houseNumberProperty().setValue(houseNumber);
    }

    public String getStreet() {
        return streetProperty().get();
    }

    public void setStreet(String street) {
        this.streetProperty().setValue(street);
    }

    public String getCreated() {
        return createdProperty().get();
    }

    public void setCreated(String created) {
        this.createdProperty().setValue(created);
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

    public StringProperty nicknameProperty() {
        return nickname;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty houseNumberProperty() {
        return houseNumber;
    }

    public StringProperty streetProperty() {
        return street;
    }

    public StringProperty createdProperty() {
        return created;
    }


}
