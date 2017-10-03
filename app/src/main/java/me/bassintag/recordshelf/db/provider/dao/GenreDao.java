package me.bassintag.recordshelf.db.provider.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.db.object.Genre;
import me.bassintag.recordshelf.db.provider.DbContentProvider;
import me.bassintag.recordshelf.db.schema.IGenreSchema;

/*
** Created by Antoine on 02/09/2017.
*/
public class GenreDao extends DbContentProvider implements IGenreSchema, IGenreDao {

  public GenreDao(SQLiteDatabase db) {
    super(db);
  }

  @Override
  public Genre getGenreById(int id) {
    String selectionArgs[] = {String.valueOf(id)};
    String selection = COLUMN_ID + " = ?";
    Cursor cursor = query(GENRE_TABLE, GENRE_COLUMNS, selection, selectionArgs, COLUMN_ID);
    Genre genre = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        genre = cursorToEntity(cursor);
      }
      cursor.close();
    }
    return genre;
  }

  @Override
  public Genre getGenreByName(String name) {
    String selectionArgs[] = {name};
    String selection = COLUMN_NAME + " = ?";
    Cursor cursor = query(GENRE_TABLE, GENRE_COLUMNS, selection, selectionArgs, COLUMN_ID);
    Genre genre = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        genre = cursorToEntity(cursor);
      }
      cursor.close();
    }
    return genre;
  }

  @Override
  public List<Genre> getAllGenres() {
    List<Genre> genreList = new ArrayList<>();
    Cursor cursor = query(GENRE_TABLE, GENRE_COLUMNS, null, null, COLUMN_NAME);
    Genre genre;
    if (cursor != null) {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        if ((genre = cursorToEntity(cursor)) != null) {
          genreList.add(genre);
        }
        cursor.moveToNext();
      }
      cursor.close();
    }
    return genreList;
  }

  @Override
  public boolean updateGenre(Genre genre) {
    String[] selectionArgs = {String.valueOf(genre.getId())};
    String selection = COLUMN_ID + " = ?";
    ContentValues contentValues = entityToContentValues(genre);
    return update(GENRE_TABLE, contentValues, selection, selectionArgs) > 0;
  }

  @Override
  public boolean addGenre(Genre genre) {
    ContentValues contentValues = entityToContentValues(genre);
    return super.insert(GENRE_TABLE, contentValues) > 0;
  }

  @Override
  public boolean addGenres(List<Genre> genres) {
    for (Genre genre : genres) {
      if (!addGenre(genre)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean deleteGenreById(int id) {
    String[] selectionArgs = {String.valueOf(id)};
    String selection = COLUMN_ID + " = ?";
    return delete(GENRE_TABLE, selection, selectionArgs) > 0;
  }

  private Genre cursorToEntity(Cursor cursor) {
    int index;
    int id = -1;
    String name = null;
    if (cursor != null) {
      if ((index = cursor.getColumnIndex(COLUMN_ID_TAG)) != -1) {
        id = cursor.getInt(index);
      } else {
        return null;
      }
      if ((index = cursor.getColumnIndex(COLUMN_NAME_TAG)) != -1) {
        name = cursor.getString(index);
      } else {
        return null;
      }
    }
    return new Genre(id, name);
  }

  private ContentValues entityToContentValues(Genre genre) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_NAME_TAG, genre.getName());
    return contentValues;
  }
}
