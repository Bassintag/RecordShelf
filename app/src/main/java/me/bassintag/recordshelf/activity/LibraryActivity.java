package me.bassintag.recordshelf.activity;

import static me.bassintag.recordshelf.activity.resultcode.AlbumRequestCode.ADD_ALBUM_REQUEST;
import static me.bassintag.recordshelf.activity.resultcode.AlbumRequestCode.EDIT_ALBUM_REQUEST;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.activity.resultcode.AlbumResultCode;
import me.bassintag.recordshelf.activity.search.AlbumSearchByArtistActivity;
import me.bassintag.recordshelf.activity.search.AlbumSearchByGenreActivity;
import me.bassintag.recordshelf.activity.search.AlbumSearchByReleaseYearActivity;
import me.bassintag.recordshelf.activity.search.AlbumSearchByTitleActivity;
import me.bassintag.recordshelf.activity.sortmode.EAlbumSortMode;
import me.bassintag.recordshelf.adapter.AlbumRecyclerViewAdapter;
import me.bassintag.recordshelf.adapter.DbCoverProvider;
import me.bassintag.recordshelf.adapter.categoryheadergenerator.AlbumNameCategoryHeaderGenerator;
import me.bassintag.recordshelf.adapter.categoryheadergenerator.ArtistCategoryHeaderGenerator;
import me.bassintag.recordshelf.adapter.categoryheadergenerator.GenreCategoryHeaderGenerator;
import me.bassintag.recordshelf.adapter.categoryheadergenerator.ICategoryHeaderGenerator;
import me.bassintag.recordshelf.adapter.categoryheadergenerator.ReleaseYearCategoryHeaderGenerator;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.viewholder.IAlbumViewHolderListener;

