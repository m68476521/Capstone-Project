package com.m68476521.mymexico.fragmentnews;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;
import com.squareup.picasso.Picasso;

/**
 * This class is an adapter for the news also it can be used in favorites fragment
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final Context context;
    private Cursor cursor;
    private final NewsItemClickListener newsItemClickListener;
    private final OnItemClicked listener;

    public NewsAdapter(Activity context, OnItemClicked listener, NewsItemClickListener newsItemClickListener) {
        this.listener = listener;
        this.context = context;
        this.newsItemClickListener = newsItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_news, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String id = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_ID));
        boolean favorite = isFavorite(id);

        if (favorite)
            holder.imageViewFavorite.setImageResource(R.drawable.ic_favorite);

        String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
        holder.textViewTitle.setText(title);

        String desc = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION));
        holder.textViewShortDesc.setText(desc);

        Picasso.with(holder.itemView.getContext())
                .load(R.drawable.ic_launcher_background)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageViewBackGround);

        String image = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMAGE));
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
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });


        ViewCompat.setTransitionName(holder.imageViewBackGround, title);
        holder.imageViewBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsItemClickListener.onlItemClick("NEWS", holder.getAdapterPosition(), holder.imageViewBackGround, v);
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
        public final TextView textViewShortDesc;
        public final ImageView imageViewBackGround;
        public final ImageView imageViewFavorite;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.text_view_title);
            textViewShortDesc = view.findViewById(R.id.text_view_short_desc);
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

    private boolean isFavorite(String id) {
        Cursor cursorResult = null;
        try {
            Uri uri2 = TaskContract.TaskEntry.CONTENT_URI_FAVORITES;

            uri2 = uri2.buildUpon().appendPath(id).build();
            cursorResult = context.getContentResolver().query(uri2,
                    null,
                    null,
                    null,
                    null);
        } finally {
            cursorResult.close();
        }

        int numberCount = cursorResult.getCount();

        return numberCount > 0;

    }
}
