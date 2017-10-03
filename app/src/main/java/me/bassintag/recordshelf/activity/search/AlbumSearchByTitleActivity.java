package me.bassintag.recordshelf.activity.search;

import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 14/09/2017.
*/
public class AlbumSearchByTitleActivity extends AlbumSearchActivity {

  @Override
  protected List<AlbumDescription> getAlbums(String query) {
    List<Album> albums;
    if (query == null) {
      return new ArrayList<>();
    } else {
      albums = mDb.getAlbumDao().getAllAlbumsByTitle(query);
    }
    return mDb.getAlbumDescriptionsByAlbums(albums);
  }

  @Override
  protected List<String> getAutocompleteList() {
    List<String> result = new ArrayList<>();
    for (Album album : mDb.getAlbumDao().getAllAlbums()) {
      result.add(album.getName());
    }
    return result;
  }

  @Override
  protected int getSearchLabelResourceId() {
    return R.string.album_title_label;
  }

  @Override
  protected int getSearchInputHintResourceId() {
    return R.string.unknown_album_title_placeholder;
  }
}
