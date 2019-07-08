package app.homework2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.homework2.R;
import app.homework2.model.Category;
import app.homework2.model.NewsAdapter;
import app.homework2.model.NewsItem;
import app.homework2.service.NewsService;


public class MainActivity extends AppCompatActivity {

    Spinner spCategories;
    ListView lstNews;
    ProgressBar prgBar;
    NewsService newsService = new NewsService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.newsitem);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher_round);
        getSupportActionBar().setTitle(R.string.app_name);
        prgBar = findViewById(R.id.prgbar);
        prgBar.setVisibility(View.INVISIBLE);

        try {
            spCategories= findViewById(R.id.spCategories);

            List<Category> categoriesList = newsService.getAllCategories();
            List<String> catlist = new ArrayList<>();

            for (Category cat : categoriesList) {
                catlist.add(cat.getCategoryName());
            }

            for (String categoryName: catlist) {
                Log.i("Dev","Category Name is   :"+categoryName);
            }
            spCategories= findViewById(R.id.spCategories);
            ArrayAdapter catAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoriesList);

            spCategories.setAdapter(catAdapter);

            //ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,catlist);
            //spCategories.setAdapter(adp);
            spCategories.setSelection(0);

            // Kategori id ye göre haberleri listView içine koyma kısmını onItemClick Listener içine koyuyoruz.
            spCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int catId = (int) spCategories.getSelectedItemId();
                    List<NewsItem> newByCategoryId =  newsService.getAllNewsByCategoryId(catId);

                    try {
                        lstNews = findViewById(R.id.lstNews);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        prgBar.setVisibility(View.VISIBLE);
                        // Eğer combo nun çalışmasını istiyorsak, brada yeni bir list<NewsItem> yaparız, switch case ile adaptöre ilgili kategorileri atarız.

                        NewsAdapter adpt = new NewsAdapter(MainActivity.this, newByCategoryId);
                        lstNews.setAdapter(adpt);
                    }catch (Exception ex){
                        Toast.makeText(MainActivity.this, "On setOnClickListener for Category Spinner.", Toast.LENGTH_SHORT).show();
                        Log.e("Dev","On setOnClickListener for Category Spinner. Details are    :" + ex.getMessage());
                    }

                }
            });

        }
        catch (Exception ex){

            Log.e("Dev","Error onCreate while populating spinner    :" + ex.getMessage());
        }

        try {
            lstNews = findViewById(R.id.lstNews);

           List<NewsItem>  allNews =  newsService.getAllNews();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            prgBar.setVisibility(View.VISIBLE);
            NewsAdapter adpt = new NewsAdapter(this, allNews);
            lstNews.setAdapter(adpt);
        }catch (Exception ex){
            Toast.makeText(MainActivity.this, "Couldn not set PlanetAdapter to planet list", Toast.LENGTH_SHORT).show();
            Log.e("Dev","Error onCreate while getting news. Details are    :" + ex.getMessage());
        }

        try {
            lstNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent i = new Intent(MainActivity.this, NewsDetailActivity.class);
                        NewsItem news = newsService.getNewsById((int) id);

                        i.putExtra("news",news);
                        startActivity(i);
                    }
                    catch (Exception ex){
                        Toast.makeText(MainActivity.this, "We cant get this News now... ", Toast.LENGTH_SHORT).show();
                        Log.e("Dev","On ListView onItemClickListener implementation.");
                    }

                }
            });

        }
        catch (Exception ex){
            Log.e("Dev","On ListView onItemClickListener implementation.");
        }
    }


    ActionMode mActionMode;

    ActionMode.Callback myCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {


            getMenuInflater().inflate(R.menu.mainmenuitem,menu);
            getActionBar().setDisplayHomeAsUpEnabled(true);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()){

                case  R.id.home:
                    Toast.makeText(MainActivity.this,"Home clicked",Toast.LENGTH_SHORT).show();

                    mActionMode.finish();

                    break;

            }

            return true;

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenuitem,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.mnhome:
                Toast.makeText(this,"Home clicked",Toast.LENGTH_SHORT).show();
                break;

        }

        return true;

    }
}
