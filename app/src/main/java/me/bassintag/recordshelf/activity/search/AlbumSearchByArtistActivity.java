package me.bassintag.recordshelf.activity.search;

import java.util.ArrayList;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.db.object.Artist;

/*
** Created by Antoine on 11/09/2017.
*/
public class AlbumSearchByArtistActivity extends AlbumSearchActivity {

  @Override
  protected List<AlbumDescription> getAlbums(String query) {
    List<Album> albums;
    if (query == null) {
      albums = mDb.getAlbumDao().getAllAlbumsWithoutArtist();
    } else {
      Artist artist = mDb.getArtistDao().getArtistByName(query);
      if (artist == null) {
        return new ArrayList<>();
      }
      albums = mDb.getAlbumDao().getAllAlbumsByArtistId(artist.getId());
    }
    return mDb.getAlbumDescriptionsByAlbums(albums);
  }

  @Override
  protected List<String> getAutocompleteList() {
    List<String> result = new ArrayList<>();
    for (Artist artist : mDb.getArtistDao().getAllArtists()) {
      result.add(artist.getName());
    }
    return result;
  }

  @Override
  protected int getSearchLabelResourceId() {
    return R.string.artist_label;
  }

  @Override
  protected int getSearchInputHintResourceId() {
    return R.string.unknown_artist_placeholder;
  }
}
