package advisor;

import java.util.Arrays;
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
        boolean commandIsValid = false;
        if (command.length < 1) {
            return;
        }
        if(command[0].contains("auth") ||
                command[0].contains("playlists") ||
                command[0].contains("new") ||
                command[0].contains("featured") ||
                command[0].contains("categories")) {
                commandIsValid = true;
        }
        if(commandIsValid){
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
                    //ensure there is a category specified.
                    if(command.length < 2) {
                        System.out.println("Please enter a category you would like to see the playlists for!");
                        break;
                    }
                    playlists(command);

                } else System.out.println("Please, provide access for application.");
                break;
            case "exit" : {
                exit = true;
                System.out.println("---GOODBYE!---");
            }
            break;
        }
        }
        else System.out.println("Please enter one of the following commands: auth, new, featured, categories, or playlists + category name\n");
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

    public void playlists(String[] commands) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < commands.length; i++) {
            sb.append(commands[i]);
            if(i != commands.length-1) sb.append(" ");
        }
        String categoryName = sb.toString();
        String categoryId = "";
        for(Category category : service.getCategories()){
            if(category.getName().equalsIgnoreCase(categoryName.toLowerCase())) {
                categoryId = category.getCategoryId();
                if (!categoryId.isEmpty()) break;
            }
        }
        List<Playlist> playlists = service.getPlaylistByCategory(categoryId);
        if (playlists != null && !playlists.isEmpty()) {
            System.out.println("---" + categoryName.toUpperCase(Locale.ROOT) + " PLAYLISTS---");
            for (Playlist playlist : playlists) {
                System.out.println(playlist.getTitle());
                System.out.println(playlist.getLink() + "\n");
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
