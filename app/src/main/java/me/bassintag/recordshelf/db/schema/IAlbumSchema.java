package me.bassintag.recordshelf.db.schema;

/*
** Created by Antoine on 02/09/2017.
*/
public interface IAlbumSchema {

  // TABLE

  String ALBUM_TABLE = "albums";

  // TAGS

  String COLUMN_ID_TAG = "_id";
  String COLUMN_NAME_TAG = "name";
  String COLUMN_RELEASE_YEAR_TAG = "release_year";
  String COLUMN_ARTIST_ID_TAG = "artist_id";
  String COLUMN_GENRE_ID_TAG = "genre_id";

  // COLUMNS

  String COLUMN_ID = ALBUM_TABLE + "." + COLUMN_ID_TAG;
  String COLUMN_NAME = ALBUM_TABLE + "." + COLUMN_NAME_TAG;
  String COLUMN_RELEASE_YEAR = ALBUM_TABLE + "." + COLUMN_RELEASE_YEAR_TAG;
  String COLUMN_ARTIST_ID = ALBUM_TABLE + "." + COLUMN_ARTIST_ID_TAG;
  String COLUMN_GENRE_ID = ALBUM_TABLE + "." + COLUMN_GENRE_ID_TAG;

  // CREATE

  String ALBUM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
      + ALBUM_TABLE + " ("
      + COLUMN_ID_TAG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_NAME_TAG + " TEXT NOT NULL, "
      + COLUMN_RELEASE_YEAR_TAG + " INTEGER, "
      + COLUMN_ARTIST_ID_TAG + " INTEGER, "
      + COLUMN_GENRE_ID_TAG + " INTEGER"
      + ")";

  // COLUMNS

  String[] ALBUM_COLUMNS = new String[]{
      COLUMN_ID,
      COLUMN_NAME,
      COLUMN_RELEASE_YEAR,
      COLUMN_ARTIST_ID,
      COLUMN_GENRE_ID
  };

}
