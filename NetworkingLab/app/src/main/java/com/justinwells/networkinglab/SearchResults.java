package com.justinwells.networkinglab;

import java.util.List;

/**
 * Created by justinwells on 11/17/16.
 */
public class SearchResults {
    private List<CustomObject> items;

    public List<CustomObject> getItems() {
        return items;
    }

    public void setItems(List<CustomObject> items) {
        this.items = items;
    }

    public int getLength () {
        return items.size();
    }

    public List<CustomObject> getList () {
        return items;
    }
}
