package me.bassintag.recordshelf.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import me.bassintag.recordshelf.R;

/*
** Created by Antoine on 05/09/2017.
*/
public class ImageChooserDialogFragment extends DialogFragment implements OnClickListener {

  private IImageChooserDialogFragmentListener mListener;

  @Override
  @NonNull
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.pick_image_chooser)
        .setItems(R.array.image_chooser_options, this);
    return builder.create();
  }

  @Override
  public void onAttach(Context context) {
    if (!(context instanceof IImageChooserDialogFragmentListener)) {
      throw new ClassCastException(
          context.toString() + " must implement IImageChooserDialogFragmentListener");
    }
    mListener = (IImageChooserDialogFragmentListener) context;
    super.onAttach(context);
  }

  private void pickImageFromGallery() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"),
        mListener.getImagePickerRequestCode());
  }

  private void pickImageFromCamera() {
    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    getActivity().startActivityForResult(cameraIntent, mListener.getImagePickerRequestCode());
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    switch (which) {
      default:
        break;
      case 0:
        pickImageFromGallery();
        break;
      case 1:
        pickImageFromCamera();
        break;
    }
  }
}
