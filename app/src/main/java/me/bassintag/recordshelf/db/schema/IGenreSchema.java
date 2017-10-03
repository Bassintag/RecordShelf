package me.bassintag.recordshelf.db.schema;

/*
** Created by Antoine on 02/09/2017.
*/
public interface IGenreSchema {

  // TABLE

  String GENRE_TABLE = "genres";

  // TAGS

  String COLUMN_ID_TAG = "_id";
  String COLUMN_NAME_TAG = "name";

  // COLUMNS

  String COLUMN_ID = GENRE_TABLE + "." + COLUMN_ID_TAG;
  String COLUMN_NAME = GENRE_TABLE + "." + COLUMN_NAME_TAG;

  // CREATE

  String GENRE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
      + GENRE_TABLE + " ("
      + COLUMN_ID_TAG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_NAME_TAG + " TEXT NOT NULL"
      + ")";

  // COLUMNS

  String[] GENRE_COLUMNS = new String[]{
      COLUMN_ID, COLUMN_NAME
  };
}
