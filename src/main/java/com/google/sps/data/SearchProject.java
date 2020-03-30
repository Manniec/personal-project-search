package com.google.sps.data;

public class SearchProject {

    private String title;
    private String[] tags;
    private String author;
    private String image = null;

    //Default class constructor:

    //Just for good practice:
    private SearchProject(){}

    //Class constructor with parameters:
    public SearchProject(String title, String[] tags, String author, String image){

        this.title = title;
        this.tags = tags;
        this.author = author;
        this.image = image;

    }

    //Getters / Setters:

    public String getTitle(){

        return title;

    }

    public String[] getTags(){

        return tags;

    }

    public String getAuthor(){

        return author;

    }

    public String getImage(){

        return image;

    }

    public void setTitle(String title){

        this.title = title;

    }

    public void setTags(String[] tags){

        this.tags = tags;

    }

    public void setAuthor(String author){

        this.author = author;

    }

    public void setImage(String image){

        this.image = image;

    }

}