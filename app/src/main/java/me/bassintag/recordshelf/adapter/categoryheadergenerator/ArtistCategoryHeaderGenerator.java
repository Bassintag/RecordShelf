package me.bassintag.recordshelf.adapter.categoryheadergenerator;

import android.content.Context;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 12/09/2017.
*/
public class ArtistCategoryHeaderGenerator implements ICategoryHeaderGenerator {

  private final String mDefaultString;

  public ArtistCategoryHeaderGenerator(Context context) {
    mDefaultString = context.getResources().getString(R.string.unknown_artist_placeholder);
  }

  @Override
  public String getHeaderText(AlbumDescription previous, AlbumDescription current) {
    if (previous != null && (previous.getArtist() == current.getArtist()
        || (previous.getArtist() != null && current.getArtist() != null
        && previous.getArtist().getId() == current.getArtist().getId()))) {
      return null;
    }
    if (current.getArtist() != null) {
      return current.getArtist().getName();
    }
    return mDefaultString;
  }
}
