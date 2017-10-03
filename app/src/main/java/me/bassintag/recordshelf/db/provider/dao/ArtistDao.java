package me.bassintag.recordshelf.db.provider.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.db.object.Artist;
import me.bassintag.recordshelf.db.provider.DbContentProvider;
import me.bassintag.recordshelf.db.schema.IArtistSchema;

/*
** Created by Antoine on 02/09/2017.
*/
public class ArtistDao extends DbContentProvider implements IArtistSchema, IArtistDao {

  public ArtistDao(SQLiteDatabase db) {
    super(db);
  }

  @Override
  public Artist getArtistById(int id) {
    String selectionArgs[] = {String.valueOf(id)};
    String selection = COLUMN_ID + " = ?";
    Cursor cursor = query(ARTIST_TABLE, ARTIST_COLUMNS, selection, selectionArgs, COLUMN_ID);
    Artist artist = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        artist = cursorToEntity(cursor);
      }
      cursor.close();
    }
    return artist;
  }

  @Override
  public Artist getArtistByName(String name) {
    String selectionArgs[] = {name};
    String selection = COLUMN_NAME + " = ?";
    Cursor cursor = query(ARTIST_TABLE, ARTIST_COLUMNS, selection, selectionArgs, COLUMN_ID);
    Artist artist = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        artist = cursorToEntity(cursor);
      }
      cursor.close();
    }
    return artist;
  }

  @Override
  public List<Artist> getAllArtists() {
    List<Artist> artistList = new ArrayList<>();
    Cursor cursor = query(ARTIST_TABLE, ARTIST_COLUMNS, null, null, COLUMN_NAME);
    Artist artist;
    if (cursor != null) {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        if ((artist = cursorToEntity(cursor)) != null) {
          artistList.add(artist);
        }
        cursor.moveToNext();
      }
      cursor.close();
    }
    return artistList;
  }

  @Override
  public boolean updateArtist(Artist artist) {
    String[] selectionArgs = {String.valueOf(artist.getId())};
    String selection = COLUMN_ID + " = ?";
    ContentValues contentValues = entityToContentValues(artist);
    return update(ARTIST_TABLE, contentValues, selection, selectionArgs) > 0;
  }

  @Override
  public boolean addArtist(Artist artist) {
    ContentValues contentValues = entityToContentValues(artist);
    return super.insert(ARTIST_TABLE, contentValues) > 0;
  }

  @Override
  public boolean addArtists(List<Artist> artists) {
    for (Artist artist : artists) {
      if (!addArtist(artist)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean deleteArtistById(int id) {
    String[] selectionArgs = {String.valueOf(id)};
    String selection = COLUMN_ID + " = ?";
    return delete(ARTIST_TABLE, selection, selectionArgs) > 0;
  }

  private Artist cursorToEntity(Cursor cursor) {
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
    return new Artist(id, name);
  }

  private ContentValues entityToContentValues(Artist artist) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_NAME_TAG, artist.getName());
    return contentValues;
  }
}
