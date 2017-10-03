package me.bassintag.recordshelf.db.object;

import android.support.annotation.NonNull;
import me.bassintag.recordshelf.db.Database;

/*
** Created by Antoine on 07/09/2017.
*/
public class AlbumDescription {

  @NonNull
  private Album mAlbum;
  private Artist mArtist;
  private Genre mGenre;

  public AlbumDescription(@NonNull Album album, Database db) {
    this(album,
        db.getArtistDao().getArtistById(album.getArtistId()),
        db.getGenreDao().getGenreById(album.getGenreId()));
  }

  public AlbumDescription(@NonNull Album album) {
    this(album, null, null);
  }

  public AlbumDescription(@NonNull Album album, Genre genre) {
    this(album, null, genre);
  }

  public AlbumDescription(@NonNull Album album, Artist artist) {
    this(album, artist, null);
  }

  public AlbumDescription(@NonNull Album album, Artist artist, Genre genre) {
    mAlbum = album;
    mArtist = artist;
    mGenre = genre;
  }

  @NonNull
  public Album getAlbum() {
    return mAlbum;
  }

  public void setAlbum(@NonNull Album album) {
    this.mAlbum = album;
  }

  public Artist getArtist() {
    return mArtist;
  }

  public void setArtist(Artist artist) {
    this.mArtist = artist;
  }

  public Genre getGenre() {
    return mGenre;
  }

  public void setGenre(Genre genre) {
    this.mGenre = genre;
  }

  public long insert(Database db){
    if (mArtist != null) {
      Artist artist;
      if ((artist = db.getArtistDao().getArtistByName(mArtist.getName())) == null) {
        db.getArtistDao().addArtist(mArtist);
        artist = db.getArtistDao().getArtistByName(mArtist.getName());
      }
      mAlbum.setArtistId(artist.getId());
    }
    if (mGenre != null) {
      Genre genre;
      if ((genre = db.getGenreDao().getGenreByName(mGenre.getName())) == null) {
        db.getGenreDao().addGenre(mGenre);
        genre = db.getGenreDao().getGenreByName(mGenre.getName());
      }
      mAlbum.setGenreId(genre.getId());
    }
    return db.getAlbumDao().addAlbum(mAlbum);
  }
}
