package advisor;

import java.util.*;

public class Service {

    ApiServer server;

    public Service(ApiServer server) {
        this.server = server;
    }

    private List<Album> newAlbums= null;

    private List<Playlist> featured;

    private List<Category> categories;


    public List<Album> getNew() {
        newAlbums = server.getNewAlbums();
        return newAlbums;
    }
    public List<Playlist> getFeatured() {
        server.getFeaturedPlaylists();
        return featured;
    }
    public List<Category> getCategories() {
        categories = server.getCategories();
        return categories;
    }
    public List<Playlist> getPlaylists(String categoryName) {
        List<Playlist> list= server.getPlaylistByCategory(categoryName);

        return list;
    }
}
