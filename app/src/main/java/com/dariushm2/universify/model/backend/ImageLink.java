package com.dariushm2.universify.model.backend;

import com.google.gson.annotations.SerializedName;

public class ImageLink {
    @SerializedName("href")
    private String url;
    @SerializedName("rel")
    private String rel;
    @SerializedName("render")
    private String render;

    public ImageLink(String url, String rel, String render) {
        this.url = url;
        this.rel = rel;
        this.render = render;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }
}
