package me.bassintag.recordshelf.adapter.categoryheadergenerator;

import android.content.Context;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 12/09/2017.
*/
public class AlbumNameCategoryHeaderGenerator implements ICategoryHeaderGenerator {


  @Override
  public String getHeaderText(AlbumDescription previous, AlbumDescription current) {
    Character prevChar = null;
    Character currentChar = current.getAlbum().getName().charAt(0);
    if (previous != null) {
      prevChar = previous.getAlbum().getName().charAt(0);
    }
    if (!currentChar.equals(prevChar)) {
      return String.valueOf(currentChar);
    }
    return null;
  }
}
