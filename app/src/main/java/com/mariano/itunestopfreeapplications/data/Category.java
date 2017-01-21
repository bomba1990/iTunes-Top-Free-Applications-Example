package com.mariano.itunestopfreeapplications.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mariano on 1/6/17.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private long id;
    private String label;
    private String term;

    public RealmList<Application> entries;

    public Category() {
    }

    public Category(long id, String label, String term) {
        this.id = id;
        this.label = label;
        this.term = term;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
