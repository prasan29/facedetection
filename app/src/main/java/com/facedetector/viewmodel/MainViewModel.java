package com.facedetector.viewmodel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facedetector.R;
import com.facedetector.util.ApplicationConstants;
import com.facedetector.view.MainActivity;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    public MutableLiveData<String> mFetchButton = new MutableLiveData<>();
    public MutableLiveData<String> mDetectFaceButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsButtonVisible = new MutableLiveData<>();
    private ImageView mGalleryImage;
    private MainActivity mActivity;

    public MainViewModel() {
        mFetchButton.setValue("Fetch image");
        mDetectFaceButton.setValue("Detect face");
        mIsButtonVisible.setValue(false);
    }

    public void setActivity(MainActivity activity) {
        mActivity = activity;
        mGalleryImage = mActivity.findViewById(R.id.image_view);
    }

    public void onDetectPressed() {
        Log.e(TAG, "onDetectPressed()");
    }

    public void setCallback(MainActivity.Callback callback) {
        callback.setCallback(this::onImageFetched);
    }

    public void onFetchImage() {
        mActivity.startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                ApplicationConstants.RESULT_LOAD_IMAGE);
    }

    private void onImageFetched(Bitmap bitmap) {
        mGalleryImage.setImageBitmap(bitmap);
        mIsButtonVisible.setValue(true);
    }

    public interface OnImageFetchedListener {
        void onImageFetched(Bitmap bitmap);
    }
}
