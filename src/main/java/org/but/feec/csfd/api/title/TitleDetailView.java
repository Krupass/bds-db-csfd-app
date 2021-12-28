package org.but.feec.csfd.api.title;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TitleDetailView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty genre = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();
    private StringProperty year = new SimpleStringProperty();
    private StringProperty lenght = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        this.nameProperty().setValue(name);
    }

    public String getType() {
        return typeProperty().get();
    }

    public void setType(String type) {
        this.typeProperty().setValue(type);
    }

    public String getGenre() {
        return genreProperty().get();
    }

    public void setGenre(String genre) {
        this.genreProperty().setValue(genre);
    }

    public String getCountry() {
        return countryProperty().get();
    }

    public void setCountry(String country) {
        this.countryProperty().setValue(country);
    }

    public String getYear() {
        return yearProperty().get();
    }

    public void setYear(String year) {
        this.yearProperty().setValue(year);
    }

    public String getLenght() {
        return lenghtProperty().get();
    }

    public void setLenght(String lenght) {
        this.lenghtProperty().setValue(lenght);
    }

    public String getDescription() {
        return descriptionProperty().get();
    }

    public void setDescription(String description) {
        this.descriptionProperty().setValue(description);
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public StringProperty countryProperty() {
        return country;
    }

    public StringProperty yearProperty() {
        return year;
    }

    public StringProperty lenghtProperty() {
        return lenght;
    }

    public StringProperty descriptionProperty() {
        return description;
    }


}
