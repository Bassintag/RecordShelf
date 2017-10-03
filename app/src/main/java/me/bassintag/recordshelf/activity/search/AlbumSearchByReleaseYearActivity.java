package me.bassintag.recordshelf.activity.search;

import android.text.InputType;
import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 14/09/2017.
*/
public class AlbumSearchByReleaseYearActivity extends AlbumSearchActivity {

  @Override
  protected List<AlbumDescription> getAlbums(String query) {
    List<Album> albums;
    if (query == null) {
      return new ArrayList<>();
    } else {
      albums = mDb.getAlbumDao().getAllAlbumsByReleaseYear(Integer.parseInt(query));
    }
    return mDb.getAlbumDescriptionsByAlbums(albums);
  }

  @Override
  protected int getInputType() {
    return InputType.TYPE_CLASS_NUMBER;
  }

  @Override
  protected List<String> getAutocompleteList() {
    List<String> result = new ArrayList<>();
    for (Album album : mDb.getAlbumDao().getAllAlbums()) {
      result.add(String.valueOf(album.getReleaseYear()));
    }
    return result;
  }

  @Override
  protected int getSearchLabelResourceId() {
    return R.string.release_year_label;
  }

  @Override
  protected int getSearchInputHintResourceId() {
    return R.string.unknown_release_year_placeholder;
  }
}
