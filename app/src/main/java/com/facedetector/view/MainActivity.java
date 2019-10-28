package com.facedetector.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.facedetector.R;
import com.facedetector.databinding.ActivityMainBinding;
import com.facedetector.util.ApplicationConstants;
import com.facedetector.viewmodel.MainViewModel;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainViewModel.OnImageFetchedListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setActivity(this);
        viewModel.setCallback(this::setCallback);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    /**
     * Method to set the callback for retrieving the image from the gallery.
     *
     * @param listener To listen the retrieved image.
     */
    private void setCallback(MainViewModel.OnImageFetchedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.RESULT_LOAD_IMAGE &&
                resultCode == RESULT_OK && null != data) {
            Bitmap bitmap = null;
            try {
                bitmap = getBitmapFromUri(data.getData());
            } catch (IOException e) {
                Log.e(TAG, "No image found!");
            }
            mListener.onImageFetched(bitmap);
        }
    }

    /**
     * Method to extract the Bitmap from URI.
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Interface for create a communication for image retrieval.
     */
    public interface Callback {
        /**
         * Method to set the callback for retrieving the image from the gallery.
         *
         * @param listener To listen the retrieved image.
         */
        void setCallback(MainViewModel.OnImageFetchedListener listener);
    }
}
