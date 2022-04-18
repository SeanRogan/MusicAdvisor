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
import java.util.Arrays;
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

        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject albumsObj = json.getAsJsonObject("albums");
        JsonArray items = albumsObj.getAsJsonArray("items");
        List<Album> albumList = new ArrayList<>();

        for(int i = 0;i<items.size();i++) {
            JsonObject item = items.get(i).getAsJsonObject();
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
                        ,item.get("href").getAsString()));

            }
            //if only one artist on album
            if(artistsInfo.size() == 1) {
                JsonObject ai = artistsInfo.get(0).getAsJsonObject();
                String artistsName = ai.get("name").getAsString();
                albumList.add(new Album(item.get("name").getAsString()
                        ,artistsName
                        ,item.get("href").getAsString()));
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
                .uri(URI.create(featuredPlaylists))
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
            list.add(new Playlist(jo.get("name").getAsString(), jo.get("href").getAsString()));
        }
        return list;
    }

    public List<Playlist> getPlaylistByCategory(String categoryId) {
        //todo last feature to make. need to make sure categoryId being passed is legit,
        // and that function to check id by name is working, then be sure custom string is working
        List<Playlist> list = new ArrayList<>();
        String responseBody = "";
        //take categoryId argument and insert in uri
        String categorySpecificUri = String.format(playlistByCategory , categoryId);
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
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject items = json.getAsJsonObject("items");
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
