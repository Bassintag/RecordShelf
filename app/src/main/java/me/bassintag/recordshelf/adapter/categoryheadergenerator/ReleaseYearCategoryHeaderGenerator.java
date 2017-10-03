package me.bassintag.recordshelf.adapter.categoryheadergenerator;

import android.content.Context;
import java.util.Objects;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 12/09/2017.
*/
public class ReleaseYearCategoryHeaderGenerator implements ICategoryHeaderGenerator {

  private final String mDefaultString;

  public ReleaseYearCategoryHeaderGenerator(Context context) {
    mDefaultString = context.getResources().getString(R.string.unknown_release_year_placeholder);
  }

  @Override
  public String getHeaderText(AlbumDescription previous, AlbumDescription current) {
    Integer prevYear = null;
    Integer currentYear = current.getAlbum().getReleaseYear();
    if (previous != null) {
      prevYear = previous.getAlbum().getReleaseYear();
    }
    if (!Objects.equals(prevYear, currentYear)) {
      if (currentYear == -1) {
        return mDefaultString;
      }
      return String.valueOf(currentYear);
    }
    return null;
  }
}
