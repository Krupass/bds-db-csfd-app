package org.but.feec.csfd.api.title;

public class TitleEditView {

    private Long id;
    private String title;
    private Long type;
    private java.sql.Date year;
    private Long lenght;
    private Long country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Long getType(){
        return type;
    }

    public void setType(Long type){
        this.type = type;
    }

    public java.sql.Date getYear(){
        return year;
    }

    public void setYear(java.sql.Date year){
        this.year = year;
    }

    public Long getLenght(){
        return lenght;
    }

    public void setLenght(Long lenght){
        this.lenght = lenght;
    }

    public Long getCountry(){
        return country;
    }

    public void setCountry(Long country){
        this.country = country;
    }

    @Override
    public String toString() {
        return "TitleEditView{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", year='" + year + '\'' +
                ", lenght='" + lenght + '\'' +
                ", country='" + country +
                '}';
    }
}
