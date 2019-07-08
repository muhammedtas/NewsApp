package app.homework2.model;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import app.homework2.R;


public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public NewsAdapter(@NonNull Context context,  @NonNull List<NewsItem> objects) {
        super(context, android.R.layout.simple_expandable_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        NewsViewHolder viewHolder = null;
        if (row==null){

            LayoutInflater inflator =((Activity)getContext()).getLayoutInflater();
            row =inflator.inflate(R.layout.news_row_layout,parent,false);
            viewHolder = new NewsViewHolder(row);
            row.setTag(viewHolder);
        }
        else{
            viewHolder = (NewsViewHolder)row.getTag();
        }


        viewHolder.getImgNews().setImageResource((getItem(position).getId()));
        viewHolder.getTxtNewsTitle().setText(getItem(position).getTitle());
        viewHolder.getTxtNewsDate().setText(getItem(position).getNewsDate().toString());
        return row;
    }

    class NewsViewHolder{
        View base;
        ImageView imgNews;
        TextView txtNewsTitle;
        //TextView txtNewsText;
        TextView txtNewsDate;
        String imgUrl;

        public NewsViewHolder(View base){
            this.base = base;
        }

        public ImageView getImgNews() {
            if (imgNews == null){
                new DownLoadImageTask(imgNews).execute(imgUrl);
                //imgNews = base.findViewById(R.id.imgNews);
            }
            return imgNews;
        }

        public TextView getTxtNewsTitle() {
            if (txtNewsTitle == null){
                txtNewsTitle = base.findViewById(R.id.txtNewsTitle);
            }
            return txtNewsTitle;
        }

        public TextView getTxtNewsDate() {
            if (txtNewsDate == null){

                txtNewsDate = base.findViewById(R.id.txtNewsDate);
            }
            return txtNewsDate;
        }
    }

    public class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){

            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){

                Log.e("Dev","DownLoadImageTask in doBackGround,  Details  :"+ e.getMessage());
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            try {
                imageView.setImageBitmap(result);

            }
            catch (Exception ex){

                Log.e("Dev","DownLoadImageTask onPostExecute. Details are : "+ex.getMessage());
            }
        }
    }
}
