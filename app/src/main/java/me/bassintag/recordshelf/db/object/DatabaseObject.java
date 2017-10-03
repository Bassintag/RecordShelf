package me.bassintag.recordshelf.db.object;

/*
** Created by Antoine on 02/09/2017.
*/
public abstract class DatabaseObject {

  private final int mId;

  DatabaseObject(int id){
    mId = id;
  }

  public int getId() {
    return mId;
  }
}
