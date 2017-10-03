package me.bassintag.recordshelf.db.provider.dao;

import android.graphics.Bitmap;
import java.util.List;
import me.bassintag.recordshelf.db.object.Album;

/*
** Created by Antoine on 02/09/2017.
*/
public interface IAlbumDao {

  // GET
  Album getAlbumById(long id);

  List<Album> getNAlbums(int start, int count);
  List<Album> getAllAlbums();
  List<Album> getAllAlbumsSortArtist();
  List<Album> getAllAlbumsSortGenre();
  List<Album> getAllAlbumsSortReleaseYear();
  List<Album> getAllAlbumsByTitle(String title);
  List<Album> getAllAlbumsByReleaseYear(int year);
  List<Album> getAllAlbumsByArtistId(int artistId);
  List<Album> getAllAlbumsByGenreId(int genreId);
  List<Album> getAllAlbumsWithoutArtist();
  List<Album> getAllAlbumsWithoutGenre();

  // UPDATE
  boolean updateAlbum(Album album);

  // ADD
  long addAlbum(Album album);
  boolean addAlbums(List<Album> albums);

  // DELETE
  boolean deleteAlbum(int id);


}
