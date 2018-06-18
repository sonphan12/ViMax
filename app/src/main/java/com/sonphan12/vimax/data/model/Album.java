package com.sonphan12.vimax.data.model;

public class Album {
    private String name;
    private int numVideos;

    public Album(String name, int numVideos) {
        this.name = name;
        this.numVideos = numVideos;
    }


    public Album() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumVideos() {
        return numVideos;
    }

    public void setNumVideos(int numVideos) {
        this.numVideos = numVideos;
    }
}
