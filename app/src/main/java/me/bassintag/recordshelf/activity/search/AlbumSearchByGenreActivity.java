package me.bassintag.recordshelf.activity.search;

import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.db.object.Genre;

/*
** Created by Antoine on 11/09/2017.
*/
public class AlbumSearchByGenreActivity extends AlbumSearchActivity {

  @Override
  protected List<AlbumDescription> getAlbums(String query) {
    List<Album> albums;
    if (query == null) {
      albums = mDb.getAlbumDao().getAllAlbumsWithoutGenre();
    } else {
      Genre genre = mDb.getGenreDao().getGenreByName(query);
      if (genre == null) {
        return new ArrayList<>();
      }
      albums = mDb.getAlbumDao().getAllAlbumsByGenreId(genre.getId());
    }
    return mDb.getAlbumDescriptionsByAlbums(albums);
  }

  @Override
  protected List<String> getAutocompleteList() {
    List<String> result = new ArrayList<>();
    for (Genre genre : mDb.getGenreDao().getAllGenres()) {
      result.add(genre.getName());
    }
    return result;
  }

  @Override
  protected int getSearchLabelResourceId() {
    return R.string.genre_label;
  }

  @Override
  protected int getSearchInputHintResourceId() {
    return R.string.unknown_genre_placeholder;
  }
}
