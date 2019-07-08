package app.homework2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.homework2.R;
import app.homework2.model.CommentItem;
import app.homework2.model.NewsItem;
import app.homework2.service.NewsService;

public class WriteActivity extends AppCompatActivity {

    TextView txtCommentsName;
    TextView txtCommentsMessage;
    Button btnSendMessage;
    List<CommentItem> comments = null;
    NewsItem newsItem = null;
    NewsService newsService = new NewsService();
    public static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher_round);
            getSupportActionBar().setTitle(R.string.typecomment);
        }catch (Exception ex){
            Toast.makeText(WriteActivity.this, "Could not set Action bar", Toast.LENGTH_SHORT).show();
        }
        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            comments = (List<CommentItem>)bundle.getSerializable("comments");
            newsItem = (NewsItem)bundle.getSerializable("news");
        }
        catch (Exception ex){
            Toast.makeText(WriteActivity.this, "Could not get the newsItem or comments from bundle's extras in Comments Activity", Toast.LENGTH_SHORT).show();
            Log.e("Dev","Could not get the bundles in WriteActivity. Details are    :" + ex.getMessage());

        }

        try {
            txtCommentsMessage = findViewById(R.id.txtMyCommandMessage);
            txtCommentsName = findViewById(R.id.txtMyCommandName);
            btnSendMessage = findViewById(R.id.btnSendComment);
            comments = newsService.allCommentsByNewsId;
        }
        catch(Exception ex){
            Toast.makeText(WriteActivity.this,"Problem has been occured while creating this page...",Toast.LENGTH_SHORT).show();
        }

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (typeComment(v)==true){
                        Toast.makeText(WriteActivity.this,"Success",Toast.LENGTH_SHORT).show();

                        try {
                            Intent intent = new Intent(WriteActivity.this,CommentsActivity.class);

                            Bundle bundle = new Bundle();
                            //bundle.putSerializable("comments", (Serializable) newsService.getCommentsByNewsId()); *** Düzelt
                            intent.putExtras(bundle);

                            startActivity(intent);

                        }
                        catch (Exception ex){
                            Toast.makeText(WriteActivity.this,"I couldnot open all the comments after texting your comment",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(WriteActivity.this,"Error on onClik method in else",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex){
                    Toast.makeText(WriteActivity.this,"Error on onClick method in  catch block",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean typeComment(View v){

        try {

            if (txtCommentsMessage.getText().toString() !="" && txtCommentsName.getText().toString() != ""){

                Toast.makeText(WriteActivity.this,"Type command entered",Toast.LENGTH_SHORT).show();

                try {

                    CommentItem commentItem = new CommentItem();

                    commentItem.setName(txtCommentsName.getText().toString());
                    commentItem.setMessage(txtCommentsMessage.getText().toString());
                    commentItem.setNews_id(newsItem.getId());

                    String result = newsService.PostComment(commentItem);

                    Toast.makeText(WriteActivity.this,result,Toast.LENGTH_SHORT).show();

                    return true;

                }
                catch (Exception ex){
                    Toast.makeText(WriteActivity.this,"Comment could not has been send",Toast.LENGTH_SHORT).show();
                }
            }

            else{
                Toast.makeText(WriteActivity.this,"Please fill all field to send your comment",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception ex){
            Toast.makeText(WriteActivity.this,"Type method  returned false",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    ActionMode mActionMode;

    ActionMode.Callback myCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {


            getMenuInflater().inflate(R.menu.writemenuitem,menu);

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
                    Toast.makeText(WriteActivity.this,"Home clicked",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(WriteActivity.this,MainActivity.class);
                    startActivity(i);
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
        getMenuInflater().inflate(R.menu.writemenuitem,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mnhome:
                Toast.makeText(this,"Home clicked",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(WriteActivity.this,MainActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
