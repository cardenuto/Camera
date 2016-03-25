package info.anth.camera;

import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * example from http://www.tutorialspoint.com/android/android_camera.htm
 */
public class MainActivity extends AppCompatActivity {
    Button b1, b2;
    ImageView iv;
    Uri gimageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        iv = (ImageView) findViewById(R.id.imageView);

        //set inital image
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

        //ImageLoader.getInstance().displayImage("http://www.password12345.com/wp-content/uploads/2014/06/stuff4-orange2.jpg", iv, options);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // before
                //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, 0);
                dispatchTakePictureIntent();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // before
        //Bitmap bp = (Bitmap) data.getExtras().get("data");
        //iv.setImageBitmap(bp);

        //                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
        //                 .showImageOnLoading(R.drawable.ic_stub)
        //set inital image
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        Log.i("ajc", "uri: " + gimageUri.toString());
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.loading);

        ImageLoader.getInstance().displayImage(gimageUri.toString(), iv, options,
                // code to add new functions
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        spinner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        String message = null;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = "Input/Output error";
                                break;
                            case DECODING_ERROR:
                                message = "Image can't be decoded";
                                break;
                            case NETWORK_DENIED:
                                message = "Downloads are denied";
                                break;
                            case OUT_OF_MEMORY:
                                message = "Out Of Memory error";
                                break;
                            case UNKNOWN:
                                message = "Unknown error";
                                break;
                        }
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                        spinner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        spinner.setVisibility(View.GONE);
                        // cloudinary
                        // set in application?
                        //Map config = new HashMap();
                        //config.put("cloud_name", "dqeqimfy5");

                        startUpload("nothing");
                        /*
                        Cloudinary cloudinary = new Cloudinary();
                        String testPath = gimageUri.getPath();
                        testPath = "/storage/emulated/0/Pictures/JPEG_20160319_140404_773191082.jpg";

                        File myfile = new File(testPath);
                        InputStream inMyFile = null;

                        if (myfile.exists()) {
                            Log.i("ajc", "file exist");
                        } else {
                            Log.i("ajc", "file does NOT exist");
                        }

                        Log.i("ajc", "onLoadComplete uri path: " + testPath);
                        try {
                            //cloudinary.uploader().unsignedUpload(getAssets().open(testPath), "ithmyy6i",
                            cloudinary.uploader().unsignedUpload(myfile, "ithmyy6i",
                                    ObjectUtils.asMap("cloud_name", "dqeqimfy5"));
                            //MyApplication.getInstance(this).getCloudinary().uploader().unsignedUpload(getAssets().open(imageUri.getPath()), "ithmyy6i");
                        } catch (RuntimeException e) {
                            Log.e("ajc", "Error uploading file: " + e.toString());
                        } catch (IOException e) {
                            Log.e("ajc", "Error uploading file: " + e.toString());
                        }
                        */
                    }
                });
    }

    // cloudinary
    /*private InputStream getAssetStream(String filename) throws IOException {
        return this.getAssets().open(filename);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("ajc", "error creating file");
            }
            gimageUri = Uri.fromFile(photoFile);
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.

            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    */
    private void startUpload(String filePath) {
        Log.i("ajc", "in StartUpload");
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... paths) {
                Log.i("ajc", "Running upload task");

                Cloudinary cloudinary = new Cloudinary();
                String testPath = gimageUri.getPath();
                //testPath = "/storage/emulated/0/Pictures/JPEG_20160319_140404_773191082.jpg";

                File myfile = new File(testPath);
                if (myfile.exists()) {
                    Log.i("ajc", "file exist");
                } else {
                    Log.i("ajc", "file does NOT exist");
                }

                Log.i("ajc", "onLoadComplete uri path: " + testPath);
                Map cloudinaryResult;
                try {
                    cloudinaryResult = cloudinary.uploader().unsignedUpload(myfile, "ithmyy6i", ObjectUtils.asMap("cloud_name", "dqeqimfy5"));
                    Log.i("ajc", "Uploaded file: " + cloudinaryResult.toString());
                } catch (RuntimeException e) {
                    Log.e("ajc", "Error uploading file: " + e.toString());
                    return "Error uploading file: " + e.toString();
                } catch (IOException e) {
                    Log.e("ajc", "Error uploading file: " + e.toString());
                    return "Error uploading file: " + e.toString();
                }

                return null;
            }

            protected void onPostExecute(String error) {
                Log.i("ajc", "onPostExecution!");
            }
        };
        task.execute(filePath);
    }
}
