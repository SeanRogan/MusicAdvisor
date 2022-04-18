package advisor;

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
            System.out.println(playlist.getLink() + "\n");
        }
    }

    public void newReleases() {
        System.out.println("---NEW RELEASES---");
        List<Album> albums = service.getNew();
        for (Album album : albums) {
            System.out.println(album.getTitle() + "\n" + album.getArtists() + "\n" + album.getLink() + "\n");
        }
    }

    public void categories() {
        System.out.println("---CATEGORIES---");
        for (Category category : service.getCategories()) {
            System.out.println(category.getName());
        }
    }

    public void playlists(String categoryName) {
        String categoryId = "";
        for(Category category : service.getCategories()){
            if(category.getName().equals(categoryName)) {
                categoryId = category.getCategoryId();
            }
        }
        //need someway to use category name to get category ID
        List<Playlist> playlists = service.getPlaylistByCategory(categoryId);
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
