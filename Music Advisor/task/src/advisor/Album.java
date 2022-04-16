package advisor;

import java.util.List;

public class Album {
    private String title;
    private List<Track> tracks;
    private String artist;
    public Album(String title, String artist, List<Track> tracks) {
        this.title = title;
        this.tracks = tracks;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

}
