package app.homework2.model;

import java.io.Serializable;

/**
 * Created by atanaltay on 28/03/2017.
 */

public class CommentItem implements Serializable {

    private int id;
    private String name;
    private String message;
    private int news_id;

    public CommentItem() {
    }

    public CommentItem(int id, String name, String message, int news_id) {
        this.name = name;
        this.message = message;
        this.news_id = news_id;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }
}
