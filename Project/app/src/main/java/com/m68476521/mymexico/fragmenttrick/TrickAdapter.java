package com.m68476521.mymexico.fragmenttrick;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.squareup.picasso.Picasso;

/**
 * TODO: add description
 */

public class TrickAdapter extends RecyclerView.Adapter<TrickAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;

    private final OnItemClicked listener;

    public TrickAdapter(Activity context, OnItemClicked listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_trick, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        cursor.moveToPosition(position);

        String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_QUESTION));
        Log.d("MIKE title", title);
        holder.textViewTitle.setText(title);

        Picasso.with(context)
                .load(R.drawable.ic_launcher_background)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageViewBackGround);

        String image = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_A));
        if (TextUtils.isEmpty(image)) {
            holder.imageViewBackGround.setVisibility(View.GONE);
        } else {
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.imageViewBackGround);
        }

        holder.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MIKE", "clicked fav");
                listener.onItemClick(v, position);
            }
        });

        holder.imageViewBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MIKE", "clicked fav");
                listener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewTitle;
        public final ImageView imageViewBackGround;
        public final ImageView imageViewFavorite;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.text_view_title);
            imageViewBackGround = view.findViewById(R.id.image_view_poster);
            imageViewFavorite = view.findViewById(R.id.image_view_fav);
        }
    }

    public void swapCursor(Cursor cursor) {
        if (this.cursor == cursor) {
            return;
        }
        this.cursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }
}
