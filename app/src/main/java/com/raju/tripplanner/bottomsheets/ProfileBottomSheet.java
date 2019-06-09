package com.raju.tripplanner.bottomsheets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.raju.tripplanner.BuildConfig;
import com.raju.tripplanner.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ProfileBottomSheet extends BottomSheetDialogFragment {


    public static final int REQUEST_IMAGE_CAPTURE = 181;
    public static final int REQUEST_IMAGE_GALLERY = 905;

    private String capturedImagePath;
    private ProfileBottomSheetListener profileBottomSheetListener;


    public ProfileBottomSheet() {
    }

    public static ProfileBottomSheet newInstance() {
        return new ProfileBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileBottomSheet = inflater.inflate(R.layout.bottom_sheet_profile, container, false);
        TextView tvCamera = profileBottomSheet.findViewById(R.id.tv_action_camera);
        TextView tvGallery = profileBottomSheet.findViewById(R.id.tv_action_gallery);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromGallery();
            }
        });
        return profileBottomSheet;
    }

    //    take an image from camera
    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File tempFile = null;
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (tempFile != null) {
                Uri imageURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", tempFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //choose an image from gallery
    private void chooseImageFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        chooseImageIntent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpg", "image/jpeg", "image/png"};
        chooseImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(chooseImageIntent, REQUEST_IMAGE_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    dismiss();
                    profileBottomSheetListener.onImageSelected(capturedImagePath);
                }
                break;

            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    dismiss();
                    //data.getData returns the content URI for the selected Image
                    String galleryImagePath = getGalleryImagePath(data.getData());
                    profileBottomSheetListener.onImageSelected(galleryImagePath);
//                    Log.i("SELECTED IMAGE PATH", galleryImagePath);
                }
                break;
        }
    }

    //    create a temporary file to store the captured image
    private File createImageFile() throws IOException {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "IMG_" + timestamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Trip Planner");
        if (!storageDir.mkdirs()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(filename, ".jpg", storageDir);
        capturedImagePath = image.getAbsolutePath();
//        Log.i("CAPTURED IMAGE PATH", capturedImagePath);
        return image;
    }

    // get the path of the image selected from gallery
    private String getGalleryImagePath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;
    }

    //    listener to be implemented in main activity
    public interface ProfileBottomSheetListener {
        void onImageSelected(String imagePath);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            profileBottomSheetListener = (ProfileBottomSheetListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement Profile BottomSheet Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        profileBottomSheetListener = null;
    }
}
