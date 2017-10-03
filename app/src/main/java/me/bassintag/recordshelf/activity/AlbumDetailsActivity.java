package me.bassintag.recordshelf.activity;

import static me.bassintag.recordshelf.activity.resultcode.AlbumRequestCode.EDIT_ALBUM_REQUEST;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.activity.resultcode.AlbumResultCode;
import me.bassintag.recordshelf.activity.search.AlbumSearchActivity;
import me.bassintag.recordshelf.activity.search.AlbumSearchByArtistActivity;
import me.bassintag.recordshelf.activity.search.AlbumSearchByGenreActivity;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.Artist;
import me.bassintag.recordshelf.db.object.Genre;

/*
** Created by Antoine on 03/09/2017.
*/
public class AlbumDetailsActivity extends DatabaseActivity {

  private Album mAlbum;
  private Artist mArtist;
  private Genre mGenre;

  @Override
  protected void onCreateAfter(Bundle savedInstanceState) {
    setContentView(R.layout.activity_album_details);
    ActionBar bar = getSupportActionBar();
    if (bar != null) {
      bar.hide();
    }
    if (Build.VERSION.SDK_INT >= 21) {
      getWindow().getDecorView().setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
      getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    initButtons();
    int albumId = getIntent().getIntExtra("album_id", -1);
    if (albumId <= 0 || (mAlbum = mDb.getAlbumDao().getAlbumById(albumId)) == null) {
      finish();
      return;
    }
    initAlbumInfo();
  }

  protected void initAlbumInfo() {
    ((TextView) findViewById(R.id.album_name)).setText(mAlbum.getName());
    if (mAlbum.getReleaseYear() > 0) {
      ((TextView) findViewById(R.id.album_release_year))
          .setText(String.valueOf(mAlbum.getReleaseYear()));
    }
    Bitmap cover = mDb.getCoverDao().getCoverByAlbumId(mAlbum.getId());
    if (cover != null) {
      ImageView coverView = (ImageView) findViewById(R.id.album_cover);
      coverView.setImageBitmap(cover);
      coverView.setPadding(0, 0, 0, 0);
    }
    mArtist = mDb.getArtistDao().getArtistById(mAlbum.getArtistId());
    if (mArtist != null) {
      ((TextView) findViewById(R.id.artist_name)).setText(mArtist.getName());
    }
    mGenre = mDb.getGenreDao().getGenreById(mAlbum.getGenreId());
    if (mGenre != null) {
      ((TextView) findViewById(R.id.genre_name)).setText(mGenre.getName());
    }
  }

  protected void initButtons() {
    ImageButton button = (ImageButton) findViewById(R.id.back_button);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    findViewById(R.id.button_edit).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AlbumDetailsActivity.this, AlbumEditActivity.class);
        intent.putExtra("album_id", mAlbum.getId());
        startActivityForResult(intent, EDIT_ALBUM_REQUEST);
      }
    });
    findViewById(R.id.button_delete).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        setResult(AlbumResultCode.RESULT_ALBUM_DELETE);
        mDb.getAlbumDao().deleteAlbum(mAlbum.getId());
        mDb.deleteArtistIfUnused(mAlbum.getArtistId());
        mDb.deleteGenreIfUnused(mAlbum.getGenreId());
        finish();
      }
    });
    initSearchButtons();
  }

  protected void initSearchButtons() {
    findViewById(R.id.album_same_artist_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String query = mArtist == null ? null : mArtist.getName();
        startSearchByIntent(AlbumSearchByArtistActivity.class, query);
      }
    });
    findViewById(R.id.album_same_genre_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String query = mGenre == null ? null : mGenre.getName();
        startSearchByIntent(AlbumSearchByGenreActivity.class, query);
      }
    });
  }

  private void startSearchByIntent(Class<? extends AlbumSearchActivity> clazz, String query) {
    Intent intent = new Intent(this, clazz);
    intent.putExtra("album_id", mAlbum.getId());
    if (query != null) {
      intent.putExtra("query", query);
    }
    startActivityForResult(intent, EDIT_ALBUM_REQUEST);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.no_change, R.anim.slide_down);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == EDIT_ALBUM_REQUEST) {
      if (resultCode == AlbumResultCode.RESULT_ALBUM_EDIT) {
        mAlbum = mDb.getAlbumDao().getAlbumById(mAlbum.getId());
        setResult(AlbumResultCode.RESULT_ALBUM_EDIT);
        initAlbumInfo();
      }
    }
  }
}
