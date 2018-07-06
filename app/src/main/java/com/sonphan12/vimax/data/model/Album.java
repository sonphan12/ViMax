package com.sonphan12.vimax.data.model;

public class Album {
    private String name;
    private int numVideos;
    private boolean isChecked;

    public Album(String name, int numVideos) {
        this.name = name;
        this.numVideos = numVideos;
        this.isChecked = false;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
