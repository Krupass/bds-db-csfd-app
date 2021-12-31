package org.but.feec.csfd.api.title;

public class TitleCreateView {

    private String name;
    private Long type;
    private Long country;
    private java.sql.Date year;
    private Long lenght;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public java.sql.Date getYear() {
        return year;
    }

    public void setYear(java.sql.Date year) {
        this.year = year;
    }

    public Long getLenght() {
        return lenght;
    }

    public void setLenght(Long lenght) {
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
