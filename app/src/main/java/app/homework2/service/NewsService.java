package app.homework2.service;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import app.homework2.R;
import app.homework2.model.Category;
import app.homework2.model.CommentItem;
import app.homework2.model.NewsItem;

public class NewsService implements Serializable {

    public List<Category> categories =null;
    public List<NewsItem> allNews = null;
    public NewsItem news = null;
    public List<NewsItem> allNewsByCategory = null;
    public List<CommentItem> allCommentsByNewsId = null;

    public String commentsSuccessMessage;
    public Resources res = Resources.getSystem();

    public List<Category> getAllCategories(){

        try {
            GetAllCategoriesTask tsk = new GetAllCategoriesTask();
            tsk.execute(res.getString(R.string.getAllNewsByIdUrl));
            return categories;
        }
        catch(Exception ex){
            Log.e("Dev","on getAllCategories :" + ex.getMessage());
        }
            return null;
    }

    public List<NewsItem> getAllNews(){

        try {
            GetAllNewsTask tsk = new GetAllNewsTask();

            tsk.execute(res.getString(R.string.getAllNewsUrl));
            //tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
            return allNews;

        }
        catch(Exception ex){
            Log.e("Dev","on getAllNews:" + ex.getMessage());
        }

            return null;
    }


    public NewsItem getNewsById(int id){
        JSONObject obj = new JSONObject();

        try {
            obj.put("id",id);
            GetNewsByIdTask tsk = new GetNewsByIdTask();
            tsk.execute(res.getString(R.string.getAllNewsByIdUrl), obj.toString());
            return news;

        }
        catch(Exception ex){
            Log.e("Dev","on getNewsById   :" + ex.getMessage());
        }
        return null;

    }

    public List<NewsItem> getAllNewsByCategoryId(int id){

        JSONObject obj = new JSONObject();
        try {
            obj.put("id",id);
            GetNewsByCategoryIdTask tsk = new GetNewsByCategoryIdTask();

            tsk.execute(res.getString(R.string.getAllNewsByCategoryIdUrl));
            tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid",obj.toString());
            return allNewsByCategory;

        } catch (JSONException ex) {
            ex.printStackTrace();
            Log.e("Dev","on getAllCategories Method details are     :" + ex.getMessage());
        }
            return null;
    }

    public List<CommentItem> getCommentsByNewsId(int id){

        JSONObject obj = new JSONObject();
        try {
            obj.put("news_id",id);
            GetAllCommentsByNewsIdTask tsk = new GetAllCommentsByNewsIdTask();

            //Log.i("Dev","GetNewsByCategoryId url is     :" +res.getString(R.string.GetAllNewsByCategoryIdUri));
            //tsk.execute(res.getString(R.string.GetAllCommentsByNewsIdUri));
            tsk.execute("\"http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid",obj.toString());

            return allCommentsByNewsId;

        } catch (JSONException ex) {
            ex.printStackTrace();
            Log.e("Dev","on getAllCategories Method details are     :" + ex.getMessage());
        }
            return null;


    }

