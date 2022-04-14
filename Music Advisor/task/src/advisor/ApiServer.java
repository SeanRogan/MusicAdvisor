package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiServer {
    final private String clientID = "b18942eaca6d48d0909ce9e208562bc0";
    final private String clientSecret = "fdd54982e0b042d8b83696f6f3dc7e96";
    final private String redirectUri = "https://api.spotify.com/v1";
    private String serverPath = "https://api.spotify.com";
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
