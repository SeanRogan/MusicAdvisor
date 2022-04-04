package advisor;

import java.util.List;

public class Playlist {
    private String title;
    private List<Track> tracks;

    public Playlist(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
