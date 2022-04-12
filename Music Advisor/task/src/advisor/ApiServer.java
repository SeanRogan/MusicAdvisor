package advisor;

public class ApiServer {
    private String serverPath;
    final private String clientID = "b18942eaca6d48d0909ce9e208562bc0";
    final private String clientSecret = "fdd54982e0b042d8b83696f6f3dc7e96";
    private String redirectUri = "https://api.spotify.com/v1";
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ApiServer(String serverPath, String accessToken) {
        this.serverPath = serverPath;
        this.accessToken = accessToken;
    }
}
