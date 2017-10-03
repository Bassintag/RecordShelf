package me.bassintag.recordshelf.db.object;

import android.support.annotation.NonNull;
import me.bassintag.recordshelf.db.Database;

/*
** Created by Antoine on 08/09/2017.
*/
public class WebAlbumDescription extends AlbumDescription {

  private String mWebId;

  public WebAlbumDescription(@NonNull Album album,
      Database db) {
    super(album, db);
  }

  public WebAlbumDescription(@NonNull Album album) {
    super(album);
  }

  public WebAlbumDescription(@NonNull Album album, Genre genre) {
    super(album, genre);
  }

  public WebAlbumDescription(@NonNull Album album, Artist artist) {
    super(album, artist);
  }

  public WebAlbumDescription(@NonNull Album album, Artist artist,
      Genre genre) {
    super(album, artist, genre);
  }

  public String getWebId() {
    return mWebId;
  }

  public void setWebId(String webId) {
    mWebId = webId;
  }
}
