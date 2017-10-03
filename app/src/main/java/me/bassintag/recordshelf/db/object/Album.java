package me.bassintag.recordshelf.db.object;

import android.graphics.Bitmap;

/*
** Created by Antoine on 02/09/2017.
*/
public class Album extends DatabaseObject {

  private String mName;
  private int mReleaseYear;
  private int mArtistId;
  private int mGenreId;
  private Bitmap mCover;

  public Album(int id, String name, int releaseYear, int artistId, int genreId) {
    super(id);
    mName = name;
    mReleaseYear = releaseYear;
    mArtistId = artistId;
    mGenreId = genreId;
  }

  public Album(String name, int releaseYear, Bitmap cover, int artistId, int genreId) {
    super(-1);
    mName = name;
    mReleaseYear = releaseYear;
    mCover = cover;
    mArtistId = artistId;
    mGenreId = genreId;
  }

  public Album(String name) {
    super(-1);
    mName = name;
    mReleaseYear = -1;
    mCover = null;
    mArtistId = -1;
    mGenreId = -1;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public int getReleaseYear() {
    return mReleaseYear;
  }

  public void setReleaseYear(int releaseYear) {
    mReleaseYear = releaseYear;
  }

  public int getArtistId() {
    return mArtistId;
  }

  public void setArtistId(int artistId) {
    mArtistId = artistId;
  }

  public int getGenreId() {
    return mGenreId;
  }

  public void setGenreId(int genreId) {
    mGenreId = genreId;
  }
}
