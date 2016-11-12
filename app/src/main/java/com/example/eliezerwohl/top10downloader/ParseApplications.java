package com.example.eliezerwohl.top10downloader;

import java.util.ArrayList;

/**
 * Created by Elie on 11/12/2016.
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }
}
