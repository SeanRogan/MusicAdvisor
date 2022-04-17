package advisor;

public class Category {
    private String name;
    private String categoryId;
    private String href;

    public String getHref() {
        return href;
    }

    Category(String name, String id, String href) {
        this.name = name;
        this.categoryId = id;
        this.href = href;
    }



    public String getCategoryId() {
        return categoryId;
    }
    public String getName() {
        return name;
    }

}
