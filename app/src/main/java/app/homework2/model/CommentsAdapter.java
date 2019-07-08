package app.homework2.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.homework2.R;


public class CommentsAdapter extends ArrayAdapter<CommentItem> {

    public CommentsAdapter(@NonNull Context context,  @NonNull List<CommentItem> objects) {
        super(context, android.R.layout.simple_expandable_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        CommentsViewHolder viewHolder = null;
        if (row==null){
            LayoutInflater inflator =((Activity)getContext()).getLayoutInflater();
            row =inflator.inflate(R.layout.comments_row_layout,parent,false);
            viewHolder = new CommentsViewHolder(row);
            row.setTag(viewHolder);
        }
        else{
            viewHolder = (CommentsViewHolder)row.getTag();
        }
        viewHolder.getName().setText(getItem(position).getName());
        viewHolder.getMessage().setText(getItem(position).getMessage());

        return row;
    }

    public class CommentsViewHolder{

        View base;
        TextView name;
        TextView message;

        public CommentsViewHolder(View base) {
            this.base = base;
        }


        public TextView getName() {

            if (name == null){

                name = base.findViewById(R.id.txtCommentsName);
            }
            return name;
        }

        public TextView getMessage() {
            if (message == null){

                message = base.findViewById(R.id.txtCommentsMessage);
            }

            return message;
        }
    }
}
