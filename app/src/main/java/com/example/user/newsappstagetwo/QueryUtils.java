package com.example.user.newsappstagetwo;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    private QueryUtils(){

    }

    public static List<News> fetchNewsData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> newsList = extractFeatureFromJson(jsonResponse);
        return newsList;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(inputStream != null)
                inputStream.close();

        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "yyyy-MM-dd";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON))
            return null;

        List<News> newsList = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject responseObject = baseJsonResponse.optJSONObject("response");
            JSONArray newsArray = responseObject.optJSONArray("results");

            for(int i = 0; i < newsArray.length(); i++){
                JSONObject currentNews = newsArray.optJSONObject(i);
                String sectionName = currentNews.optString("sectionName");

                String publicationDate = currentNews.optString("webPublicationDate");
                publicationDate = formatDate(publicationDate);

                String webTitle = currentNews.optString("webTitle");

                String url = currentNews.optString("webUrl");
                url = url.replace("\\","");

                JSONArray tags = currentNews.optJSONArray("tags");
                String articleAuthor;
                if(tags.length()!=0) {
                    JSONObject tagsObject = tags.optJSONObject(0);
                    articleAuthor = tagsObject.optString("webTitle");
                } else { articleAuthor = "no Author"; }

                JSONObject fields = currentNews.optJSONObject("fields");
                String thumbnail;
                if(fields.length() != 0){
                    thumbnail = fields.optString("thumbnail");
                    thumbnail = thumbnail.replace("\\","");
                } else { thumbnail = "no thumbnail"; }

                News news = new News(sectionName, publicationDate, webTitle, url, articleAuthor, thumbnail);
                newsList.add(news);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }
        return newsList;
    }

}