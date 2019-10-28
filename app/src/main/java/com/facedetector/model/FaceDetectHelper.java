package com.facedetector.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class FaceDetectHelper {
    private static FaceDetectHelper mFaceDetectHelper;
    private static FaceDetector mFaceDetector;
    private Bitmap editedBitmap;

    private FaceDetectHelper(Context context) {
        mFaceDetector = new FaceDetector.Builder(context.getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
    }

    public static FaceDetectHelper getInstance(Context context) {
        if (mFaceDetectHelper == null) {
            mFaceDetectHelper = new FaceDetectHelper(context);
        }

        return mFaceDetectHelper;
    }

    public String processImage(Bitmap bitmap, Context context, ImageView imageView) {
        if (mFaceDetector.isOperational() && bitmap != null) {
            editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), bitmap.getConfig());
            float scale = context.getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.CYAN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            Canvas canvas = new Canvas(editedBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = mFaceDetector.detect(frame);

            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);

                canvas.drawText("Face " + (index + 1), face.getPosition().x +
                        face.getWidth(), face.getPosition().y + face.getHeight(), paint);

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }
            }

            if (faces.size() == 0) {
                return "No face detected!";
            } else {
                imageView.setImageBitmap(editedBitmap);
                return faces.size() + " faces found!";
            }
        } else {
            return "Could not setup FaceDetector!";
        }
    }

}
