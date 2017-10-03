package me.bassintag.recordshelf.adapter;

import android.graphics.Bitmap;
import me.bassintag.recordshelf.db.Database;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 13/09/2017.
*/
public class DbCoverProvider implements ICoverProvider<AlbumDescription> {

  private final Database mDb;

  public DbCoverProvider(Database db) {
    mDb = db;
  }

  @Override
  public Bitmap getCoverForAlbum(AlbumDescription albumDescription) {
    return mDb.getCoverDao().getCoverByAlbumId(albumDescription.getAlbum().getId());
  }
}
