package me.bassintag.recordshelf.activity;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.Detector.Processor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import java.util.List;
import me.bassintag.recordshelf.R;
import me.bassintag.recordshelf.activity.resultcode.AlbumResultCode;
import me.bassintag.recordshelf.activity.viewmanager.AddAlbumViewManager;
import me.bassintag.recordshelf.db.object.WebAlbumDescription;
import me.bassintag.recordshelf.task.IRetrieveAlbumCoverListener;
import me.bassintag.recordshelf.task.IRetrieveAlbumsListener;
import me.bassintag.recordshelf.task.RetrieveAlbumCoverTask;
import me.bassintag.recordshelf.task.RetrieveAlbumCoverTask.RetrievedCover;
import me.bassintag.recordshelf.task.RetrieveAlbumsByBarcodeTask;
import me.bassintag.recordshelf.task.RetrieveAlbumsTask;

/*
** Created by Antoine on 09/09/2017.
*/
public class BarcodeScannerActivity extends DatabaseActivity implements Callback,
    Processor<Barcode>, IRetrieveAlbumsListener, IRetrieveAlbumCoverListener, OnClickListener {

  private SurfaceView mCameraView;
  private CameraSource mCameraSource;
  private BarcodeDetector mBarcodeDetector;

  private TextView mBarcodeTextView;
  private TextView mPlaceholder;

  private AddAlbumViewManager mAddAlbumCard;
  private View mPreloader;

  private RetrieveAlbumsTask mTask;
  private RetrieveAlbumCoverTask mCoverTask;

  private String mLastBarcodeValue;

  private void initCameraSource() {
    mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(
        Barcode.EAN_13 | Barcode.EAN_8 | Barcode.UPC_A | Barcode.UPC_E | Barcode.CODE_39
            | Barcode.CODE_93 | Barcode.CODE_128 | Barcode.ITF | Barcode.CODABAR
    )
        .build();
    mBarcodeDetector.setProcessor(this);
    mCameraView.getHolder().addCallback(BarcodeScannerActivity.this);
  }

  private void onScanNewBarcode(final String value) {
    mLastBarcodeValue = value;
    mBarcodeTextView.setText(value);
    mPreloader.setVisibility(View.VISIBLE);
    mAddAlbumCard.hide();
    mPlaceholder.setVisibility(View.GONE);
    if (mTask != null) {
      mTask.cancel(true);
    }
    if (mCoverTask != null) {
      mCoverTask.cancel(true);
    }
    mTask = new RetrieveAlbumsByBarcodeTask(this);
    mTask.execute(value);
  }

  @Override
  protected void onCreateAfter(Bundle savedInstanceState) {
    setContentView(R.layout.activity_barcode_scanner);
    mCameraView = (SurfaceView) findViewById(R.id.camera_view);
    mBarcodeTextView = (TextView) findViewById(R.id.barcode_text);
    mPlaceholder = (TextView) findViewById(R.id.scan_barcode_placeholder);
    mPreloader = findViewById(R.id.preloader);
    mAddAlbumCard = new AddAlbumViewManager((ViewGroup) findViewById(R.id.add_album_card), mDb,
        this);
    mAddAlbumCard.setOnClickListener(this);
    initCameraSource();
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (ContextCompat.checkSelfPermission(this, permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
      return;
    }
    if (mCameraSource != null) {
      mCameraSource.stop();
    }
    mCameraSource = new CameraSource.Builder(BarcodeScannerActivity.this, mBarcodeDetector)
        .setRequestedPreviewSize(width, height)
        .setAutoFocusEnabled(true)
        .setFacing(CameraSource.CAMERA_FACING_BACK)
        .build();
    try {
      mCameraSource.start(holder);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    LayoutParams params = mCameraView.getLayoutParams();
    Size size = mCameraSource.getPreviewSize();
    params.height = mCameraView.getMeasuredWidth() * size.getHeight() / size.getWidth();
    mCameraView.setLayoutParams(params);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    if (mCameraSource != null) {
      mCameraSource.stop();
    }
  }

  @Override
  public void release() {

  }

  @Override
  public void receiveDetections(Detections<Barcode> detections) {
    SparseArray<Barcode> barcodes = detections.getDetectedItems();
    if (barcodes.size() > 0) {
      final Barcode barcode = barcodes.valueAt(0);
      if (!barcode.displayValue.equals(mLastBarcodeValue)) {
        mBarcodeTextView.post(new Runnable() {
          @Override
          public void run() {
            onScanNewBarcode(barcode.displayValue);
          }
        });
      }
    }
  }

  @Override
  public void onAlbumRetrieved(WebAlbumDescription albumDescription) {
  }

  @Override
  public void onTaskError() {
    mPreloader.setVisibility(View.GONE);
    mPlaceholder.setText(R.string.error_occurred);
    mPlaceholder.setVisibility(View.VISIBLE);
    mPreloader.setVisibility(View.GONE);
    mTask = null;
  }

  @Override
  public void onTaskEnd(List<WebAlbumDescription> albumList) {
    mTask = null;
    if (albumList.size() == 0) {
      mPlaceholder.setText(R.string.no_result_found);
      mPlaceholder.setVisibility(View.VISIBLE);
      mPreloader.setVisibility(View.GONE);
      return;
    }
    mCoverTask = new RetrieveAlbumCoverTask(this);
    mCoverTask.execute(albumList.get(0));
  }

  @Override
  public void onAlbumCoverRetrieved(RetrievedCover retrievedCover) {
    mCoverTask = null;
    mAddAlbumCard.show();
    mAddAlbumCard.setFromAlbumDescription(retrievedCover.albumDescription, retrievedCover.cover);
    mPreloader.setVisibility(View.GONE);
  }

  @Override
  public void onClick(View v) {
    setResult(AlbumResultCode.RESULT_ALBUM_CREATE);
  }
}