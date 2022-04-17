package advisor;

import java.util.*;

public class Service {

    ApiServer server;

    public Service(ApiServer server) {
        this.server = server;
    }

    public List<Album> getNew() {
        return server.getNewAlbums();
    }
    public List<Playlist> getFeatured() {
        return server.getFeaturedPlaylists();
    }
    public List<Category> getCategories() {
        return server.getCategories();
    }
    public List<Playlist> getPlaylists(String categoryName) {
        return server.getPlaylistByCategory(categoryName);
    }
}
