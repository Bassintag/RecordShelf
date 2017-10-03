package me.bassintag.recordshelf.task;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.db.object.WebAlbumDescription;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.Artist;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*
** Created by Antoine on 07/09/2017.
*/
public abstract class RetrieveAlbumsTask extends
    AsyncTask<String, WebAlbumDescription, List<WebAlbumDescription>> {

  private static final String REQUEST_METHOD = "GET";
  private static final int CONNECTION_TIMEOUT = 15000;
  private static final int READ_TIMEOUT = 15000;

  static final String BASE_URL = "http://musicbrainz.org/ws/2/release/?fmt=json&query=";

  private final IRetrieveAlbumsListener mCallback;

  RetrieveAlbumsTask(IRetrieveAlbumsListener callback) {
    mCallback = callback;
  }

  protected abstract URL createURL(String query) throws MalformedURLException;

  private List<JSONObject> readAlbumsFromStream(InputStream stream) {
    InputStreamReader streamReader = new InputStreamReader(stream);
    BufferedReader reader = new BufferedReader(streamReader);
    StringBuilder data = new StringBuilder();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        data.append(line);
      }
      reader.close();
      streamReader.close();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    try {
      JSONObject root = new JSONObject(data.toString());
      JSONArray results = root.getJSONArray("releases");
      List<JSONObject> albums = new ArrayList<>();
      for (int i = 0; i < results.length(); i++) {
        albums.add(results.getJSONObject(i));
      }
      return albums;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  private WebAlbumDescription jsonToAlbum(JSONObject object) {
    Album album;
    Artist artist = null;
    String mid;
    try {
      String name = object.getString("title");
      album = new Album(name);
      mid = object.getString("id");
      if (object.has("date")) {
        String date = object.getString("date");
        if (date.contains("-")) {
          date = date.substring(0, date.indexOf("-"));
        }
        album.setReleaseYear(Integer.valueOf(date));
      }
      JSONArray artists = object.getJSONArray("artist-credit");
      if (artists.length() > 0) {
        JSONObject artistJson = artists.getJSONObject(0);
        if (artistJson.has("artist")) {
          artistJson = artistJson.getJSONObject("artist");
          if (artistJson.has("name")) {
            artist = new Artist(artistJson.getString("name"));
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    WebAlbumDescription albumDescription = new WebAlbumDescription(album, artist);
    albumDescription.setWebId(mid);
    return albumDescription;
  }

  @Override
  protected List<WebAlbumDescription> doInBackground(String... queries) {
    List<WebAlbumDescription> result = new ArrayList<>();
    try {
      for (String title : queries) {
        title = title.replace("\\", "\\\\").replace("\"", "\\\"");
        URL url = createURL(title);
        System.out.println("URL: " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(REQUEST_METHOD);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.connect();
        if (connection.getResponseCode() != 200) {
          return null;
        }
        InputStream inputStream = connection.getInputStream();
        if (inputStream != null) {
          List<JSONObject> albumsJson = readAlbumsFromStream(connection.getInputStream());
          if (albumsJson != null) {
            for (JSONObject albumJson : albumsJson) {
              WebAlbumDescription album = jsonToAlbum(albumJson);
              if (album != null) {
                result.add(album);
                publishProgress(album);
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return result;
  }

  @Override
  protected void onProgressUpdate(WebAlbumDescription... albumDescriptions) {
    for (WebAlbumDescription albumDescription : albumDescriptions) {
      mCallback.onAlbumRetrieved(albumDescription);
    }
  }

  @Override
  protected void onPostExecute(List<WebAlbumDescription> albums) {
    if (albums == null) {
      mCallback.onTaskError();
    } else {
      mCallback.onTaskEnd(albums);
    }
  }
}
