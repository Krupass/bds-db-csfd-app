package org.but.feec.csfd.api.title;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TitleBasicView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty typeId = new SimpleStringProperty();
    private StringProperty year = new SimpleStringProperty();
    private StringProperty lenght = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();
    private StringProperty countryId = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getTitle(){
        return titleProperty().get();
    }

    public void setTitle(String title){
        this.titleProperty().setValue(title);
    }

    public String getType(){
        return typeProperty().get();
    }

    public void setType(String type){
        this.typeProperty().setValue(type);
    }

    public String getTypeId(){
        return typeIdProperty().get();
    }

    public void setTypeId(String typeId){
        this.typeIdProperty().setValue(typeId);
    }

    public String getYear(){
        return yearProperty().get();
    }

    public void setYear(String year){
        this.yearProperty().setValue(year);
    }

    public String getLenght(){
        return lenghtProperty().get();
    }

    public void setLenght(String lenght){
        this.lenghtProperty().setValue(lenght);
    }

    public String getCountry(){
        return countryProperty().get();
    }

    public void setCountry(String country){
        this.countryProperty().setValue(country);
    }

    public String getCountryId(){
        return countryIdProperty().get();
    }

    public void setCountryId(String countryId){
        this.countryIdProperty().setValue(countryId);
    }

    public String getDescription(){
        return descriptionProperty().get();
    }

    public void setDescription(String description){
        this.descriptionProperty().setValue(description);
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public StringProperty typeIdProperty() {
        return typeId;
    }

    public StringProperty yearProperty() {
        return year;
    }

    public StringProperty lenghtProperty() {
        return lenght;
    }

    public StringProperty countryProperty() {
        return country;
    }

    public StringProperty countryIdProperty() {
        return countryId;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

}
