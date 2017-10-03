package me.bassintag.recordshelf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.adapter.categoryheadergenerator.ICategoryHeaderGenerator;
import me.bassintag.recordshelf.db.object.AlbumDescription;
import me.bassintag.recordshelf.viewholder.AlbumViewHolder;
import me.bassintag.recordshelf.viewholder.IAlbumViewHolderListener;

/*
** Created by Antoine on 02/09/2017.
*/
public class AlbumRecyclerViewAdapter<T extends AlbumDescription> extends RecyclerView.Adapter<AlbumViewHolder> {

  private List<T> mAlbums;
  private IAlbumViewHolderListener<T> mListener;

  private ICoverProvider<T> mCoverProvider;
  private ICategoryHeaderGenerator mGenerator;

  public AlbumRecyclerViewAdapter(IAlbumViewHolderListener<T> listener,
      List<T> albums,
      ICoverProvider<T> coverProvider,
      ICategoryHeaderGenerator headerGenerator) {
    mAlbums = albums;
    mListener = listener;
    mCoverProvider = coverProvider;
    mGenerator = headerGenerator;
  }

  @Override
  public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_album_card, parent, false);
    return new AlbumViewHolder(view, mListener);
  }

  @Override
  public void onBindViewHolder(AlbumViewHolder holder, int position) {
    T album = mAlbums.get(position);
    String header = null;
    if (mGenerator != null) {
      T previous = null;
      if (position > 0) {
        previous = mAlbums.get(position - 1);
      }
      header = mGenerator.getHeaderText(previous, mAlbums.get(position));
    }
    holder.setAlbumDescription(album,
        mCoverProvider.getCoverForAlbum(album),
        header);
    holder.setIndex(position);
  }

  @Override
  public int getItemCount() {
    return mAlbums.size();
  }

  public void setAlbum(T albumDescription, int position) {
    mAlbums.set(position, albumDescription);
    notifyItemChanged(position);
  }

  public int addAlbum(T albumDescription) {
    mAlbums.add(albumDescription);
    notifyItemInserted(mAlbums.size() - 1);
    return mAlbums.size() - 1;
  }

  public void clear() {
    int count = mAlbums.size();
    mAlbums.clear();
    notifyItemRangeRemoved(0, count);
  }

  public void setAlbums(List<T> albums) {
    mAlbums = albums;
    notifyDataSetChanged();
  }

  public void setHeaderGenerator(ICategoryHeaderGenerator headerGenerator) {
    mGenerator = headerGenerator;
    notifyDataSetChanged();
  }
}
