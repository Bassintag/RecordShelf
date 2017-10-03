package me.bassintag.recordshelf.db.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
** Created by Antoine on 02/09/2017.
*/
public abstract class DbContentProvider {

  private final SQLiteDatabase mDb;

  public DbContentProvider(SQLiteDatabase db) {
    mDb = db;
  }

  protected long insert(String tableName, ContentValues values) {
    return mDb.insert(tableName, null, values);
  }

  protected int delete(String tableName, String selection, String[] selectionArgs) {
    return mDb.delete(tableName, selection, selectionArgs);
  }

  protected int update(String tableName, ContentValues values, String selection,
      String[] selectionArgs) {
    return mDb.update(tableName, values, selection, selectionArgs);
  }

  protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs,
      String orderBy) {
    return mDb.query(tableName, columns, selection, selectionArgs, null, null, orderBy);
  }

  protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs,
      String orderBy, String limit) {
    return mDb.query(tableName, columns, selection, selectionArgs, null, null, orderBy, limit);
  }

  public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs,
      String groupBy, String having, String orderBy, String limit) {
    return mDb.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
  }

  public Cursor rawQuery(String sql, String[] selectionArgs) {
    return mDb.rawQuery(sql, selectionArgs);
  }


}
