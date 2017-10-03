package me.bassintag.recordshelf.db.provider.dao;

import java.util.List;
import me.bassintag.recordshelf.db.object.Artist;

/*
** Created by Antoine on 02/09/2017.
*/
public interface IArtistDao {

  // GET
  Artist getArtistById(int id);
  Artist getArtistByName(String name);
  List<Artist> getAllArtists();

  // UPDATE
  boolean updateArtist(Artist artist);

  // ADD
  boolean addArtist(Artist artist);
  boolean addArtists(List<Artist> artists);

  // DELETE
  boolean deleteArtistById(int id);
}
