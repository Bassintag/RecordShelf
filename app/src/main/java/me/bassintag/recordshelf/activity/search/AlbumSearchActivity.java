package me.bassintag.recordshelf.activity.search;

import static me.bassintag.recordshelf.activity.resultcode.AlbumRequestCode.ADD_ALBUM_REQUEST;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.activity.AlbumDetailsActivity;
import me.bassintag.recordshelf.activity.DatabaseActivity;
import me.bassintag.recordshelf.activity.resultcode.AlbumResultCode;
import me.bassintag.recordshelf.adapter.AlbumRecyclerViewAdapter;
import me.bassintag.recordshelf.adapter.DbCoverProvider;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.viewholder.IAlbumViewHolderListener;

/*
** Created by Antoine on 11/09/2017.
*/
public abstract class AlbumSearchActivity extends DatabaseActivity implements
    IAlbumViewHolderListener<AlbumDescription> {

  private AlbumRecyclerViewAdapter<AlbumDescription> mAdapter;

  private AutoCompleteTextView mSearchInput;

  private String mQuery;

  @Override
  protected final void onCreateAfter(Bundle savedInstance) {
    setContentView(R.layout.activity_album_search);
    mQuery = null;
    Intent intent = getIntent();
    mSearchInput = (AutoCompleteTextView) findViewById(R.id.search_input);
    if (intent != null) {
      mQuery = intent.getStringExtra("query");
      mSearchInput.setText(mQuery);
    }
    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.search_result_recycler_view);
    mAdapter = new AlbumRecyclerViewAdapter<>(this, getAlbums(mQuery), new DbCoverProvider(mDb),
        null);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setAdapter(mAdapter);
    TextView mSearchLabel = (TextView) findViewById(R.id.search_label);
    mSearchLabel.setText(getSearchLabelResourceId());
    initSearch();
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void initAutoComplete() {
    List<String> list = getAutocompleteList();
    if (list == null) {
      return;
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_dropdown_item_1line, list);
    mSearchInput.setAdapter(adapter);
  }

  protected int getInputType() {
    return InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
  }

  private void initSearch() {
    mSearchInput.setInputType(getInputType());
    initAutoComplete();
    mSearchInput.setHint(getSearchInputHintResourceId());
    mSearchInput.setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
          if (mSearchInput.length() > 0) {
            mQuery = mSearchInput.getText().toString().trim();
          } else {
            mQuery = null;
          }
          reloadAdapter();
          InputMethodManager imm = (InputMethodManager) getSystemService(
              Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
          return true;
        }
        return false;
      }
    });
  }

  private void reloadAdapter() {
    mAdapter.setAlbums(getAlbums(mQuery));
  }

  protected abstract List<AlbumDescription> getAlbums(String query);

  protected abstract List<String> getAutocompleteList();

  protected abstract int getSearchLabelResourceId();

  protected abstract int getSearchInputHintResourceId();

  @Override
  protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ADD_ALBUM_REQUEST) {
      if (resultCode == AlbumResultCode.RESULT_ALBUM_EDIT
          || resultCode == AlbumResultCode.RESULT_ALBUM_CREATE
          || resultCode == AlbumResultCode.RESULT_ALBUM_DELETE) {
        mAdapter.setAlbums(mDb.getAllAlbumDescriptionsSortArtist());
      }
    }
  }

  @Override
  public final void onAlbumViewClicked(AlbumDescription albumDescription, int index) {
    Intent intent = new Intent(this, AlbumDetailsActivity.class);
    Album album = albumDescription.getAlbum();
    intent.putExtra("album_id", album.getId());
    startActivityForResult(intent, ADD_ALBUM_REQUEST);
    overridePendingTransition(R.anim.slide_up, R.anim.no_change);
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
}
