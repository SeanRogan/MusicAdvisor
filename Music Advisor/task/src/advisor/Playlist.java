package advisor;

public class Playlist {
    private String title;
    private String link;

    public Playlist(String title, String link) {
        this.title = title;
        this.link = link;
        //this.link = String.format("%s/user/spotify/playlist/%s" , Main.getApiServerPath() ,id);
    }

    public String getTitle() {
        return title;
    }
    public String getLink() {
        return link;
    }
}
