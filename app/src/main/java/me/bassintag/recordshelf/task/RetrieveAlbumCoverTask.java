package me.bassintag.recordshelf.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.db.object.WebAlbumDescription;
import me.bassintag.recordshelf.task.RetrieveAlbumCoverTask.RetrievedCover;
import me.bassintag.recordshelf.utils.BitmapUtils;

/*
** Created by Antoine on 08/09/2017.
*/
public class RetrieveAlbumCoverTask extends
    AsyncTask<WebAlbumDescription, RetrievedCover, Integer> {

  public static class RetrievedCover {
    public final AlbumDescription albumDescription;
    public final Bitmap cover;

    public RetrievedCover(AlbumDescription albumDescription, Bitmap cover) {
      this.albumDescription = albumDescription;
      this.cover = cover;
    }
  }

  private static final String COVERS_BASE_URL = "http://coverartarchive.org/release/";
  private static final String COVER_END_URL = "/front-500";

  private IRetrieveAlbumCoverListener mCallback;

  public RetrieveAlbumCoverTask(IRetrieveAlbumCoverListener callback) {
    mCallback = callback;
  }

  private Bitmap getCoverFromId(String mid) {
    URL url;
    try {
      url = new URL(COVERS_BASE_URL + mid + COVER_END_URL);
      System.out.println("Loading url cover: '" + url.toString() + "'");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
    HttpURLConnection connection;
    Bitmap bitmap;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoInput(true);
      connection.connect();
      if (connection.getResponseCode() != 200) {
        return null;
      }
      InputStream input = connection.getInputStream();
      bitmap = BitmapFactory.decodeStream(input);
      if (bitmap == null) {
        return null;
      }
      bitmap = BitmapUtils.resizeToSquare(bitmap);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return bitmap;
  }

  @Override
  protected Integer doInBackground(WebAlbumDescription... params) {
    int loaded = 0;
    for (WebAlbumDescription description : params) {
      Bitmap cover = getCoverFromId(description.getWebId());
      if (cover != null) {
        RetrievedCover retrievedCover = new RetrievedCover(description, cover);
        publishProgress(retrievedCover);
        loaded += 1;
      }
    }
    return loaded;
  }

  @Override
  protected void onProgressUpdate(RetrievedCover... retrievedCovers) {
    for (RetrievedCover retrievedCover : retrievedCovers) {
      mCallback.onAlbumCoverRetrieved(retrievedCover);
    }
  }
}
