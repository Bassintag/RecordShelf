package me.bassintag.recordshelf.task;

import java.util.List;
import me.bassintag.recordshelf.db.object.WebAlbumDescription;

/*
** Created by Antoine on 07/09/2017.
*/
public interface IRetrieveAlbumsListener {

  void onAlbumRetrieved(WebAlbumDescription albumDescription);
  void onTaskError();
  void onTaskEnd(List<WebAlbumDescription> albumList);
}
