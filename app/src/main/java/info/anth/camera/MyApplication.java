package info.anth.camera;

import android.app.Application;
import android.content.Context;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    private Cloudinary cloudinary;

    /**
     * @return An initialized Cloudinary instance
     */
    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    /**
     * Provides access to the singleton and the getCloudinary method
     * @param context Android Application context
     * @return instance of the Application singleton.
     */
    public static MyApplication getInstance(Context context) {
        return (MyApplication)context.getApplicationContext();
    }


    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        super.onCreate();
// Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        Map configgy = new HashMap();
        configgy.put("cloud_name", "dqeqimfy5");
        cloudinary = new Cloudinary(configgy);

        // Cloudinary: creating a cloudinary instance using meta-data from manifest
        //cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(this));
    }
}