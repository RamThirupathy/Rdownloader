package com.ramkt.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
public class Categories
{
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("links")
    private Links links;

    @JsonProperty("photo_count")
    private String photo_count;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public Links getLinks ()
    {
        return links;
    }

    public void setLinks (Links links)
    {
        this.links = links;
    }

    public String getPhoto_count ()
    {
        return photo_count;
    }

    public void setPhoto_count (String photo_count)
    {
        this.photo_count = photo_count;
    }

}
