package advisor;

import java.util.*;

public class Service {
    ApiServer server;
    public Service(ApiServer server) {
        this.server = server;
    }

    private final List<Album> newAlbums;
    {
        newAlbums = new LinkedList<>();

        newAlbums.add(new Album("Mountains [Sia, Diplo, Labrinth]"));
        newAlbums.add(new Album("Runaway [Lil Peep]"));
        newAlbums.add(new Album("The Greatest Show [Panic! At The Disco]"));
        newAlbums.add(new Album("All Out Life [Slipknot]"));
    }
    private final List<Playlist> featured;
    {
        featured = new LinkedList<>();

        featured.add(new Playlist("Mellow Morning"));
        featured.add(new Playlist("Wake Up and Smell the Coffee"));
        featured.add(new Playlist("Monday Motivation"));
        featured.add(new Playlist("Songs to Sing in the Shower"));
    }
    private final Map<String, Set<Playlist>> categories;
    {
        categories = new HashMap<>();

        Set<Playlist> p = new HashSet<>();
        p.add(new Playlist("Walk Like A Badass  "));
        p.add(new Playlist("Rage Beats"));
        p.add(new Playlist("Arab Mood Booster"));
        p.add(new Playlist("Sunday Stroll"));

        categories.put("Top Lists", p);
        categories.put("Pop", p);
        categories.put("Mood", p);
        categories.put("Latin", p);
    }

    public List<Album> getNew() {
        server.getNewPlaylists();
        return newAlbums;
    }
    public List<Playlist> getFeatured() {
        server.getFeaturedPlaylists();
        return featured;
    }
    public String[] getCategories() {
        server.getCategories();
        return categories.keySet().toArray(String[]::new);
    }
    public List<Playlist> getPlaylists(String categoryName) {
        server.getPlaylistByCategory(categoryName);
        Set<Playlist> category = categories.get(categoryName);
        if (category != null) {
            return new LinkedList<>(category);
        }
        return null;
    }
}
