package advisor;

import java.util.List;

public class Album {
    private String title;
    private List<Track> tracks;

    public Album(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
