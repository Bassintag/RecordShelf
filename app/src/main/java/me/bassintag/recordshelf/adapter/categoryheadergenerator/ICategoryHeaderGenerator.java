package me.bassintag.recordshelf.adapter.categoryheadergenerator;

import me.bassintag.recordshelf.db.object.AlbumDescription;

/*
** Created by Antoine on 12/09/2017.
*/
public interface ICategoryHeaderGenerator {

  String getHeaderText(AlbumDescription previous, AlbumDescription current);
}
