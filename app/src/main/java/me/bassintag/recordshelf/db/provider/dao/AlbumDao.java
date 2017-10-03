package me.bassintag.recordshelf.db.provider.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.provider.DbContentProvider;
import me.bassintag.recordshelf.db.schema.IAlbumSchema;
import me.bassintag.recordshelf.db.schema.IArtistSchema;
import me.bassintag.recordshelf.db.schema.IGenreSchema;

/*
** Created by Antoine on 02/09/2017.
*/
public class AlbumDao extends DbContentProvider implements IAlbumSchema, IAlbumDao {

  public AlbumDao(SQLiteDatabase db) {
    super(db);
  }


  @Override
  public Album getAlbumById(long id) {
    String selectionArgs[] = {String.valueOf(id)};
    String selection = COLUMN_ID + " = ?";
    Cursor cursor = query(ALBUM_TABLE, ALBUM_COLUMNS, selection, selectionArgs, COLUMN_ID);
    Album album = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        album = cursorToEntity(cursor);
      }
      cursor.close();
    }
    return album;
  }

  private List<Album> getAlbumsBy(String selection, String[] selectionArgs, String sortColumn,
      String limit) {
    List<Album> albumList = new ArrayList<>();
    Cursor cursor = query(
        ALBUM_TABLE +
            " LEFT OUTER JOIN " + IArtistSchema.ARTIST_TABLE + " ON " +
            COLUMN_ARTIST_ID + "=" + IArtistSchema.COLUMN_ID +
            " LEFT OUTER JOIN " + IGenreSchema.GENRE_TABLE + " ON " +
            COLUMN_GENRE_ID + "=" + IGenreSchema.COLUMN_ID,
        ALBUM_COLUMNS, selection, selectionArgs, sortColumn + "," + COLUMN_NAME,
        limit);
    Album album;
    if (cursor != null) {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        if ((album = cursorToEntity(cursor)) != null) {
          albumList.add(album);
        }
        cursor.moveToNext();
      }
      cursor.close();
    }
    return albumList;
  }

  @Override
  public List<Album> getNAlbums(int start, int count) {
    return getAlbumsBy(null, null, COLUMN_NAME,
        String.valueOf(start) + "," + String.valueOf(count));
  }

  @Override
  public List<Album> getAllAlbums() {
    return getAlbumsBy(null, null, COLUMN_NAME, null);
  }

  @Override
  public List<Album> getAllAlbumsSortArtist() {
    return getAlbumsBy(null, null, IArtistSchema.COLUMN_NAME, null);
  }

  @Override
  public List<Album> getAllAlbumsSortGenre() {
    return getAlbumsBy(null, null, IGenreSchema.COLUMN_NAME, null);
  }

  @Override
  public List<Album> getAllAlbumsSortReleaseYear() {
    return getAlbumsBy(null, null, COLUMN_RELEASE_YEAR, null);
  }

  @Override
  public List<Album> getAllAlbumsByTitle(String title) {
    String[] selectionArgs = {title.replaceAll("^|\\s+|$", "%")};
    String selection = COLUMN_NAME + " LIKE ?";
    return getAlbumsBy(selection, selectionArgs, COLUMN_NAME, null);
  }

  @Override
  public List<Album> getAllAlbumsByReleaseYear(int year) {
    String[] selectionArgs = {String.valueOf(year)};
    String selection = COLUMN_RELEASE_YEAR + " = ?";
    return getAlbumsBy(selection, selectionArgs, COLUMN_NAME, null);
  }

  @Override
  public List<Album> getAllAlbumsByArtistId(int artistId) {
    String[] selectionArgs = {String.valueOf(artistId)};
    String selection = COLUMN_ARTIST_ID + " = ?";
    return getAlbumsBy(selection, selectionArgs, COLUMN_NAME, null);
  }

  @Override
  public List<Album> getAllAlbumsByGenreId(int genreId) {
    String[] selectionArgs = {String.valueOf(genreId)};
    String selection = COLUMN_GENRE_ID + " = ?";
    return getAlbumsBy(selection, selectionArgs, COLUMN_NAME, null);
  }

  public List<Album> getAllAlbumsWithoutArtist() {
    String selection = COLUMN_ARTIST_ID + " IS NULL OR " + COLUMN_ARTIST_ID + " <= 0";
    return getAlbumsBy(selection, null, COLUMN_NAME, null);
  }

  public List<Album> getAllAlbumsWithoutGenre() {
    String selection = COLUMN_GENRE_ID + " IS NULL OR " + COLUMN_GENRE_ID + " <= 0";
    return getAlbumsBy(selection, null, COLUMN_NAME, null);
  }

  @Override
  public boolean updateAlbum(Album album) {
    String[] selectionArgs = {String.valueOf(album.getId())};
    String selection = COLUMN_ID + " = ?";
    ContentValues contentValues = entityToContentValues(album);
    return update(ALBUM_TABLE, contentValues, selection, selectionArgs) > 0;
  }

  @Override
  public long addAlbum(Album album) {
    ContentValues contentValues = entityToContentValues(album);
    return super.insert(ALBUM_TABLE, contentValues);
  }

  @Override
  public boolean addAlbums(List<Album> albums) {
    for (Album album : albums) {
      if (addAlbum(album) < 0) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean deleteAlbum(int id) {
    String[] selectionArgs = {String.valueOf(id)};
    String selection = COLUMN_ID + " = ?";
    return delete(ALBUM_TABLE, selection, selectionArgs) > 0;
  }

  private Album cursorToEntity(Cursor cursor) {
    int index;
    int id = -1;
    String name = null;
    int releaseYear = -1;
    int artistId = -1;
    int genreId = -1;
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
      if ((index = cursor.getColumnIndex(COLUMN_RELEASE_YEAR_TAG)) != -1) {
        releaseYear = cursor.getInt(index);
      }
      if ((index = cursor.getColumnIndex(COLUMN_ARTIST_ID_TAG)) != -1) {
        artistId = cursor.getInt(index);
      }
      if ((index = cursor.getColumnIndex(COLUMN_GENRE_ID_TAG)) != -1) {
        genreId = cursor.getInt(index);
      }
    }
    return new Album(id, name, releaseYear, artistId, genreId);
  }

  private ContentValues entityToContentValues(Album album) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_NAME_TAG, album.getName());
    contentValues.put(COLUMN_RELEASE_YEAR_TAG, album.getReleaseYear());
    contentValues.put(COLUMN_ARTIST_ID_TAG, album.getArtistId());
    contentValues.put(COLUMN_GENRE_ID_TAG, album.getGenreId());
    return contentValues;
  }
}
