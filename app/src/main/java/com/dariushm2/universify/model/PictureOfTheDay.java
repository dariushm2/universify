package com.dariushm2.universify.model;

import java.io.Serializable;

public class PictureOfTheDay implements Serializable {

    public static final String PICTURE_OF_THE_DAY = "PictureOfTheDay";

    private String title;
    private String explanation;
    private  String date;
    private String url;

    public PictureOfTheDay(String title, String explanation, String date, String url) {
        this.title = title;
        this.explanation = explanation;
        this.date = date;
        this.url = url;
    }

    public PictureOfTheDay() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
