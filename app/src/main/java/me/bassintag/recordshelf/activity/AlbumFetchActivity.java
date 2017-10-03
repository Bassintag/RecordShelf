package me.bassintag.recordshelf.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.activity.resultcode.AlbumResultCode;
import me.bassintag.recordshelf.adapter.AlbumRecyclerViewAdapter;
import me.bassintag.recordshelf.adapter.ICoverProvider;
import me.bassintag.recordshelf.db.object.WebAlbumDescription;
import me.bassintag.recordshelf.task.IRetrieveAlbumCoverListener;
import me.bassintag.recordshelf.task.IRetrieveAlbumsListener;
import me.bassintag.recordshelf.task.RetrieveAlbumCoverTask;
import me.bassintag.recordshelf.task.RetrieveAlbumCoverTask.RetrievedCover;
import me.bassintag.recordshelf.task.RetrieveAlbumsByTitleTask;
import me.bassintag.recordshelf.task.RetrieveAlbumsTask;
import me.bassintag.recordshelf.viewholder.IAlbumViewHolderListener;

/*
** Created by Antoine on 07/09/2017.
*/
public class AlbumFetchActivity extends DatabaseActivity implements IRetrieveAlbumsListener,
    IAlbumViewHolderListener<WebAlbumDescription>, ICoverProvider<WebAlbumDescription> {

  private AlbumRecyclerViewAdapter<WebAlbumDescription> mAdapter;
  private List<RetrieveAlbumCoverTask> mCoverTasks;
  private RetrieveAlbumsTask mTask;
  private TextView mMessage;

  private Map<String, Bitmap> covers;

  @Override
  protected void onCreateAfter(Bundle savedInstanceState) {
    covers = new HashMap<>();
    mCoverTasks = new ArrayList<>();
    setContentView(R.layout.activity_album_fetch);
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_result_recycler_view);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    mAdapter = new AlbumRecyclerViewAdapter<>(this, new ArrayList<WebAlbumDescription>(), this,
        null);
    mMessage = (TextView) findViewById(R.id.search_placeholder_message);
    recyclerView.setAdapter(mAdapter);
    final EditText searchField = (EditText) findViewById(R.id.search_input);
    searchField.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        String text = searchField.getText().toString();
        if (!hasFocus && text.length() > 0) {
          endTasks();
          mMessage.setText(R.string.loading);
          mTask = new RetrieveAlbumsByTitleTask(AlbumFetchActivity.this);
          recyclerView.setItemViewCacheSize(0);
          mAdapter.clear();
          mTask.execute(text);
          InputMethodManager inputManager = (InputMethodManager) getSystemService(
              Context.INPUT_METHOD_SERVICE);
          inputManager.toggleSoftInput(0, 0);
        }
      }
    });
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void endTasks() {
    if (mTask != null && mTask.getStatus() == Status.RUNNING) {
      mTask.cancel(true);
      mTask = null;
    }
    for (RetrieveAlbumCoverTask task : mCoverTasks) {
      if (task.getStatus() == Status.RUNNING) {
        task.cancel(true);
      }
    }
    mCoverTasks.clear();
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

  @Override
  public void onAlbumRetrieved(final WebAlbumDescription albumDescription) {
    final int index = mAdapter.addAlbum(albumDescription);
    RetrieveAlbumCoverTask task = new RetrieveAlbumCoverTask(new IRetrieveAlbumCoverListener() {
      @Override
      public void onAlbumCoverRetrieved(RetrievedCover retrievedCover) {
        covers.put(albumDescription.getWebId(), retrievedCover.cover);
        mAdapter.notifyItemChanged(index);
      }
    });
    task.execute(albumDescription);
    mMessage.setText("");
  }

  @Override
  public void onTaskError() {
    mMessage.setText(R.string.error_occurred);
  }

  @Override
  public void onTaskEnd(List<WebAlbumDescription> albumList) {
    if (albumList.size() == 0) {
      mMessage.setText(R.string.no_result_found);
    }
  }

  @Override
  public void onAlbumViewClicked(WebAlbumDescription album, int index) {
    long id = album.insert(mDb);
    setResult(AlbumResultCode.RESULT_ALBUM_CREATE);
    Bitmap cover = covers.get(album.getWebId());
    if (cover != null) {
      mDb.getCoverDao().setAlbumCoverByAlbumId(id, cover);
    }
    finish();
  }

  @Override
  public void onDestroyAfter() {
    endTasks();
  }

  @Override
  public Bitmap getCoverForAlbum(WebAlbumDescription albumDescription) {
    return covers.get(albumDescription.getWebId());
  }
}
