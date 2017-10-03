package me.bassintag.recordshelf.db.provider.dao;

import android.graphics.Bitmap;

/*
** Created by Antoine on 13/09/2017.
*/
public interface ICoverDao {

  // GET
  Bitmap getCoverByAlbumId(int id);
  boolean getHasCoverByAlbumId(long id);

  // UPDATE
  boolean setAlbumCoverByAlbumId(long id, Bitmap cover);

  // DELETE
  boolean deleteCoverByAlbumId(int id);
}
