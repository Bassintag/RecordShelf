package me.bassintag.recordshelf.adapter.categoryheadergenerator;

import android.content.Context;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 12/09/2017.
*/
public class GenreCategoryHeaderGenerator implements ICategoryHeaderGenerator {

  private final String mDefaultString;

  public GenreCategoryHeaderGenerator(Context context) {
    mDefaultString = context.getResources().getString(R.string.unknown_genre_placeholder);
  }

  @Override
  public String getHeaderText(AlbumDescription previous, AlbumDescription current) {
    if (previous != null && (previous.getGenre() == current.getGenre()
        || (previous.getGenre() != null && current.getGenre() != null
        && previous.getGenre().getId() == current.getGenre().getId()))) {
      return null;
    }
    if (current.getGenre() != null) {
      return current.getGenre().getName();
    }
    return mDefaultString;
  }
}
