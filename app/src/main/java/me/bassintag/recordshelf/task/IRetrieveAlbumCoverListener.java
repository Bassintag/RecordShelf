package me.bassintag.recordshelf.task;

import me.bassintag.recordshelf.db.object.WebAlbumDescription;
import me.bassintag.recordshelf.task.RetrieveAlbumCoverTask.RetrievedCover;

/*
** Created by Antoine on 08/09/2017.
*/
public interface IRetrieveAlbumCoverListener {

  void onAlbumCoverRetrieved(RetrievedCover retrievedCover);
}
