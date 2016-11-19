package com.ramkt.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
public class Profile_image {
    @JsonProperty("small")
    private String small;

    @JsonProperty("large")
    private String large;

    @JsonProperty("medium")
    private String medium;

    public String getSmall ()
    {
        return small;
    }

    public void setSmall (String small)
    {
        this.small = small;
    }

    public String getLarge ()
    {
        return large;
    }

    public void setLarge (String large)
    {
        this.large = large;
    }

    public String getMedium ()
    {
        return medium;
    }

    public void setMedium (String medium)
    {
        this.medium = medium;
    }

}
