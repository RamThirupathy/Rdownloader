package com.ramkt.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
public class Pins {

    @JsonProperty("id")
    private String id;

    @JsonProperty("height")
    private int height;

    @JsonProperty("current_user_collections")
    private String[] current_user_collections;

    @JsonProperty("color")
    private String color;

    @JsonProperty("urls")
    private Urls urls;

    @JsonProperty("likes")
    private String likes;

    @JsonProperty("width")
    private int width;

    @JsonProperty("created_at")
    private String created_at;

    @JsonProperty("links")
    private Links links;

    @JsonProperty("categories")
    private Categories[] categories;

    @JsonProperty("user")
    private User user;

    @JsonProperty("liked_by_user")
    private String liked_by_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String[] getCurrent_user_collections() {
        return current_user_collections;
    }

    public void setCurrent_user_collections(String[] current_user_collections) {
        this.current_user_collections = current_user_collections;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCategory() {
        String value = "";
        for (Categories categorie : categories) {
            if (value.isEmpty())
                value = categorie.getTitle();
            else
                value = String.format("%s, %s", value, categorie.getTitle());
        }
        return value;
    }

    public String getImageUrl() {
        if (urls != null)
            return urls.getRegular();
        return null;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Categories[] getCategories() {
        return categories;
    }

    public void setCategories(Categories[] categories) {
        this.categories = categories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLiked_by_user() {
        return liked_by_user;
    }

    public void setLiked_by_user(String liked_by_user) {
        this.liked_by_user = liked_by_user;
    }

    public String getPinCount() {
        float value = 0f;
        for (Categories categorie : categories) {
            value = Float.valueOf(categorie.getPhoto_count()) + value;
        }
        return String.format("%d%s", Math.round(value / 1000), "K");
    }
}
