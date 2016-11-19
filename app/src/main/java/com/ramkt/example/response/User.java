package com.ramkt.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
public class User {
    @JsonProperty("id")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("profile_image")
    private Profile_image profile_image;

    @JsonProperty("name")
    private String name;

    @JsonProperty("links")
    private Links links;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public Profile_image getProfile_image ()
    {
        return profile_image;
    }

    public void setProfile_image (Profile_image profile_image)
    {
        this.profile_image = profile_image;
    }

    public String getName ()
    {
        return name;
    }


    public void setName (String name)
    {
        this.name = name;
    }

    public Links getLinks ()
    {
        return links;
    }

    public void setLinks (Links links)
    {
        this.links = links;
    }

}
