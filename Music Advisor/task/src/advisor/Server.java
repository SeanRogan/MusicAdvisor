package advisor;

public class Server {

    private String serverPath;

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    private String redirectUri = "http://localhost:8080/";
    private String accessCode;

    public Server(String serverPath) {
        this.serverPath = serverPath;
    }

}
