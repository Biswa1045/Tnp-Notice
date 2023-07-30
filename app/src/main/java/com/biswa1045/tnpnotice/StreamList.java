package com.biswa1045.tnpnotice;

import java.io.Serializable;

public class StreamList implements Serializable {

    private String Name;
    private boolean isChecked = false;

    public StreamList(String Name,boolean isChecked) {
        this.Name = Name;
        this.isChecked = isChecked;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}


