package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AuthServer {
    final private String clientID = "b18942eaca6d48d0909ce9e208562bc0";
    final private String clientSecret = "fdd54982e0b042d8b83696f6f3dc7e96";
    final private String redirectUri = "http://localhost:8080";
    private String accessToken;
    private String serverPath;
    private String authCode = "";

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public AuthServer(String serverPath) {
        this.serverPath = serverPath;

    }

    public void getAccess(){
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/",
                    (HttpExchange exchange) -> {
                        String query = exchange.getRequestURI().getQuery();
                        String request;
                        if(query != null && query.contains("code")) {
                            setAuthCode(query.substring(5));
                            System.out.println("Access code received");
                            request = "Got the code. Return back to your program.";
                        } else {
                            request = "Authorization code not found. Try again.";
                        }
                        exchange.sendResponseHeaders(200, request.length());
                        exchange.getResponseBody().write(request.getBytes());
                        exchange.getResponseBody().close();
                    });
            server.start();
            System.out.println("Waiting for code...");
            while(authCode.equals("")) {
                Thread.sleep(100);
            }
            server.stop(5);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getToken() {
        System.out.println("making http request for access_token...\n" +
                "response:");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Accept" , "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(serverPath + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + authCode
                                + "&client_id=" + clientID
                                + "&client_secret=" + clientSecret
                                + "&redirect_uri=" + redirectUri))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            accessToken = response.body();
            System.out.println(response.body());
            System.out.println("---SUCCESS---");
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

