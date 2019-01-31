package com.brand.Kratos.model;

public class MenuModel2 {
    String musicName,musicArtist;
    Integer movieName;

    public MenuModel2(String musicName, String musicArtist, Integer movieName) {
        this.musicName = musicName;
        this.musicArtist = musicArtist;
        this.movieName = movieName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public Integer getMovieName() {
        return movieName;
    }

    public void setMovieName(Integer movieName) {
        this.movieName = movieName;
    }
}
