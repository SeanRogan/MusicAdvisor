package advisor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Controller {
    private Service service;
    private AuthServer httpAuthServer;
    public static boolean exit = false;
    public static boolean authorized = false;
    private String apiServerPath;
    public Controller(AuthServer httpAuthServer,String apiServerPath) {
        this.httpAuthServer = httpAuthServer;
        this.apiServerPath = apiServerPath;
    }


    public void execute(String[] command) {
        if (command.length < 1) {
            return;
        }

        switch (command[0]) {
            case "auth" : {
                String authRequestBody = httpAuthServer.getAccess();
                if(authRequestBody != null && authRequestBody.contains("access_token")) {
                    authorized = true;
                    service = new Service(new ApiServer(apiServerPath,authRequestBody));
                }
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
        ArrayList<Category> categories= service.getCategories();
        for (Category category : categories) {
            System.out.println(category.getName());
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


    private boolean accessToKenIsValid(String token) {
        return token.contains("access_token");
    }

    public static boolean isExit() {
        return exit;
    }
}
