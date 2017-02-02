package com.davidsilvan.fileconcealer;

/**
 * Created by David on 3/25/2016.
 */
public class Item {

    private String item;

    public Item(String item) {
        this.item = item;
    }

    public String getString() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
