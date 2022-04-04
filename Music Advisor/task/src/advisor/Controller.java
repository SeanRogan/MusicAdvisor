package advisor;

import java.util.List;
import java.util.Locale;

public class Controller {
    private Service service;
    public static boolean exit = false;

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
            case "featured" :
                featured();
                break;
            case "new" :
                newReleases();
                break;
            case "categories" :
                categories();
                break;
            case "playlists" :
                playlists(command[1]);
                break;
            case "exit" :
                exit = true;
                System.out.println("---GOODBYE!---");
                break;
            default:

        }
    }

    public static boolean isExit() {
        return exit;
    }
}
