package com.ramkt.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
public class Links {
    @JsonProperty("photos")
    private String photos;

    @JsonProperty("likes")
    private String likes;

    @JsonProperty("html")
    private String html;

    @JsonProperty("self")
    private String self;

    public String getPhotos ()
    {
        return photos;
    }

    public void setPhotos (String photos)
    {
        this.photos = photos;
    }

    public String getLikes ()
    {
        return likes;
    }

    public void setLikes (String likes)
    {
        this.likes = likes;
    }

    public String getHtml ()
    {
        return html;
    }

    public void setHtml (String html)
    {
        this.html = html;
    }

    public String getSelf ()
    {
        return self;
    }

    public void setSelf (String self)
    {
        this.self = self;
    }
}
