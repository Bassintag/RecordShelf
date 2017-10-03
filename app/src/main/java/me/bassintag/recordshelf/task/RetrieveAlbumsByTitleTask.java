package me.bassintag.recordshelf.task;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/*
** Created by Antoine on 07/09/2017.
*/
public class RetrieveAlbumsByTitleTask extends RetrieveAlbumsTask {

  public RetrieveAlbumsByTitleTask(IRetrieveAlbumsListener callback) {
    super(callback);
  }

  @Override
  protected URL createURL(String query) throws MalformedURLException {
    try {
      return new URL(BASE_URL + URLEncoder
          .encode("release:" + query + " AND status:official AND (format:/CD|.*Vinyl.*/)",
              "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }
}
