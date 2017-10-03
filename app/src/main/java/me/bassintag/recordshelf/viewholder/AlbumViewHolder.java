package me.bassintag.recordshelf.viewholder;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.db.object.Album;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.db.object.Artist;
import me.bassintag.recordshelf.db.object.Genre;

/*
** Created by Antoine on 06/09/2017.
*/
public class AlbumViewHolder<T extends AlbumDescription> extends ViewHolder implements OnClickListener {

  private TextView mAlbumNameView;
  private TextView mArtistNameView;
  private TextView mAlbumReleaseYearView;
  private TextView mGenreNameView;
  private TextView mHeaderView;
  private ImageView mAlbumCoverView;

  private String mDefaultAlbumName, mDefaultArtistName, mDefaultReleaseYear, mDefaultGenreName;
  private Drawable mDefaultAlbumCover;
  private int mDefaultPadding;

  private T mAlbum;

  private IAlbumViewHolderListener<T> mListener;
  private int mIndex = -1;

  public AlbumViewHolder(View itemView, IAlbumViewHolderListener<T> listener) {
    super(itemView);
    mListener = listener;
    itemView.findViewById(R.id.album_card).setOnClickListener(this);
    mAlbumNameView = (TextView) itemView.findViewById(R.id.album_card_name);
    mDefaultAlbumName = mAlbumNameView.getText().toString();
    mArtistNameView = (TextView) itemView.findViewById(R.id.album_card_artist);
    mDefaultArtistName = mArtistNameView.getText().toString();
    mGenreNameView = (TextView) itemView.findViewById(R.id.album_card_genre);
    mDefaultGenreName = mGenreNameView.getText().toString();
    mAlbumReleaseYearView = (TextView) itemView.findViewById(R.id.album_card_release_year);
    mDefaultReleaseYear = mAlbumReleaseYearView.getText().toString();
    mAlbumCoverView = (ImageView) itemView.findViewById(R.id.album_card_cover);
    mDefaultAlbumCover = mAlbumCoverView.getDrawable();
    mDefaultPadding = mAlbumCoverView.getPaddingStart();
    mHeaderView = (TextView) itemView.findViewById(R.id.album_category_header);
  }

  @Override
  public void onClick(View view) {
    mListener.onAlbumViewClicked(mAlbum, mIndex);
  }

  private void resetDefaults() {
    mAlbumNameView.setText(mDefaultAlbumName);
    mArtistNameView.setText(mDefaultArtistName);
    mAlbumReleaseYearView.setText(mDefaultReleaseYear);
    mGenreNameView.setText(mDefaultGenreName);
    mAlbumCoverView.setImageDrawable(mDefaultAlbumCover);
    mAlbumCoverView.setPadding(mDefaultPadding, mDefaultPadding, mDefaultPadding, mDefaultPadding);
    mHeaderView.setVisibility(View.GONE);
  }

  public void setAlbumDescription(T albumDescription, Bitmap cover, String header) {
    resetDefaults();
    if (header != null) {
      mHeaderView.setVisibility(View.VISIBLE);
      mHeaderView.setText(header);
    }
    mAlbum = albumDescription;
    Album album = albumDescription.getAlbum();
    mAlbumNameView.setText(album.getName());
    if (album.getReleaseYear() > 0) {
      mAlbumReleaseYearView.setText(String.valueOf(album.getReleaseYear()));
    }
    if (cover != null) {
      mAlbumCoverView.setImageBitmap(cover);
      mAlbumCoverView.setPadding(0, 0, 0, 0);
    }
    Artist artist = mAlbum.getArtist();
    if (artist != null) {
      mArtistNameView.setText(artist.getName());
    }
    Genre genre = mAlbum.getGenre();
    if (genre != null) {
      mGenreNameView.setText(genre.getName());
    }
  }

  public void setIndex(int index) {
    this.mIndex = index;
  }
}
