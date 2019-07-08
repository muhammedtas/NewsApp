package app.homework2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import app.homework2.R;
import app.homework2.model.CommentItem;
import app.homework2.model.CommentsAdapter;
import app.homework2.model.NewsItem;
import app.homework2.service.NewsService;

public class NewsDetailActivity extends AppCompatActivity {

    NewsItem news = null;
    NewsService newsService = new NewsService();

    ImageView img;
    TextView txtdata;
    TextView txtcat;
    List<CommentItem> comments = null;
    MenuItem menuItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        try {
            //getSupportActionBar().setTitle(news.getTitle());
            getSupportActionBar().setTitle(R.string.newsdetail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }catch (Exception ex){
            Toast.makeText(NewsDetailActivity.this, "Could not set Action bar", Toast.LENGTH_SHORT).show();
        }

        try {
            this.news = (NewsItem)getIntent().getExtras().getSerializable("news");
        }catch (Exception ex){
            Toast.makeText(NewsDetailActivity.this, "Could'not get the News Details", Toast.LENGTH_SHORT).show();
        }
        try {
            img = findViewById(R.id.imgBigNews);
            txtdata = findViewById(R.id.txtNewsDetailText);
            txtcat = findViewById(R.id.txtNewsDetailTitle);
            menuItem = findViewById(R.id.mncomments);

            //img.setImageResource(Integer.parseInt(news.getImageUrl()));
            img.setImageBitmap(news.getImage());

            txtdata.setText(news.getText());
            txtcat.setText(news.getTitle());

            try {

                comments = newsService.getCommentsByNewsId(news.getId());
            }
            catch (Exception ex){
                Toast.makeText(NewsDetailActivity.this, "Error occured while getting comments on this new's detail activity", Toast.LENGTH_SHORT).show();
            }

            CommentsAdapter adp = new CommentsAdapter(this, comments);


        }catch(Exception ex){
            Toast.makeText(NewsDetailActivity.this, "Could not set some parts in Detailed news Page", Toast.LENGTH_SHORT).show();
        }

    }


    ActionMode mActionMode;

    ActionMode.Callback myCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {


            getMenuInflater().inflate(R.menu.detailsmenuitem,menu);

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
                    Toast.makeText(NewsDetailActivity.this,"Home clicked",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(NewsDetailActivity.this,MainActivity.class);
                    startActivity(i);
                    mActionMode.finish();

                    break;

                case  R.id.mncomments:
                    //Toast.makeText(NewsDetailActivity.this,"comments clicked",Toast.LENGTH_SHORT).show();
                    //mActionMode.finish();


                    try {
                        Intent intent = new Intent(NewsDetailActivity.this, CommentsActivity.class);
                        CommentItem comments = newsService.getCommentsByNewsId(news.getId()).get(1);

                        intent.putExtra("news",news);
                        intent.putExtra("comments",comments);
                        startActivity(intent);
                    }
                    catch (Exception ex){
                        Toast.makeText(NewsDetailActivity.this, "Problem has occured while getting comments...", Toast.LENGTH_SHORT).show();
                    }


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

        getMenuInflater().inflate(R.menu.detailsmenuitem,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.mnhome:
                Toast.makeText(this,"Home clicked",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NewsDetailActivity.this,MainActivity.class);
                startActivity(i);

                break;
            case R.id.mncomments:

                Toast.makeText(this,"comments clicked",Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = new Intent(NewsDetailActivity.this,CommentsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("comments", (Serializable) comments);
                    intent.putExtras(bundle);

                    startActivity(intent);

                }catch (Exception e){

                    Toast.makeText(NewsDetailActivity.this, "Error Occured when clicked comments menu Item", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return true;
    }
}
