package me.bassintag.recordshelf.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.activity.resultcode.AlbumResultCode;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.Artist;
import me.bassintag.recordshelf.db.object.Genre;
import me.bassintag.recordshelf.fragment.IImageChooserDialogFragmentListener;
import me.bassintag.recordshelf.fragment.ImageChooserDialogFragment;
import me.bassintag.recordshelf.utils.BitmapUtils;


/*
** Created by Antoine on 04/09/2017.
*/
public class AlbumEditActivity extends DatabaseActivity implements
    IImageChooserDialogFragmentListener {

  private Album mAlbum;
  private Artist mArtist;
  private Genre mGenre;

  private Bitmap mCover = null;

  private ImageView mCoverImageView;
  private EditText mAlbumNameEditText;
  private AutoCompleteTextView mArtistNameEditText;
  private EditText mReleaseYearEditText;
  private AutoCompleteTextView mGenreNameEditText;

  private static final int PICK_IMAGE_REQUEST = 1;

  private void loadAlbumFromIntent() {
    int albumId = getIntent().getIntExtra("album_id", -1);
    if (albumId > 0) {
      mAlbum = mDb.getAlbumDao().getAlbumById(albumId);
      if (mAlbum != null) {
        mArtist = mDb.getArtistDao().getArtistById(mAlbum.getArtistId());
        mGenre = mDb.getGenreDao().getGenreById(mAlbum.getGenreId());
      }
    }
  }

  private void refreshCoverImageView() {
    if (mCover != null) {
      mCoverImageView.setImageBitmap(mCover);
      mCoverImageView.setPadding(0, 0, 0, 0);
    } else if (mAlbum != null) {
      Bitmap cover = mDb.getCoverDao().getCoverByAlbumId(mAlbum.getId());
      if (cover != null) {
        mCoverImageView.setImageBitmap(cover);
        mCoverImageView.setPadding(0, 0, 0, 0);
      }
    }
  }

  private void initTextInputs() {
    mAlbumNameEditText = (EditText) findViewById(R.id.album_name_edit_text);
    mArtistNameEditText = (AutoCompleteTextView) findViewById(R.id.artist_name_edit_text);
    List<Artist> artists = mDb.getArtistDao().getAllArtists();
    String[] artistNames = new String[artists.size()];
    for (int i = 0; i < artists.size(); i++) {
      artistNames[i] = artists.get(i).getName();
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_dropdown_item_1line, artistNames);
    mArtistNameEditText.setAdapter(adapter);
    mReleaseYearEditText = (EditText) findViewById(R.id.release_year_edit_text);
    mGenreNameEditText = (AutoCompleteTextView) findViewById(R.id.genre_edit_text);
    List<Genre> genres = mDb.getGenreDao().getAllGenres();
    String[] genreNames = new String[genres.size()];
    for (int i = 0; i < genres.size(); i++) {
      genreNames[i] = genres.get(i).getName();
    }
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_dropdown_item_1line, genreNames);
    mGenreNameEditText.setAdapter(adapter);
    mCoverImageView = (ImageView) findViewById(R.id.album_cover);
    refreshCoverImageView();
    loadTextInputsFromAlbum();
  }

  private void loadTextInputsFromAlbum() {
    if (mAlbum != null) {
      mAlbumNameEditText.setText(mAlbum.getName());
      if (mArtist != null) {
        mArtistNameEditText.setText(mArtist.getName());
      }
      if (mAlbum.getReleaseYear() > 0) {
        mReleaseYearEditText.setText(String.valueOf(mAlbum.getReleaseYear()));
      }
      if (mGenre != null) {
        mGenreNameEditText.setText(mGenre.getName());
      }
    }
  }

  private void onSave() {
    String albumName = mAlbumNameEditText.getText().toString();
    if (albumName.length() > 0) {
      boolean update = !(mAlbum == null);
      if (!update) {
        mAlbum = new Album(albumName);
      }
      mAlbum.setName(albumName);
      String sYear = mReleaseYearEditText.getText().toString();
      if (sYear.length() > 0) {
        mAlbum.setReleaseYear(Integer.valueOf(sYear));
      }
      if (mCover != null) {
        mDb.getCoverDao().setAlbumCoverByAlbumId(mAlbum.getId(), mCover);
      }
      String artistName = mArtistNameEditText.getText().toString();
      if (artistName.length() > 0) {
        mArtist = mDb.getArtistDao().getArtistByName(artistName);
        if (mArtist == null && mDb.getArtistDao().addArtist(new Artist(artistName))) {
          mArtist = mDb.getArtistDao().getArtistByName(artistName);
          mDb.deleteArtistIfUnused(mAlbum.getArtistId(), 1);
        }
        mAlbum.setArtistId(mArtist.getId());
      }
      String genreName = mGenreNameEditText.getText().toString();
      if (genreName.length() > 0) {
        mGenre = mDb.getGenreDao().getGenreByName(genreName);
        if (mGenre == null && mDb.getGenreDao().addGenre(new Genre(genreName))) {
          mGenre = mDb.getGenreDao().getGenreByName(genreName);
          mDb.deleteGenreIfUnused(mAlbum.getGenreId(), 1);
        }
        mAlbum.setGenreId(mGenre.getId());
      }
      if (!update) {
        mDb.getAlbumDao().addAlbum(mAlbum);
      } else {
        mDb.getAlbumDao().updateAlbum(mAlbum);
      }
      setResult(update ? AlbumResultCode.RESULT_ALBUM_EDIT : AlbumResultCode.RESULT_ALBUM_CREATE);
    }
    finish();
  }

  @Override
  protected void onCreateAfter(Bundle savedInstanceState) {
    setContentView(R.layout.activity_album_edit);
    ActionBar bar = getSupportActionBar();
    if (bar != null) {
      bar.setDisplayHomeAsUpEnabled(true);
    }
    loadAlbumFromIntent();
    initTextInputs();
    findViewById(R.id.button_save).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onSave();
      }
    });
    findViewById(R.id.album_cover).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        ImageChooserDialogFragment fragment = new ImageChooserDialogFragment();
        fragment.show(getSupportFragmentManager(), "image_chooser");
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void onImageChosen(Bitmap image) {
    image = BitmapUtils.resizeToSquare(image);
    mCover = image;
    refreshCoverImageView();
  }

  @Override
  public int getImagePickerRequestCode() {
    return PICK_IMAGE_REQUEST;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST) {
      if (resultCode == RESULT_OK) {
        if (data != null) {
          try {
            InputStream inputStream = getContentResolver().openInputStream(data.getData());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
              onImageChosen(bitmap);
            }
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
