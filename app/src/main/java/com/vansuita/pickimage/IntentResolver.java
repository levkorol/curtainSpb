package com.vansuita.pickimage;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * Created by jrvansuita build 07/02/17.
 */

public final class IntentResolver {

    public static final String SAVE_FILE_PATH_TAG = "savePath";

    private final Activity activity;
    private final String title;
    private Intent galleryIntent;
    private Intent cameraIntent;
    private File saveFile;


    public IntentResolver(Activity activity, String title, Bundle savedInstanceState) {
        this.activity = activity;
        this.title = title;

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    public boolean isCamerasAvailable() {
        String feature = PackageManager.FEATURE_CAMERA;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            feature = PackageManager.FEATURE_CAMERA_ANY;
        }

        return activity.getPackageManager().hasSystemFeature(feature);
    }

    public Intent getCameraIntent() {
        if (cameraIntent == null) {
            cameraIntent = new Intent(/*setup.isVideo() ? MediaStore.ACTION_VIDEO_CAPTURE :*/ MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUriForProvider());

            applyProviderPermission();
        }

        return cameraIntent;
    }

    /**
     * Granting permissions to write and read for available cameras to file provider.
     */
    private void applyProviderPermission() {
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, cameraUriForProvider(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public File cameraFile() {
        if (saveFile != null) {
            return saveFile;
        }

        File directory;
        String fileName;
        /*if (setup.isCameraToPictures()) {
            ApplicationInfo applicationInfo = activity.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            String appName = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : activity.getString(stringId);
            directory = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appName);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            if (setup.isVideo()) {
                fileName = timeStamp + ".mp4";
            } else {
                fileName = timeStamp + ".jpg";
            }
        } else {*/
            directory = new File(activity.getFilesDir(), "upload");
            fileName = String.valueOf(System.currentTimeMillis());
//        }

        // File directory = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"teste");
        directory.mkdirs();
        saveFile = new File(directory, fileName);
        Log.i("File-PickImage", saveFile.getAbsolutePath());

        return saveFile;
    }

    private String getAuthority() {
        return activity.getApplication().getPackageName() + ".com.vansuita.pickimage.provider";
    }

    private Uri cameraUriForProvider() {
        try {
            return FileProvider.getUriForFile(activity, getAuthority(), cameraFile());
        } catch (Exception e) {
            if (e.getMessage().contains("ProviderInfo.loadXmlMetaData")) {
                throw new Error("FileProvider not declared or has wrong authority.");
            } else {
                throw e;
            }
        }
    }

    public Intent getGalleryIntent() {
        if (galleryIntent == null) {
            galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    /*setup.isVideo() ? "video/*" :*/ "image/*"
                );
        }

        return galleryIntent;
    }

    @Nullable public Intent systemChooser() {
        Intent gal = /*setup.pickFromGallery() ?*/ getGalleryIntent() /*: null*/;
        Intent cam =
            /*setup.pickFromCamera() &&*/ isCamerasAvailable()// && !wasCameraPermissionDeniedForever()
                ? getCameraIntent() : null;

        if (gal == null && cam == null) return null;

        Intent ch = Intent.createChooser(gal != null ? gal : cam, title);
        if (gal != null && cam != null) ch.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{ cam });
        return ch;
    }

    public boolean fromCamera(Intent data) {
        return (data == null || data.getData() == null || data.getData().toString().contains(cameraFile().toString()) || data.getData().toString().contains("to_be_replaced"));
    }

    public Activity getActivity() {
        return activity;
    }

    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        final String saveFilePath = savedInstanceState.getString(SAVE_FILE_PATH_TAG);

        if (saveFilePath != null) {
            saveFile = new File(saveFilePath);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (saveFile != null) {
            outState.putString(SAVE_FILE_PATH_TAG, saveFile.getAbsolutePath());
        }
    }
}
