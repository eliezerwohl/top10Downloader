package com.example.eliezerwohl.top10downloader;

/**
 * Created by Elie on 11/12/2016.
 */

public class FeedEntry {
    private String name;
    private String artist;
    private String releaseDate;
    private String imgURL;
    private String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return
                "name=" + name + '\n' +
                ", artist=" + artist + '\n' +
                ", releaseDate=" + releaseDate + '\n' +
                ", imgURL=" + imgURL + '\n';

    }
}
