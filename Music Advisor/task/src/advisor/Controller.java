package advisor;

import java.util.List;
import java.util.Locale;

public class Controller {
    private Service service;
    private ApiServer apiServer;
    private AuthServer httpAuthServer;
    public static boolean exit = false;
    public static boolean authorized = false;
    public final String oAuthLink = "https://accounts.spotify.com/authorize?client_id=b18942eaca6d48d0909ce9e208562bc0&redirect_uri=http://localhost:8080&response_type=code";
    private String apiServerPath;
    public Controller(Service service, AuthServer httpAuthServer,String apiServerPath) {
        this.service = service;
        this.httpAuthServer = httpAuthServer;
        this.apiServerPath = apiServerPath;
    }

    public void featured() {
        System.out.println("---FEATURED---");
        List<Playlist> playlists = service.getFeatured();
        for (Playlist playlist : playlists) {
            System.out.println(playlist.getTitle());
        }
    }

    public void newReleases() {
        System.out.println("---NEW RELEASES---");
        List<Album> albums = service.getNew();
        for (Album album : albums) {
            System.out.println(album.getTitle());
        }
    }

    public void categories() {
        System.out.println("---CATEGORIES---");
        String[] categories = service.getCategories();
        for (String category : categories) {
            System.out.println(category);
        }
    }

    public void playlists(String categoryName) {
        List<Playlist> playlists = service.getPlaylists(categoryName);
        if (playlists != null && !playlists.isEmpty()) {
            System.out.println("---" + categoryName.toUpperCase(Locale.ROOT) + " PLAYLISTS---");
            for (Playlist playlist : playlists) {
                System.out.println(playlist.getTitle());
            }
        }
    }

    public void execute(String[] command) {
        if (command.length < 1) {
            return;
        }

        switch (command[0]) {
            case "auth" : {
                System.out.println(oAuthLink);
                httpAuthServer.getAccess();
                String accessToken = httpAuthServer.getToken();
                if(accessToKenIsValid(accessToken)) {
                authorized = true;
                apiServer = new ApiServer(apiServerPath, accessToken);
                } else System.out.println("Something went wrong with authorization...");
            }

            break;
            case "featured" :
                if(authorized) {
                    featured();
                } else System.out.println("Please, provide access for application.");
                break;
            case "new" :
                if(authorized) {
                    newReleases();
                } else System.out.println("Please, provide access for application.");
                break;
            case "categories" :
                if(authorized) {
                    categories();
                } else System.out.println("Please, provide access for application.");
                break;
            case "playlists" :
                if(authorized) {
                    playlists(command[0]);
                } else System.out.println("Please, provide access for application.");
                break;
            case "exit" : {
                exit = true;
                System.out.println("---GOODBYE!---");
            }
                break;
        }
    }

    private boolean accessToKenIsValid(String token) {
        return token.contains("access_token");
    }

    public static boolean isExit() {
        return exit;
    }
}
