package com.sonphan12.vimax.data.model;

public class Folder {
    private String name;
    private int numVideos;

    public Folder(String name, int numVideos) {
        this.name = name;
        this.numVideos = numVideos;
    }

    public Folder() {
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
