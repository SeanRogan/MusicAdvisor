package advisor;

import java.util.List;
import java.util.Locale;

public class Controller {
    private Service service;
    public static boolean exit = false;
    public static boolean authorized = false;
    public final String oAuthLink = "https://accounts.spotify.com/authorize?client_id=b18942eaca6d48d0909ce9e208562bc0&redirect_uri=https://localhost:8080&response_type=code";

    public Controller(Service service) {
        this.service = service;
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
                System.out.println("---SUCCESS---");
                authorized = true;
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
            default:

        }
    }

    public static boolean isExit() {
        return exit;
    }
}
