package me.bassintag.recordshelf.adapter;

import android.graphics.Bitmap;
import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 13/09/2017.
*/
public interface ICoverProvider<T extends AlbumDescription> {

  Bitmap getCoverForAlbum(T albumDescription);

}
