package com.example.retrofit_practice_1.model;


import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class MovieDatabase {
    @SerializedName("actors")
    private HashMap<String, Integer> actors;
    @SerializedName("movies")
    private JsonElement movies;

    public MovieDatabase(JsonElement actors, JsonElement movies) {
        this.setActors(actors);
        this.movies = movies;
    }

    public HashMap<String, Integer> getActors() {
        return actors;
    }

    public void setActors(JsonElement actors) {

        HashMap<String, Integer> actorMap = new HashMap<String, Integer>();

        for (Map.Entry<String, JsonElement> actor: actors.getAsJsonObject().entrySet()) {
            Integer actorId = Integer.parseInt(actor.getKey());
            String actorName = actor.getValue().getAsString();

            actorMap.put(actorName, actorId);
        }

        this.actors = actorMap;
    }

    public JsonElement getMovies() {
        return movies;
    }

    public void setMovies(JsonElement movies) {
        this.movies = movies;
    }
}