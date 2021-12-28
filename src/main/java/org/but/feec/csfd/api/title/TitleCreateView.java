package org.but.feec.csfd.api.title;

import java.util.Arrays;

public class TitleCreateView {

    private String name;
    private String type;
    private String country;
    private String year;
    private String lenght;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TitleCreateView{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", year='" + year + '\'' +
                ", lenght='" + lenght + '\'' +
                ", description='" + description +
                '}';
    }
}
