package org.but.feec.csfd.api.title;

public class TitleEditView {

    private Long id;
    private String title;
    private String type;
    private String year;
    private String lenght;
    private String country;

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

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getYear(){
        return year;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getLenght(){
        return lenght;
    }

    public void setLenght(String lenght){
        this.lenght = lenght;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
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
