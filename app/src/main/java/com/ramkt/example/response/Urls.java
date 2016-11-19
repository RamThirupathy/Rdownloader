package com.ramkt.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
public class Urls
{
    @JsonProperty("raw")
    private String raw;

    @JsonProperty("regular")
    private String regular;

    @JsonProperty("full")
    private String full;

    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("small")
    private String small;

    public String getRaw ()
    {
        return raw;
    }

    public void setRaw (String raw)
    {
        this.raw = raw;
    }

    public String getRegular ()
    {
        return regular;
    }

    public void setRegular (String regular)
    {
        this.regular = regular;
    }

    public String getFull ()
    {
        return full;
    }

    public void setFull (String full)
    {
        this.full = full;
    }

    public String getThumb ()
    {
        return thumb;
    }

    public void setThumb (String thumb)
    {
        this.thumb = thumb;
    }

    public String getSmall ()
    {
        return small;
    }

    public void setSmall (String small)
    {
        this.small = small;
    }
}
