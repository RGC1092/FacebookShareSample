package com.example.facebooksharesample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "@SampleAPP";
    private TextView txtFB;
    private ShareDialog shareDialog;
    private String msg = "Our Partner Rahul has referred you for a Personal Loan. Click on the link to Download the App";
    private String link = "https://play.google.com/store/apps/details?id=com.ewspartner.earnwealth";
    private String url = "https://www.earnwealth.in/images/product/telemedicine.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareDialog = new ShareDialog(this);
        printHashKey(this);


         txtFB = findViewById(R.id.txtFacebook);
         txtFB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 shareFacebook();
             }
         });
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void shareFacebook() {
        try {

            Picasso.get().load(url).memoryPolicy(MemoryPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    share(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    Toast.makeText(MainActivity.this, "Image Loading Error", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onBitmapFailed: "+e.toString() );
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });




        } catch (Exception e) {
            e.printStackTrace();
            Log.i("@FBTAG", "shareFacebook: " + e);
        }
    }

    private void share(Bitmap bitmap) {

        Log.i(TAG, "share: Calling Facebook");

        try {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .setCaption(msg + "\n" + link)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .setShareHashtag(new ShareHashtag.Builder().setHashtag(msg + "\n#KamateRaho  #EarnWealth" + "\n" + link).build())
                    .build();
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC); // Show facebook ShareDialog
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "share: "+e.toString());
        }

    }
}