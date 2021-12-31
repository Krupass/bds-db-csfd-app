package org.but.feec.csfd.api.dummy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DummyBasicView {
    private StringProperty string = new SimpleStringProperty();
    private String find;

    public String getString(){
        return stringProperty().get();
    }

    public void setString(String string){
        this.stringProperty().setValue(string);
    }

    public String getFind(){
        return find;
    }

    public void setFind(String find){
        this.find = find;
    }

    public StringProperty stringProperty() {
        return string;
    }

}
