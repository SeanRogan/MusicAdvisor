package advisor;

import java.lang.reflect.Type;
import java.util.List;

public class Album {
    private String title;
    private String link;
    private String artist;
    public Album(String title, String artist, String link) {
        this.title = title;
        this.link = link;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }
    public String getArtists() {return artist;}

}
