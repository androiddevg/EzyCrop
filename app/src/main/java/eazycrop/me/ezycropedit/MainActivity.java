package eazycrop.me.ezycropedit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

import eazycrop.me.ezycropedit.Utils.ImagePicker;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    private static final int CROP_IMAGE = 238; // the number doesn't matter
    private static final int MY_PERMISSIONS_REQUEST = 1025; // the number doesn't matter
    /**
     * The Selected image.
     */
    ImageView selectedImage, /**
     * The Cropped image.
     */
    croppedImage;
    /**
     * The No prev avali 1.
     */
    AppCompatTextView no_prev_avali_1, /**
     * The No prev avali 2.
     */
    no_prev_avali_2;
    /**
     * The Btn select.
     */
    AppCompatButton btn_select;
    /**
     * The Final image uri.
     */

    Uri finalImageUri;
    /**
     * The Cropped image uri.
     */
    Uri croppedImageUri;
    RelativeLayout rl_croppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        btn_select.setOnClickListener(this);
    }

    private void initialize() {
        selectedImage = findViewById(R.id.selectedImage);
        croppedImage = findViewById(R.id.croppedImage);
        no_prev_avali_1 = findViewById(R.id.no_prev_avali_1);
        no_prev_avali_2 = findViewById(R.id.no_prev_avali_2);
        rl_croppedImage = findViewById(R.id.rl_croppedImage);
        btn_select = findViewById(R.id.btn_select);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == btn_select.getId()) {
            checkForPermissions();
            no_prev_avali_1.setVisibility(View.VISIBLE);
            no_prev_avali_2.setVisibility(View.VISIBLE);
            croppedImage.setImageURI(null);
            selectedImage.setImageURI(null);
            rl_croppedImage.setBackgroundColor(this.getResources().getColor(R.color.colorTransparent));

            if (isAccessGiven()) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
            } else {
                checkForPermissions();
            }
        }
    }

    private void checkForPermissions() {
        int writeStoreage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStoreage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int useCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (writeStoreage != PackageManager.PERMISSION_GRANTED || readStoreage != PackageManager.PERMISSION_GRANTED || useCamera != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private boolean isAccessGiven() {
        int writeStoreage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStoreage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int useCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (writeStoreage == PackageManager.PERMISSION_GRANTED && useCamera == PackageManager.PERMISSION_GRANTED && readStoreage == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                finalImageUri = ImagePicker.getImageUri(this, resultCode, data);
                if (finalImageUri != null) {
                    no_prev_avali_1.setVisibility(View.GONE);
                    selectedImage.setImageURI(null);
                    selectedImage.setImageURI(finalImageUri);
                    rl_croppedImage.setBackgroundColor(this.getResources().getColor(R.color.colorTransparent));

                }

                croppedImageUri = ImagePicker.doCrop(this, resultCode, data, CROP_IMAGE);

                // TODO use imageURI
                break;

            case CROP_IMAGE:
                if (croppedImageUri != null) {
                    no_prev_avali_2.setVisibility(View.GONE);
                    croppedImage.setImageURI(null);
                    croppedImage.setImageURI(croppedImageUri);
                    rl_croppedImage.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                }
                // TODO use imageURI
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
