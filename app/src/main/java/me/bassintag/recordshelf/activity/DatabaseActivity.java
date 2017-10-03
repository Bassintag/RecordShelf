package me.bassintag.recordshelf.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import me.bassintag.recordshelf.db.Database;

/*
** Created by Antoine on 08/09/2017.
*/
public abstract class DatabaseActivity extends AppCompatActivity {

  protected Database mDb;

  @Override
  protected final void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDb = new Database(this);
    onCreateAfter(savedInstanceState);
  }

  protected void onCreateAfter(Bundle savedInstanceState) {
  }

  @Override
  protected final void onDestroy() {
    mDb.close();
    onDestroyAfter();
    super.onDestroy();
  }

  protected void onDestroyAfter() {
  }
}
