package com.example.user.newsappstagetwo;

public class News {

    private String mSectionName;
    private String mPublishTime;
    private String mArticleTitle;
    private String mUrl;
    private String mAuthor;
    private String mThumbnail;

    public News(String sectionName, String publishTime,
                String articleTitle, String url, String author, String thumbnail){
        mSectionName = sectionName;
        mPublishTime = publishTime;
        mArticleTitle = articleTitle;
        mUrl = url;
        mAuthor = author;
        mThumbnail = thumbnail;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getPublishTime() {
        return mPublishTime;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getThumbnail() { return  mThumbnail; }
}
