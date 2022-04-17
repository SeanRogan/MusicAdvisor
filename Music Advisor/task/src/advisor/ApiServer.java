package advisor;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;


public class ApiServer {
    private String featuredPlaylists = "https://api.spotify.com/v1/browse/featured-playlists";
    private String newReleases = "https://api.spotify.com/v1/browse/new-releases";
    private String playlistByCategory = "https://api.spotify.com/v1/browse/categories/%s/playlists";
    private String allCategories = "https://api.spotify.com/v1/browse/categories";
    final private String clientID = "b18942eaca6d48d0909ce9e208562bc0";
    final private String clientSecret = "fdd54982e0b042d8b83696f6f3dc7e96";
    final private String redirectUri = "https://api.spotify.com/v1";
    private String serverPath = "https://api.spotify.com";
    private String accessToken;
    /*
        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    */
    public List<Album> getNewAlbums() {
        List<Album> albumArrayList = null;
        String responseBody = "";
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(newReleases))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //todo parse the json into corresponding POJOs (album playlist track, prbly need an artist class?)
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray albumArray = json.getAsJsonArray("album");
        List<JsonElement> albumList = new ArrayList<>();
        for(int i = 0;i<albumArray.size();i++) {
            albumList.add(albumArray.get(i));
        }
        Gson gson = new Gson();

        //JsonObject items = json.getAsJsonObject("items");
        return albumArrayList;
    }

    public String getFeaturedPlaylists() {
        String responseBody = "";
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(featuredPlaylists))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();

        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        gson.fromJson(responseBody,Category.class);
        //todo parse the json into corresponding POJOs (album playlist track, prbly need an artist class?)
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray featuredPlaylistArray = json.getAsJsonArray("albums");
        ArrayList<JsonElement> featuredPlaylistList = new ArrayList<>();
        for(int i = 0;i<featuredPlaylistArray.size();i++) {
            featuredPlaylistList.add(featuredPlaylistArray.get(i));
        }



return responseBody;
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
                .uri(URI.create(allCategories))
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
        //ArrayList<Object> categoriesArray = new ArrayList<>();
        for(int i = 0; i<items.size(); i++) {
        JsonObject item = items.get(i).getAsJsonObject();
        categories.add(new Category(item.get("name").getAsString(), item.get("id").getAsString(), item.get("href").getAsString()));
    }
        return categories;
    }
    public List<Playlist> getPlaylistByCategory(String categoryName) {
        List<Playlist> list = new ArrayList<>();
        String responseBody = "";
        //take categoryName argument and insert in uri
        String categorySpecificUri = String.format(playlistByCategory , categoryName);
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
            System.out.println(responseBody);
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //todo parse the json into corresponding POJOs (album playlist track, prbly need an artist class?)
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        json.getAsJsonObject("");
        return list;
    }

    public ApiServer(String serverPath, String authRequestBody) {
        this.serverPath = serverPath;
        accessToken = parseAccessToken(authRequestBody);
    }

    private String parseAccessToken(String authRequestBody) {
        JsonObject jObj = JsonParser.parseString(authRequestBody).getAsJsonObject();
        return jObj.get("access_token").getAsString();
    }


}
