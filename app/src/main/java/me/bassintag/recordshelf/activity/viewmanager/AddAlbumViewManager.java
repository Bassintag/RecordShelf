package me.bassintag.recordshelf.activity.viewmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.Database;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 10/09/2017.
*/
public class AddAlbumViewManager implements OnClickListener, AnimationListener {

  private final ViewGroup mRoot;

  private final ImageView mCoverView;

  private final LayoutParams mDefaultLayout;

  private final TextView mAlbumNameView;
  private final TextView mArtistNameView;
  private final TextView mReleaseYearView;

  private final View mAddButtonContainer;

  private final Database mDb;

  private AlbumDescription mDescription;

  private OnClickListener mListener;

  private boolean mClicked;

  private Animation mAnimation;

  private Bitmap mCover;

  public AddAlbumViewManager(ViewGroup root, Database db, Context context) {
    mRoot = root;
    mCoverView = (ImageView) root.findViewById(R.id.album_card_cover);
    mDb = db;
    mDefaultLayout = mCoverView.getLayoutParams();
    mAlbumNameView = (TextView) root.findViewById(R.id.album_card_name);
    mArtistNameView = (TextView) root.findViewById(R.id.album_card_artist);
    mReleaseYearView = (TextView) root.findViewById(R.id.album_card_release_year);
    mAddButtonContainer = root.findViewById(R.id.add_album_button_container);
    mAnimation = AnimationUtils.loadAnimation(context, R.anim.collapse_x);
    View mAddButton = root.findViewById(R.id.add_album_button);
    mAddButton.setOnClickListener(this);
    resetView();
  }

  private void resetView() {
    mCoverView.setLayoutParams(mDefaultLayout);
    mCoverView.setImageResource(R.drawable.ic_album_inverse_24dp);
    mAlbumNameView.setText(R.string.unknown_album_title_placeholder);
    mArtistNameView.setText(R.string.unknown_artist_placeholder);
    mReleaseYearView.setVisibility(View.GONE);
    mReleaseYearView.setText("");
    mAddButtonContainer.setVisibility(View.VISIBLE);
    mClicked = false;
  }

  public void setFromAlbumDescription(AlbumDescription description, Bitmap cover) {
    resetView();
    mDescription = description;
    mAlbumNameView.setText(description.getAlbum().getName());
    if (cover != null) {
      mCover = cover;
      mCoverView.setPadding(0, 0, 0, 0);
      mCoverView.setImageBitmap(cover);
    }
    if (description.getAlbum().getReleaseYear() > 0) {
      mReleaseYearView.setVisibility(View.VISIBLE);
      mReleaseYearView.setText(String.valueOf(description.getAlbum().getReleaseYear()));
    }
    if (description.getArtist() != null) {
      mArtistNameView.setText(description.getArtist().getName());
    }
  }

  public void hide() {
    mRoot.setVisibility(View.GONE);
  }

  public void show() {
    mRoot.setVisibility(View.VISIBLE);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.mListener = listener;
  }

  @Override
  public void onClick(View v) {
    if (mClicked) {
      return;
    }
    mClicked = true;
    long id = mDescription.insert(mDb);
    if (mCover != null) {
      mDb.getCoverDao().setAlbumCoverByAlbumId(id, mCover);
    }
    mAddButtonContainer.startAnimation(mAnimation);
    mAnimation.setAnimationListener(this);
    if (mListener != null) {
      mListener.onClick(v);
    }
  }

  @Override
  public void onAnimationStart(Animation animation) {

  }

  @Override
  public void onAnimationEnd(Animation animation) {
    mAddButtonContainer.setVisibility(View.GONE);
  }

  @Override
  public void onAnimationRepeat(Animation animation) {

  }
}
