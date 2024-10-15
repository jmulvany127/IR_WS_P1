package model;

public class CranDoc {
    private String id;
    private String title;
    private String author;
    private String bibliography;
    private String words;

    // Getters and setters for each field
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
