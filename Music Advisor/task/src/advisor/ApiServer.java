package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//todo build methods to process the Json responses into java objects.
public class ApiServer {
    private String featuredReleases = "https://api.spotify.com/v1/browse/featured-playlists";
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
    public String getNewPlaylists() {
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
        return responseBody;
        //todo parse the json into corresponding POJOs (album playlist track, prbly need an artist class?)
        //JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        //JsonObject items = json.getAsJsonObject("items");
    }
    public String getFeaturedPlaylists() {
        String responseBody = "";
        //http client sends request and handles response
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                //access token for authorization
                .header("Authorization", "Bearer " + accessToken)
                //redirect path, where the resources are requested from
                .uri(URI.create(featuredReleases))
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();

        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //todo parse the json into corresponding POJOs (album playlist track, prbly need an artist class?)
        //JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        //JsonObject items = json.getAsJsonObject("items");

return responseBody;
    }
    public String getCategories() {
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
            System.out.println(responseBody);
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //todo parse the json into corresponding POJOs (album playlist track, prbly need an artist class?)
        //JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        //JsonObject items = json.getAsJsonObject("items");

        return responseBody;
    }
    public String getPlaylistByCategory(String categoryName) {
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
        //JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        //JsonObject items = json.getAsJsonObject("items");

        return responseBody;
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
