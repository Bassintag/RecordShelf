package me.bassintag.recordshelf.db.schema;

/*
** Created by Antoine on 13/09/2017.
*/
public interface ICoverSchema {

  // TABLE

  String COVER_TABLE = "covers";

  // TAGS

  String COLUMN_ALBUM_ID_TAG = "album_id";
  String COLUMN_COVER_TAG = "cover";

  // COLUMNS

  String COLUMN_ALBUM_ID = COVER_TABLE + "." + COLUMN_ALBUM_ID_TAG;
  String COLUMN_COVER = COVER_TABLE + "." + COLUMN_COVER_TAG;

  // CREATE

  String COVER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
      + COVER_TABLE + "("
      + COLUMN_ALBUM_ID_TAG + " INTEGER PRIMARY KEY, "
      + COLUMN_COVER_TAG + " BLOB"
      + ")";

  // COLUMNS

  String[] COVER_COLUMNS = new String[]{
      COLUMN_ALBUM_ID,
      COLUMN_COVER
  };
}
