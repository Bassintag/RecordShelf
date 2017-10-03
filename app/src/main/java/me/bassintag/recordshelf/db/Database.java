package me.bassintag.recordshelf.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.db.object.Artist;
import me.bassintag.recordshelf.db.object.Genre;
import me.bassintag.recordshelf.db.provider.dao.AlbumDao;
import me.bassintag.recordshelf.db.provider.dao.ArtistDao;
import me.bassintag.recordshelf.db.provider.dao.CoverDao;
import me.bassintag.recordshelf.db.provider.dao.GenreDao;
import me.bassintag.recordshelf.db.provider.dao.IAlbumDao;
import me.bassintag.recordshelf.db.provider.dao.IArtistDao;
import me.bassintag.recordshelf.db.provider.dao.ICoverDao;
import me.bassintag.recordshelf.db.provider.dao.IGenreDao;
import me.bassintag.recordshelf.db.schema.IAlbumSchema;
import me.bassintag.recordshelf.db.schema.IArtistSchema;
import me.bassintag.recordshelf.db.schema.ICoverSchema;
import me.bassintag.recordshelf.db.schema.IGenreSchema;

/*
** Created by Antoine on 02/09/2017.
*/
public class Database extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "recordshelf_database.db";
  private static final int DATABASE_VERSION = 4;

  private static final String[] DATABASE_TABLES = {
      IAlbumSchema.ALBUM_TABLE,
      IArtistSchema.ARTIST_TABLE,
      IGenreSchema.GENRE_TABLE,
      ICoverSchema.COVER_TABLE
  };

  private IAlbumDao mAlbumDao;
  private IArtistDao mArtistDao;
  private IGenreDao mGenreDao;
  private ICoverDao mCoverDao;

  public Database(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    mAlbumDao = new AlbumDao(getWritableDatabase());
    mArtistDao = new ArtistDao(getWritableDatabase());
    mGenreDao = new GenreDao(getWritableDatabase());
    mCoverDao = new CoverDao(getWritableDatabase());
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(IAlbumSchema.ALBUM_TABLE_CREATE);
    db.execSQL(IArtistSchema.ARTIST_TABLE_CREATE);
    db.execSQL(IGenreSchema.GENRE_TABLE_CREATE);
    db.execSQL(ICoverSchema.COVER_TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    for (String table : DATABASE_TABLES) {
      db.execSQL("DROP TABLE IF EXISTS " + table);
    }
    onCreate(db);
  }

  public IAlbumDao getAlbumDao() {
    return mAlbumDao;
  }

  public IArtistDao getArtistDao() {
    return mArtistDao;
  }

  public IGenreDao getGenreDao() {
    return mGenreDao;
  }

  public ICoverDao getCoverDao() {
    return mCoverDao;
  }

  // Utils

  public boolean deleteArtistIfUnused(int id, int min) {
    return getAlbumDao().getAllAlbumsByArtistId(id).size() <= min && getArtistDao()
        .deleteArtistById(id);
  }

  public boolean deleteArtistIfUnused(int id) {
    return deleteArtistIfUnused(id, 0);
  }

  public boolean deleteGenreIfUnused(int id, int min) {
    return getAlbumDao().getAllAlbumsByGenreId(id).size() <= min && getGenreDao()
        .deleteGenreById(id);
  }

  public boolean deleteGenreIfUnused(int id) {
    return deleteGenreIfUnused(id, 0);
  }

  public AlbumDescription getAlbumDescriptionByAlbumId(int id) {
    Album album = getAlbumDao().getAlbumById(id);
    if (album == null) {
      return null;
    }
    return getAlbumDescriptionByAlbum(album);
  }

  public AlbumDescription getAlbumDescriptionByAlbum(Album album) {
    Artist artist = getArtistDao().getArtistById(album.getArtistId());
    Genre genre = getGenreDao().getGenreById(album.getGenreId());
    return new AlbumDescription(album, artist, genre);
  }

  public AlbumDescription[] getAlbumDescriptionsByAlbums(Album... albums) {
    AlbumDescription[] result = new AlbumDescription[albums.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = getAlbumDescriptionByAlbum(albums[i]);
    }
    return result;
  }

  public List<AlbumDescription> getAlbumDescriptionsByAlbums(List<Album> albums) {
    List<AlbumDescription> result = new ArrayList<>(albums.size());
    for (Album album : albums) {
      result.add(getAlbumDescriptionByAlbum(album));
    }
    return result;
  }

  public List<AlbumDescription> getAllAlbumDescriptionsSortArtist() {
    return getAllAlbumDescriptions(getAlbumDao().getAllAlbumsSortArtist());
  }

  public List<AlbumDescription> getAllAlbumDescriptionsSortGenre() {
    return getAllAlbumDescriptions(getAlbumDao().getAllAlbumsSortGenre());
  }

  public List<AlbumDescription> getAllAlbumDescriptionsSortReleaseYear() {
    return getAllAlbumDescriptions(getAlbumDao().getAllAlbumsSortReleaseYear());
  }

  public List<AlbumDescription> getAllAlbumDescriptionsSortName() {
    return getAllAlbumDescriptions(getAlbumDao().getAllAlbums());
  }

  private List<AlbumDescription> getAllAlbumDescriptions(List<Album> source) {
    Map<Integer, Artist> artistsCache = new HashMap<>();
    Map<Integer, Genre> genresCache = new HashMap<>();
    List<AlbumDescription> result = new ArrayList<>();
    for (Album album : source) {
      Artist artist = null;
      if (album.getArtistId() >= 0) {
        if (artistsCache.containsKey(album.getArtistId())) {
          artist = artistsCache.get(album.getArtistId());
        } else {
          artist = getArtistDao().getArtistById(album.getArtistId());
          artistsCache.put(album.getArtistId(), artist);
        }
      }
      Genre genre = null;
      if (album.getGenreId() >= 0) {
        if (genresCache.containsKey(album.getGenreId())) {
          genre = genresCache.get(album.getGenreId());
        } else {
          genre = getGenreDao().getGenreById(album.getGenreId());
          genresCache.put(album.getGenreId(), genre);
        }
      }
      AlbumDescription description = new AlbumDescription(album, artist, genre);
      result.add(description);
    }
    return result;
  }
}
