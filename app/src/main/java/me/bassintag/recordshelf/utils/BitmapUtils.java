package me.bassintag.recordshelf.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;

/*
** Created by Antoine on 05/09/2017.
*/
public class BitmapUtils {

  private static final int MAX_THUMBNAIL_SIZE = 540;

  @NonNull
  public static Bitmap resizeToSquare(@NonNull Bitmap bitmap) {
    int dimension = Math.min(MAX_THUMBNAIL_SIZE, Math.min(bitmap.getWidth(), bitmap.getHeight()));
    return ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
  }

}
