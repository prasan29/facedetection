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

/**
 * Class for holding data and handle events for MainActivity.
 */
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    public MutableLiveData<String> mFetchButton = new MutableLiveData<>();
    public MutableLiveData<String> mDetectFaceButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsButtonVisible = new MutableLiveData<>();
    private ImageView mGalleryImage;
    private Activity mActivity;
    private FaceDetectHelper mFaceDetector;
    private Bitmap mDetectImage;

    /**
     * Constructor to build all the view and data.
     */
    public MainViewModel() {
        mFetchButton.setValue("Fetch image");
        mDetectFaceButton.setValue("Detect face");
        mIsButtonVisible.setValue(false);
    }

    /**
     * Method to save Activity instance.
     *
     * @param activity Instance of Activity.
     */
    public void setActivity(MainActivity activity) {
        mActivity = activity;
        mGalleryImage = mActivity.findViewById(R.id.image_view);
        mFaceDetector = FaceDetectHelper.getInstance(activity);
    }

    /**
     * Method called when detect button is pressed.
     */
    public void onDetectPressed() {
        String result = mFaceDetector.processImage(mDetectImage, mActivity, mGalleryImage);
        Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to set the callback for retrieving the image from the gallery.
     *
     * @param callback To listen the retrieved image.
     */
    public void setCallback(MainActivity.Callback callback) {
        callback.setCallback(this::onImageFetched);
    }

    /**
     * Method called when fetch button is pressed.
     */
    public void onFetchImage() {
        mActivity.startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                ApplicationConstants.RESULT_LOAD_IMAGE);
    }

    /**
     * Method to set the retrieved image into the ImageView.
     *
     * @param bitmap Retrieved image.
     */
    private void onImageFetched(Bitmap bitmap) {
        mDetectImage = bitmap;
        mGalleryImage.setImageBitmap(bitmap);
        mIsButtonVisible.setValue(true);
    }

    /**
     * Interface to listen for image retrieval.
     */
    public interface OnImageFetchedListener {
        /**
         * Method to set the retrieved image into the ImageView.
         *
         * @param bitmap Retrieved image.
         */
        void onImageFetched(Bitmap bitmap);
    }
}
