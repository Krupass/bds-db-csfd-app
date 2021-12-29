package org.but.feec.csfd.api.dummy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DummyBasicView {
    private StringProperty string = new SimpleStringProperty();

    public String getString(){
        return stringProperty().get();
    }

    public void setString(String string){
        this.stringProperty().setValue(string);
    }

    public StringProperty stringProperty() {
        return string;
    }

}
