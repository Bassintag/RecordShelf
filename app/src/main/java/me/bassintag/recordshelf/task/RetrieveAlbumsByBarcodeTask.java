package me.bassintag.recordshelf.task;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/*
** Created by Antoine on 10/09/2017.
*/
public class RetrieveAlbumsByBarcodeTask extends RetrieveAlbumsTask {

  public RetrieveAlbumsByBarcodeTask(IRetrieveAlbumsListener callback) {
    super(callback);
  }

  @Override
  protected URL createURL(String query) throws MalformedURLException {
    try {
      return new URL(BASE_URL + URLEncoder.encode("barcode:" + query, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }
}