    public String PostComment(CommentItem commentItem){

        JSONObject obj = new JSONObject();

        try {
            obj.put("news_id",commentItem.getNews_id());
            obj.put("name",commentItem.getName());
            obj.put("text",commentItem.getMessage());

            PostCommentTask tsk = new PostCommentTask();
            //tsk.execute(res.getString(R.string.PostCommentsUri));
            tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",obj.toString());
            return commentsSuccessMessage;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "UnSuccessfull";
    }

    class GetAllCategoriesTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String urlS = params[0];
            Log.i("Dev","Entered GetAtllCategories  DoBackInground method");

            try {
                URL url = new URL(urlS);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                StringBuilder builder = new StringBuilder();

                String line = "";
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                conn.disconnect();
                return builder.toString();

            } catch (MalformedURLException e) {
                Log.e("Dev","MalformedUrlException geatallcategory DoBackInground");

                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Dev","IOException geatallcategory DoBackInground");

                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.i("Dev","Entered GetAtllCategories  onPostExecute method");

                JSONObject obj = new JSONObject(s);
                Log.i("Dev","JsonArray result in GetAllCategory onPostmethod"+obj);

                //JSONArray js = new JSONArray(obj);
                //js.getJSONArray(1);
                JSONArray result = obj.getJSONArray("items");
                Log.i("Dev","JsonArray result in GetAllCategory onPostmethod"+result);

                for (int i = 0; i < result.length(); i++)
                {
                    //JSONObject js = result.getJSONObject(i);
                    //String name = js.getString("name");
                    //int id = js.getInt("id");

                    Category catitem = new Category();

                    //catitem.setCategoryName(name);
                    //catitem.setCategoryId(id);

                    int catId = Integer.parseInt(result.getJSONArray(i).getString(1));
                    Log.i("Dev","Getting CategoryItem is Successfull. Category ID is "+catId);
                    String catName = result.getJSONArray(i).getString(2);
                    Log.i("Dev","Getting CategoryItem is Successfull. Category name is "+catName);

                    catitem.setCategoryId(catId);
                    catitem.setCategoryName(catName);
                    //category.add(result.getString(i));
                    categories.add(catitem);
                }


            } catch (JSONException e) {
                Log.e("Dev","Get Category onPostExecute");
                e.printStackTrace();
            }

        }
    }

    class GetAllNewsTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String urlS = params[0];
            try {
                URL url = new URL(urlS);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                StringBuilder builder = new StringBuilder();

                String line = "";
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                conn.disconnect();
                return builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Dev","IOException GetAllNews DoBackInground");

                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject obj = new JSONObject(s);
                JSONArray result = obj.getJSONArray("items");



                for (int i = 0; i < result.length(); i++)
                {
                    NewsItem newsitem = new NewsItem();
                    int newsId = Integer.parseInt(result.getJSONArray(i).getString(1));
                    Log.i("Values",i+".News's id : "+newsId);
                    String newsTitle = result.getJSONArray(i).getString(2);
                    Log.i("Values",i+".News's title : "+newsTitle);
                    String newsText = result.getJSONArray(i).getString(3);
                    Log.i("Values",i+".News's Text : "+newsTitle);
                    String date = result.getJSONArray(i).getString(4);
                    Log.i("Values",i+".News's Date : "+date);
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// olursa daha sonra deneriz.
                    //Date date = (Date) sdf.parse(DateFromDb);
                    String imageUrl = result.getJSONArray(i).getString(5);
                    Log.i("Values",i+".News's imageUrl : "+imageUrl);
                    String categoryName = result.getJSONArray(i).getString(6);
                    Log.i("Values",i+".News's category name : "+categoryName);
                    Category cat  = new Category();
                    cat.setCategoryName(categoryName);

                    newsitem.setId(newsId);
                    newsitem.setTitle(newsTitle);
                    newsitem.setText(newsText);
                    newsitem.setNewsDate(Calendar.getInstance().getTime()); // şimdilik böyle kalsın.
                    newsitem.setImageUrl(imageUrl);

                    newsitem.setCategory(cat);
                    allNews.add(newsitem);
                }


            } catch (JSONException e) {
                Log.e("Dev","Error in  GetAllNews method in NewsService onPostExecute Part");
                e.printStackTrace();
            }

        }
    }

    class GetNewsByIdTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String urlS = params[0];
            String data = params[1];
            try {
                URL url = new URL(urlS);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type","application/JSON");
                BufferedWriter writer = new
                        BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder builder = new StringBuilder();

                String line = "";
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                conn.disconnect();
                return builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject obj = new JSONObject(s);

                Category newsCategory = new Category();
                newsCategory.setCategoryName(obj.getString("categoryName"));

                NewsItem newItem = new NewsItem();

                newItem.setId(Integer.parseInt(String.valueOf(obj.getInt("id"))));
                newItem.setTitle(obj.getString("title"));
                newItem.setText(obj.getString("text"));
                newItem.setNewsDate(Calendar.getInstance().getTime()); // Şimdilik böyle kalsın.
                newItem.setImageUrl(obj.getString("image"));
                newItem.setCategory(newsCategory);

                news = newItem;

            } catch (JSONException e) {
                Log.e("Dev","Error occured Get GetNewsById onPostExecute");
                e.printStackTrace();
            }

        }
    }

    class GetNewsByCategoryIdTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String urlS = params[0];
            String data = params[1];
            try {
                URL url = new URL(urlS);

                HttpURLConnection cnn = new HttpURLConnection(url) {
                    @Override
                    public void disconnect() {

                    }

                    @Override
                    public boolean usingProxy() {
                        return false;
                    }

                    @Override
                    public void connect() throws IOException {

                    }
                };
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type","application/JSON");
                BufferedWriter writer = new
                        BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder builder = new StringBuilder();

                String line = "";
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                conn.disconnect();
                return builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject obj = new JSONObject(s);
                JSONArray result = obj.getJSONArray("items");

                for (int i = 0; i < result.length(); i++){

                    allNews = null;

                    NewsItem newsitem = new NewsItem();
                    int newsId = Integer.parseInt(result.getJSONArray(i).getString(1));

                    String newsTitle = result.getJSONArray(i).getString(2);

                    String newsText = result.getJSONArray(i).getString(3);

                    String date = result.getJSONArray(i).getString(4);

                    String imageUrl = result.getJSONArray(i).getString(5);

                    String categoryName = result.getJSONArray(i).getString(6);

                    Category cat  = new Category();
                    cat.setCategoryName(categoryName);

                    newsitem.setId(newsId);
                    newsitem.setTitle(newsTitle);
                    newsitem.setText(newsText);
                    newsitem.setNewsDate(Calendar.getInstance().getTime());
                    newsitem.setImageUrl(imageUrl);

                    newsitem.setCategory(cat);
                    allNews.add(newsitem);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class GetAllCommentsByNewsIdTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String urlS = params[0];


            try {
                URL url = new URL(urlS);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                StringBuilder builder = new StringBuilder();

                String line = "";
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                conn.disconnect();
                return builder.toString();

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.i("Dev","Entered GetAtllCategories  onPostExecute method");

                JSONObject obj = new JSONObject(s);
                Log.i("Dev","JsonArray result in GetAllCategory onPostmethod"+obj);

                JSONArray result = obj.getJSONArray("items");
                Log.i("Dev","JsonArray result in GetAllCategory onPostmethod"+result);

                for (int i = 0; i < result.length(); i++)
                {

                    CommentItem commentItem = new CommentItem();
                    int commentId = Integer.parseInt(result.getJSONArray(i).getString(1));
                    int newsId = Integer.parseInt(result.getJSONArray(i).getString(2));
                    String text = result.getJSONArray(i).getString(3);
                    Log.i("Dev","Getting CategoryItem is Successfull. Category ID is "+text);
                    String name = result.getJSONArray(i).getString(4);
                    Log.i("Dev","Getting CategoryItem is Successfull. Category name is "+name);

                    commentItem.setId(commentId);
                    commentItem.setName(name);
                    commentItem.setMessage(text);
                    commentItem.setNews_id(newsId);
                    allCommentsByNewsId.add(commentItem);

                }


            } catch (JSONException e) {
                Log.e("Dev","Get Category onPostExecute");
                e.printStackTrace();
            }

        }
    }

    class PostCommentTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String urlS = params[0];
            String data = params[1];
            try {
                URL url = new URL(urlS);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type","application/JSON");
                BufferedWriter writer = new
                        BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder builder = new StringBuilder();

                String line = "";
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                conn.disconnect();
                return builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject obj = new JSONObject(s);
                String result= obj.getString("serviceMessageText");
                commentsSuccessMessage = result;
            } catch (JSONException e) {
                e.printStackTrace();
                commentsSuccessMessage = "Error has occured while posting your command";
            }

        }
    }
}
