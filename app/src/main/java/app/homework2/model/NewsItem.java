package app.homework2.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewsItem implements Serializable {

    private String title;
    private String text;

    //name of image in Drawable folder
    private String imageUrl;
    private Category category;

    private Bitmap image;

    private Date newsDate;
    private int id;


    public NewsItem() {
    }

    public NewsItem(int id, String title, String text, String imageUrl, Bitmap image, Date newsDate, Category category) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.imageUrl = imageUrl;
        this.newsDate = newsDate;
        this.id = id;
        this.category = category;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static List<NewsItem> getAllNews(){
        List<NewsItem> news = new ArrayList<>();
        return news;
    }

}



