package com.jashan.child_control_app.activities.parent;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.PixelCopy;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jashan.child_control_app.repository.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class Screenshot extends Activity {
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAeaalugs:APA91bG-jfAM3S6slzjcX1AwCeC26c2ZnEJ9-3CU1vdnMjdWgjDXI0u8LmJ6nQqFym4krauxsBn5IF4BzuHpaMeTyjCp_8bRJ_iQrzbla9C3vIwhJX7ljT_NhJduZZf1Xw9fIppf7i2m";
    final private String contentType = "application/json";
    public static final String TOPIC = "/topics/client";

    public static final String STATE_RESULT_CODE = "result_code";
    public static final String STATE_RESULT_DATA = "result_data";
    public static final int REQUEST_MEDIA_PROJECTION = 1;
    private int mScreenDensity;
    private int mResultCode;
    private Intent mResultData;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionManager mMediaProjectionManager;
    private ImageReader imageReader;
    public  int width,height;
    private MediaProjectionManager mgr;
    Uri uri;
    StorageReference storage= FirebaseStorage.getInstance().getReference("uploads/");



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_child);

        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity=displayMetrics.densityDpi;
        width=displayMetrics.widthPixels;
        height=displayMetrics.heightPixels;

        imageReader=ImageReader.newInstance(width,height, ImageFormat.JPEG,2);


        mMediaProjectionManager=(MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);
        KeyguardManager myKM = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);

        boolean isPhoneLocked = myKM.inKeyguardRestrictedInputMode();

        if(isPhoneLocked){
            Toast.makeText(Screenshot.this,"locked",Toast.LENGTH_SHORT).show();

            // send("SSNAVLB");
            Toast.makeText(this,"locked",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Screenshot.this, "not locked", Toast.LENGTH_SHORT).show();
            // send("SSAVLB");
            startScreenCapture();

        }

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void finish() {
        super.finishAndRemoveTask();
        stopScreenCapture();
        tearDownMediaProjection();


    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScreenCapture() {

        if (mMediaProjection != null) {
            setUpVirtualDisplay();

        } else if (mResultCode != 0 && mResultData != null) {

            setUpMediaProjection();
            setUpVirtualDisplay();

        } else {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);

        }

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setUpMediaProjection(){
        Log.d("setttttttttttvvvvv","setting up media display");
        mMediaProjection=mMediaProjectionManager.getMediaProjection(mResultCode,mResultData);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void tearDownMediaProjection(){
        if (mMediaProjection!=null){
            mMediaProjection.stop();
            mMediaProjection=null;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpVirtualDisplay(){

        Log.d("setttttttttttvvvvv","setting up virtual display");
//        new Handler().postDelayed(,1000);
                mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture", width, height, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null);


    }
    private void stopScreenCapture(){

        if (mVirtualDisplay==null){
            return;
        }

        mVirtualDisplay.release();
        mVirtualDisplay=null;

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopScreenCapture();
    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Screenshot.this, "successfully send ", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Screenshot.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
                return;
            }



            mResultCode = resultCode;
            mResultData = data;
            setUpMediaProjection();
            setUpVirtualDisplay();
            Handler handler=new Handler();

            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {

                    final Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                    try {



                        PixelCopy.request(imageReader.getSurface(), bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                            @Override
                            public void onPixelCopyFinished(int copyResult) {
                                if (copyResult==PixelCopy.SUCCESS){

                                    String stren=BitMapToString(bitmap);


                                    Log.d("[][][][][][]]]]]]]]]]","inside geturi method ");
                                    String Fileuri =  getImageUri(Screenshot.this, bitmap);
                                    uri = Uri.fromFile(new File(Fileuri));
                                    if (uri!=null){
                                        Toast.makeText(Screenshot.this, uri.toString(),Toast.LENGTH_SHORT).show();
                                        Log.d("**********************",uri.toString());


                                        final StorageReference store=storage.child("uploads/"+uri.getLastPathSegment()+".jpeg");
                                        UploadTask uploadTask =store.putFile(uri);


                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Log.d("SUCCESSFULLTASK","WE HAVE DONE IT");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("EXCEPTION OCCURED",e.toString());
                                            }
                                        });
                                        Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (!task.isSuccessful()){
                                                    throw task.getException();
                                                }
                                                Log.d("xxxxxxxx",store.getDownloadUrl().toString());
                                                return store.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()){
                                                    Uri downloaduri=task.getResult();
                                                    if (downloaduri!=null && !downloaduri.toString().isEmpty()){
                                                        Log.d("GETTING THE DOWNLOADABLE URL","doenloadable url");
                                                        send(downloaduri.toString());
                                                    }
                                                }
                                            }
                                        });

                                        finish();
                                    }
                                    else
                                        Toast.makeText(Screenshot.this,"uri is null",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Handler());

                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if (bitmap!=null)
                        Toast.makeText(Screenshot.this,"not null",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Screenshot.this,"null",Toast.LENGTH_SHORT).show();

                }
            },100);



        }



    }


    public void send(String string){
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", "call me");
            notifcationBody.put("message", string);
            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendNotification(notification);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }




    public String getImageUri(Context inContext, Bitmap inImage) {
        ContextWrapper cw = new ContextWrapper(inContext);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"p.jpg");

        FileOutputStream fos = null;

//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        //String path= MediaStore.Images.Media.insertImage(inContext.getApplicationContext().getContentResolver(),inImage,"title",null);
//        //Log.d("urlpath",path);
//        return path;

        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            inImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath()+"/p.jpg";
    }
}
