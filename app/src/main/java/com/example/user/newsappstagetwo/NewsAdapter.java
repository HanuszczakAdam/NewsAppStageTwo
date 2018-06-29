package com.example.user.newsappstagetwo;

import android.content.Context;
import android.media.ImageReader;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    static class ViewHolder {

        private TextView sectionNameView;
        private TextView publishTimeView;
        private TextView articleTitleView;
        private TextView authorView;
        private ImageView thumbnailView;
    }

    public NewsAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }
        News currentNews = getItem(position);
        viewHolder = new ViewHolder();

        String sectionName = currentNews.getSectionName();
        viewHolder.sectionNameView = convertView.findViewById(R.id.section_name);
        viewHolder.sectionNameView.setText(sectionName);

        String date = currentNews.getPublishTime();
        viewHolder.publishTimeView = convertView.findViewById(R.id.publish_time);
        viewHolder.publishTimeView.setText(date);

        String articleTitle = currentNews.getArticleTitle();
        viewHolder.articleTitleView = convertView.findViewById(R.id.article_title);
        viewHolder.articleTitleView.setText(articleTitle);

        String author = currentNews.getAuthor();
        viewHolder.authorView = convertView.findViewById(R.id.author);
        viewHolder.authorView.setText("Author: " + author);

        String thumbnailUrl = currentNews.getThumbnail();
        viewHolder.thumbnailView = convertView.findViewById(R.id.networkThumbnail);
        viewHolder.thumbnailView.setVisibility(View.VISIBLE);

        Ima


        return convertView;
    }


}