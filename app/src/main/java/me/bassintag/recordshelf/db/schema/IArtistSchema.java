package me.bassintag.recordshelf.db.schema;

/*
** Created by Antoine on 02/09/2017.
*/
public interface IArtistSchema {

  // TABLE

  String ARTIST_TABLE = "artists";

  // TAGS

  String COLUMN_ID_TAG = "_id";
  String COLUMN_NAME_TAG = "name";

  // COLUMNS

  String COLUMN_ID = ARTIST_TABLE + "." + COLUMN_ID_TAG;
  String COLUMN_NAME = ARTIST_TABLE + "." + COLUMN_NAME_TAG;

  // CREATE

  String ARTIST_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
      + ARTIST_TABLE + " ("
      + COLUMN_ID_TAG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_NAME_TAG + " TEXT NOT NULL"
      + ")";

  // COLUMNS

  String[] ARTIST_COLUMNS = new String[]{
      COLUMN_ID,
      COLUMN_NAME
  };
}