public class LibraryActivity extends DatabaseActivity implements
    IAlbumViewHolderListener<AlbumDescription>, OnNavigationItemSelectedListener {

  private AlbumRecyclerViewAdapter<AlbumDescription> mAdapter;

  private EAlbumSortMode mSortMode;

  private DrawerLayout mDrawer;

  private NavigationView mNav;

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setContentView(R.layout.activity_library);
    mSortMode = EAlbumSortMode.ARTIST_NAME;
    initAlbumView();
    View addAlbumButton = findViewById(R.id.add_album_button);
    addAlbumButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(LibraryActivity.this, AlbumEditActivity.class),
            ADD_ALBUM_REQUEST);
      }
    });
    View fetchAlbumButton = findViewById(R.id.fetch_album_button);
    fetchAlbumButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(LibraryActivity.this, AlbumFetchActivity.class),
            ADD_ALBUM_REQUEST);
      }
    });
    View scanBarcodeButton = findViewById(R.id.barcode_album_button);
    scanBarcodeButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(LibraryActivity.this, BarcodeScannerActivity.class),
            ADD_ALBUM_REQUEST);
      }
    });
    mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    mNav = (NavigationView) findViewById(R.id.navigation_view);
    mNav.setNavigationItemSelectedListener(this);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void initAlbumView() {
    RecyclerView mAlbumsView = (RecyclerView) findViewById(R.id.library_recycler_view);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mAlbumsView.setLayoutManager(layoutManager);
    mAlbumsView.setHasFixedSize(true);
    mAdapter = new AlbumRecyclerViewAdapter<>(this, getAlbumDescriptions(),
        new DbCoverProvider(mDb),
        new ArtistCategoryHeaderGenerator(this));
    mAlbumsView.setAdapter(mAdapter);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ADD_ALBUM_REQUEST) {
      if (resultCode == AlbumResultCode.RESULT_ALBUM_EDIT
          || resultCode == AlbumResultCode.RESULT_ALBUM_CREATE
          || resultCode == AlbumResultCode.RESULT_ALBUM_DELETE) {
        reloadAdapter();
      }
    }
  }

  @Override
  public void onAlbumViewClicked(AlbumDescription albumDescription, int index) {
    Intent intent = new Intent(this, AlbumDetailsActivity.class);
    Album album = albumDescription.getAlbum();
    intent.putExtra("album_id", album.getId());
    startActivityForResult(intent, ADD_ALBUM_REQUEST);
    overridePendingTransition(R.anim.slide_up, R.anim.no_change);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.album_categories_filter, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    item.setChecked(true);
    switch (item.getItemId()) {
      case R.id.menu_filter_album_name:
        mSortMode = EAlbumSortMode.ALBUM_NAME;
        break;
      case R.id.menu_filter_artist_name:
        mSortMode = EAlbumSortMode.ARTIST_NAME;
        break;
      case R.id.menu_filter_genre_name:
        mSortMode = EAlbumSortMode.GENRE_NAME;
        break;
      case R.id.menu_filter_release_year:
        mSortMode = EAlbumSortMode.RELEASE_YEAR;
        break;
      case android.R.id.home:
        if (mDrawer.isDrawerOpen(mNav)) {
          mDrawer.closeDrawer(mNav);
        } else {
          mDrawer.openDrawer(mNav);
        }
        return true;
      default:
        break;
    }
    reloadAdapter();
    return true;
  }

  private List<AlbumDescription> getAlbumDescriptions() {
    switch (mSortMode) {
      case ALBUM_NAME:
        return mDb.getAllAlbumDescriptionsSortName();
      case ARTIST_NAME:
        return mDb.getAllAlbumDescriptionsSortArtist();
      case GENRE_NAME:
        return mDb.getAllAlbumDescriptionsSortGenre();
      case RELEASE_YEAR:
        return mDb.getAllAlbumDescriptionsSortReleaseYear();
      default:
        return null;
    }
  }

  private ICategoryHeaderGenerator getHeaderGenerator() {
    switch (mSortMode) {
      case ALBUM_NAME:
        return new AlbumNameCategoryHeaderGenerator();
      case ARTIST_NAME:
        return new ArtistCategoryHeaderGenerator(this);
      case GENRE_NAME:
        return new GenreCategoryHeaderGenerator(this);
      case RELEASE_YEAR:
        return new ReleaseYearCategoryHeaderGenerator(this);
      default:
        return null;
    }
  }

  private void reloadAdapter() {
    mAdapter.setHeaderGenerator(getHeaderGenerator());
    mAdapter.setAlbums(getAlbumDescriptions());
  }

  @Override
  public boolean onSupportNavigateUp() {
        if (mDrawer.isDrawerOpen(mNav)) {
          mDrawer.closeDrawer(mNav);
        } else {
          mDrawer.openDrawer(mNav);
        }
    return true;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_search_by_title:
        startActivityForResult(new Intent(LibraryActivity.this, AlbumSearchByTitleActivity.class),
            EDIT_ALBUM_REQUEST);
        break;
      case R.id.menu_search_by_release_year:
        startActivityForResult(new Intent(LibraryActivity.this, AlbumSearchByReleaseYearActivity.class),
            EDIT_ALBUM_REQUEST);
        break;
      case R.id.menu_search_by_artist:
        startActivityForResult(new Intent(LibraryActivity.this, AlbumSearchByArtistActivity.class),
            EDIT_ALBUM_REQUEST);
        break;
      case R.id.menu_search_by_genre:
        startActivityForResult(new Intent(LibraryActivity.this, AlbumSearchByGenreActivity.class),
            EDIT_ALBUM_REQUEST);
        break;
      case R.id.menu_add_manually:
        startActivityForResult(new Intent(LibraryActivity.this, AlbumEditActivity.class),
            ADD_ALBUM_REQUEST);
        break;
      case R.id.menu_add_from_web:
        startActivityForResult(new Intent(LibraryActivity.this, AlbumFetchActivity.class),
            ADD_ALBUM_REQUEST);
        break;
      case R.id.menu_add_from_barcode:
        startActivityForResult(new Intent(LibraryActivity.this, BarcodeScannerActivity.class),
            ADD_ALBUM_REQUEST);
        break;
      default:
        return false;
    }
    return true;
  }
}
