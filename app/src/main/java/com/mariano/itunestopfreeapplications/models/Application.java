package com.mariano.itunestopfreeapplications.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mariano on 1/6/17.
 */

public class Application extends RealmObject {
    @PrimaryKey
    private long id;

    private String summary;
    private Category category;
    private String title;

    private String image;
    private String link;
    private String rights;

    public Application() {
    }


    public Application(long id, String summary, Category category, String title, String image, String link, String rights) {
        this.id = id;
        this.summary = summary;
        this.category = category;
        this.title = title;
        this.image = image;
        this.link = link;
        this.rights = rights;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }
}
