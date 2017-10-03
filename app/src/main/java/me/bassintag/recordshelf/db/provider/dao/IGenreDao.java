package me.bassintag.recordshelf.db.provider.dao;

import java.util.List;
import me.bassintag.recordshelf.db.object.Genre;

/*
** Created by Antoine on 02/09/2017.
*/
public interface IGenreDao {

  // GET
  Genre getGenreById(int id);
  Genre getGenreByName(String name);
  List<Genre> getAllGenres();

  // UPDATE
  boolean updateGenre(Genre genre);

  // ADD
  boolean addGenre(Genre genre);
  boolean addGenres(List<Genre> genres);

  // DELETE
  boolean deleteGenreById(int id);
}
