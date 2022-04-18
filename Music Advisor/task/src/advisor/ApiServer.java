package advisor;

import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ApiServer {
    final private String clientID = "b18942eaca6d48d0909ce9e208562bc0";
    final private String clientSecret = "fdd54982e0b042d8b83696f6f3dc7e96";
    final private String redirectUri = "https://api.spotify.com/v1";
    private String featuredPlaylists = "/v1/browse/featured-playlists";
    private String newReleases = "/v1/browse/new-releases";
    private String playlistByCategory = "/v1/browse/categories/%s/playlists";
    private String allCategories = "/v1/browse/categories";
    private String serverPath = "https://api.spotify.com";
    private String accessToken;

    public ApiServer(String serverPath, String authRequestBody) {
        this.serverPath = serverPath;
        accessToken = parseAccessToken(authRequestBody);
    }

    public List<Album> getNewAlbums() {

        String responseBody = "";
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(serverPath + newReleases))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        //get albums as json object from json response
        JsonObject albumsObj = json.getAsJsonObject("albums");
        //get json array of album json object
        JsonArray items = albumsObj.getAsJsonArray("items");
        //instantiate list to hold album java objects
        List<Album> albumList = new ArrayList<>();
        //loop through array of albums
        for(int i = 0;i<items.size();i++) {
            //get first array element as jsonObj
            JsonObject item = items.get(i).getAsJsonObject();
            JsonObject extUrls = item.get("external_urls").getAsJsonObject();
            //get external url
            String url = extUrls.get("spotify").getAsString();
            //get album id
            String id = item.get("id").getAsString();
            //get json array of artists
            JsonArray artistsInfo = item.get("artists").getAsJsonArray();
            //if multiple artists on album
            if(artistsInfo.size() > 1) {
                //create array of just the name strings from the artist infos
                String[] artistsNames = new String[artistsInfo.size()];
                for(int j = 0;j < artistsInfo.size(); j++) {
                    JsonObject ai = artistsInfo.get(j).getAsJsonObject();
                    artistsNames[j] = ai.get("name").getAsString();
                }
                String stringOfArtists = Arrays.toString(artistsNames);
                albumList.add(new Album(item.get("name").getAsString()
                        ,stringOfArtists
                        ,url
                        ,id));

            }
            //if only one artist on album
            if(artistsInfo.size() == 1) {
                JsonObject ai = artistsInfo.get(0).getAsJsonObject();
                String [] artistsNames = new String[] {ai.get("name").getAsString()};

                albumList.add(new Album(item.get("name").getAsString()
                        ,Arrays.toString(artistsNames)
                        ,url
                        ,id));
            }
        }
        return albumList;
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String responseBody = "";
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(serverPath + allCategories))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject cat = json.get("categories").getAsJsonObject();
        JsonArray items = cat.get("items").getAsJsonArray();
        for(int i = 0; i<items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            categories.add(new Category(item.get("name").getAsString(), item.get("id").getAsString(), item.get("href").getAsString()));
        }
        return categories;
    }

    public List<Playlist> getFeaturedPlaylists() {
        List<Playlist> list = new ArrayList<>();
        String responseBody = "";
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(serverPath + featuredPlaylists))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();

        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject playlistsObj = json.get("playlists").getAsJsonObject();
        JsonArray playlistArray = playlistsObj.getAsJsonArray("items");
        for(int i = 0; i < playlistArray.size(); i++) {
            JsonObject jo = playlistArray.get(i).getAsJsonObject();
            JsonObject extUrls = jo.getAsJsonObject("external_urls");
            String url = extUrls.get("spotify").getAsString();
            list.add(new Playlist(jo.get("name").getAsString(), url));
        }
        return list;
    }

    public List<Playlist> getPlaylistByCategory(String categoryId) {
        List<Playlist> list = new ArrayList<>();
        String responseBody = "";
        //take categoryId argument and insert in uri
        String genericCatURI = serverPath + playlistByCategory;
        String categorySpecificUri = String.format(genericCatURI , categoryId);
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(categorySpecificUri))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();

        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if(checkForErrorCode(responseBody)) {
            return null;
        }
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject playlists = json.getAsJsonObject("playlists");
        JsonArray items = playlists.getAsJsonArray("items");
        for(int i = 0; i < items.size(); i++) {
            JsonObject jo = items.get(i).getAsJsonObject();
            JsonObject extUrls = jo.get("external_urls").getAsJsonObject();
            String url = extUrls.get("spotify").getAsString();
            list.add(new Playlist(jo.get("name").getAsString(), url));
        }
        return list;
    }

    private boolean checkForErrorCode(String responseBody) {
        JsonObject error;
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        try {
            error = json.getAsJsonObject("error");

            if(error != null && error.get("message").getAsString().contains("id doesn't exist")) {
                System.out.println("Unknown category name.");
                return true;
            }
            if (error != null && (!error.get("message").getAsString().contains("id doesn't exist"))) {
                System.out.println(error.get("message").getAsString());
                System.out.println("Status code: " + error.get("status").getAsString());
                return true;
            }} catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private String parseAccessToken(String authRequestBody) {
        JsonObject jObj = JsonParser.parseString(authRequestBody).getAsJsonObject();
        return jObj.get("access_token").getAsString();
    }


}
