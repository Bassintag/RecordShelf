package me.bassintag.recordshelf.db.provider.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import me.bassintag.recordshelf.db.provider.DbContentProvider;
import me.bassintag.recordshelf.db.schema.ICoverSchema;

/*
** Created by Antoine on 13/09/2017.
*/
public class CoverDao extends DbContentProvider implements ICoverSchema, ICoverDao {

  public CoverDao(SQLiteDatabase db) {
    super(db);
  }

  @Override
  public Bitmap getCoverByAlbumId(int id) {
    String[] selectionArgs = {String.valueOf(id)};
    String selection = COLUMN_ALBUM_ID + " = ?";
    Cursor cursor = query(COVER_TABLE, COVER_COLUMNS, selection, selectionArgs, COLUMN_ALBUM_ID);
    Bitmap result = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        result = cursorToBitmap(cursor);
      }
      cursor.close();
    }
    return result;
  }

  @Override
  public boolean getHasCoverByAlbumId(long id) {
    String[] selectionArgs = {String.valueOf(id)};
    String selection = COLUMN_ALBUM_ID + " = ?";
    Cursor cursor = query(COVER_TABLE, COVER_COLUMNS, selection, selectionArgs, COLUMN_ALBUM_ID);
    if (cursor == null) {
      return false;
    }
    cursor.moveToFirst();
    boolean result = !cursor.isAfterLast();
    cursor.close();
    return result;
  }

  @Override
  public boolean setAlbumCoverByAlbumId(long id, Bitmap cover) {
    ContentValues contentValues = createContentValues(id, cover);
    if (!getHasCoverByAlbumId(id)) {
      return insert(COVER_TABLE, contentValues) > 0;
    } else {
      String[] selectionArgs = {String.valueOf(id)};
      String selection = COLUMN_ALBUM_ID + " = ?";
      return update(COVER_TABLE, contentValues, selection, selectionArgs) > 0;
    }
  }


  @Override
  public boolean deleteCoverByAlbumId(int id) {
    String[] selectionArgs = {String.valueOf(id)};
    String selection = COLUMN_ALBUM_ID + " = ?";
    return delete(COVER_TABLE, selection, selectionArgs) > 0;
  }

  private Bitmap cursorToBitmap(Cursor cursor) {
    Bitmap bitmap = null;
    if (cursor != null) {
      int index;
      if ((index = cursor.getColumnIndex(COLUMN_COVER_TAG)) != -1) {
        byte[] data = cursor.getBlob(index);
        if (data != null) {
          bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
      }
    }
    return bitmap;
  }

  private ContentValues createContentValues(long id, Bitmap cover) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_ALBUM_ID_TAG, id);
    if (cover != null) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      cover.compress(CompressFormat.PNG, 0, outputStream);
      byte[] data = outputStream.toByteArray();
      contentValues.put(COLUMN_COVER_TAG, data);
    } else {
      contentValues.putNull(COLUMN_COVER_TAG);
    }
    return contentValues;
  }
}
