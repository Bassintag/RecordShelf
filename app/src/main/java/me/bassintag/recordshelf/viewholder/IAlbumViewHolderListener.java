package me.bassintag.recordshelf.viewholder;

import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 06/09/2017.
*/
public interface IAlbumViewHolderListener<T extends AlbumDescription> {

  void onAlbumViewClicked(T album, int index);

}
