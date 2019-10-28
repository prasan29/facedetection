package com.facedetector.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facedetector.R;
import com.facedetector.model.FaceDetectHelper;
import com.facedetector.util.ApplicationConstants;
import com.facedetector.view.MainActivity;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    public MutableLiveData<String> mFetchButton = new MutableLiveData<>();
    public MutableLiveData<String> mDetectFaceButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsButtonVisible = new MutableLiveData<>();
    private ImageView mGalleryImage;
    private Activity mActivity;
    private FaceDetectHelper mFaceDetector;
    private Bitmap mDetectImage;

    public MainViewModel() {
        mFetchButton.setValue("Fetch image");
        mDetectFaceButton.setValue("Detect face");
        mIsButtonVisible.setValue(false);
    }

    public void setActivity(MainActivity activity) {
        mActivity = activity;
        mGalleryImage = mActivity.findViewById(R.id.image_view);
        mFaceDetector = FaceDetectHelper.getInstance(activity);
    }

    public void onDetectPressed() {
        String result = mFaceDetector.processImage(mDetectImage, mActivity, mGalleryImage);
        Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
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
        mDetectImage = bitmap;
        mGalleryImage.setImageBitmap(bitmap);
        mIsButtonVisible.setValue(true);
    }

    public interface OnImageFetchedListener {
        void onImageFetched(Bitmap bitmap);
    }
}
