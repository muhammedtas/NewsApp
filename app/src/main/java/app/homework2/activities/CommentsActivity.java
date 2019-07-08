package app.homework2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import app.homework2.R;
import app.homework2.model.CommentItem;
import app.homework2.model.CommentsAdapter;
import app.homework2.model.NewsItem;

public class CommentsActivity extends AppCompatActivity {

    ListView lstComments;
    List<CommentItem> comments = null;
    NewsItem newsItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher_round);
        getSupportActionBar().setTitle(R.string.comments);

        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            comments = (List<CommentItem>)bundle.getSerializable("comments");
            newsItem = (NewsItem)bundle.getSerializable("news");

        }
        catch(Exception ex){

            Toast.makeText(CommentsActivity.this, "Could not get the newsItem or comments from bundle's extras in Comments Activity", Toast.LENGTH_SHORT).show();
        }

        try {
            lstComments = findViewById(R.id.lstComments);
            CommentsAdapter adapter = new CommentsAdapter(this, comments); // holder activity nin kenbdisi
            lstComments.setAdapter(adapter);
        }catch (Exception ex){
            Toast.makeText(CommentsActivity.this, "Couldn not set CommentsAdapter to comment list", Toast.LENGTH_SHORT).show();
        }
    }

    ActionMode mActionMode;

    ActionMode.Callback myCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {


            getMenuInflater().inflate(R.menu.commentsmenuitem,menu);

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
                    Toast.makeText(CommentsActivity.this,"Home clicked",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CommentsActivity.this,MainActivity.class);
                    startActivity(i);
                    mActionMode.finish();

                    break;

                case  R.id.mnwrite:
                    Toast.makeText(CommentsActivity.this,"Write clicked",Toast.LENGTH_SHORT).show();
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

        getMenuInflater().inflate(R.menu.commentsmenuitem,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.mnhome:
                Toast.makeText(this,"Home clicked",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(CommentsActivity.this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.mnwrite:
                Toast.makeText(this,"Write clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CommentsActivity.this,WriteActivity.class);
                intent.putExtra("news",newsItem);
                startActivity(intent);
                break;
        }
        return true;
    }
}
