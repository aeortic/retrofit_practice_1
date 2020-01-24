package com.example.retrofit_practice_1.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MovieDatabase {
    @SerializedName("actors")
    private JsonElement actors;
    @SerializedName("movies")
    private JsonElement movies;

    public MovieDatabase(JsonElement actors, JsonElement movies) {
        this.setActors(actors);
        this.setMovies(movies);
    }

    public HashMap<String, Integer> getActors() {
        HashMap<String, Integer> actorMap = new HashMap<String, Integer>();

        for (Map.Entry<String, JsonElement> actor: actors.getAsJsonObject().entrySet()) {
            Integer actorId = Integer.parseInt(actor.getKey());
            String actorName = actor.getValue().getAsString();

            actorMap.put(actorName, actorId);
        }

        return actorMap;
    }

    public void setActors(JsonElement actors) {

        this.actors = actors;
    }

    class Movie {
        private String title;
        private List<Integer> actors;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setActors(List<Integer> actors) {
            this.actors = actors;
        }
    }

    public HashMap<Integer, Movie> getMovies() {
        HashMap<Integer, Movie> movieMap = new HashMap<Integer, Movie>();

        for (Map.Entry<String, JsonElement> movieElement: movies.getAsJsonObject().entrySet()) {

            Integer movieId = Integer.parseInt(movieElement.getKey());

            JsonObject movieObject = movieElement.getValue().getAsJsonObject();
            JsonArray actorsJsonArray = movieObject.get("actors").getAsJsonArray();
            List<Integer> actorList = new ArrayList<Integer>();
            for (JsonElement actorJsonId: actorsJsonArray) {
                actorList.add(actorJsonId.getAsInt());
            }
            String title = movieObject.get("title").getAsString();
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setActors(actorList);

            movieMap.put(movieId, movie);
        }

        return movieMap;
    }

    public List<String> getMoviesByActorId(Integer actorId) {
        List<String> filmCareer = new ArrayList<String>();

        HashMap<Integer, Movie> movieMap = getMovies();

        Iterator movieIterator = movieMap.entrySet().iterator();

        while(movieIterator.hasNext()) {
            Map.Entry movieElement = (Map.Entry)movieIterator.next();
            Movie movie = (Movie)movieElement.getValue();
            for (Integer actorInMovie: movie.actors) {
                if (actorInMovie.equals(actorId)) {
                    filmCareer.add(movie.title);
                }
            }
        }



        return filmCareer;
    }

    public void setMovies(JsonElement movies) {
        this.movies = movies;
    }
}