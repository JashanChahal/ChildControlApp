package com.jashan.child_control_app.repository;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.RemoteMessage;
import com.jashan.child_control_app.activities.parent.AppUsageStat;
import com.jashan.child_control_app.model.UsageStatsWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService implements NotificationService{

    private final FirebaseMessaging messagingInstance;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAeaalugs:APA91bG-jfAM3S6slzjcX1AwCeC26c2ZnEJ9-3CU1vdnMjdWgjDXI0u8LmJ6nQqFym4krauxsBn5IF4BzuHpaMeTyjCp_8bRJ_iQrzbla9C3vIwhJX7ljT_NhJduZZf1Xw9fIppf7i2m";
    final private String contentType = "application/json";
    public static final String TOPIC = "/topics/client";


    public FirebaseMessagingService(){
        messagingInstance = FirebaseMessaging.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("notification","notification received");
        Log.d(remoteMessage.getData().get("title"),remoteMessage.getData().toString());

        if (remoteMessage.getData().get("title").equals("LL")) {
            Log.d("inside","inside location call");
            send_location();
        }
        if(remoteMessage.getData().get("title").equals("AU")){
            Log.d("inside","inside usage app statics");
            AppUsageStat appUsageStat = new AppUsageStat(this);
            List<UsageStatsWrapper> usageData = appUsageStat.retrieveUsageStats();
            sendAppUsageStaticInfo(usageData);
        }

    }
    @Override
    public void subscribeToNotificationTopic(String topicName){
        messagingInstance.subscribeToTopic(topicName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "sucessfully connect to firebase messaging";
                        if (!task.isSuccessful()) {

                            msg = "failed to connect to firebase messaging";
                        }

//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Log.d("subscribe",msg);
                    }
                });
    }
    @Override
    public void send(String title, String message, JSONObject notificationBody, Context context){
        JSONObject notification = new JSONObject();
        //JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", title);
            notificationBody.put("message", message);
            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("second func","inside send func");
        sendNotification(notification, context);
    }

    private void sendNotification(JSONObject notification, Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("notiicationStatus","success");
                        //Toast.makeText(ParentHomepage.this, "successfully send ", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("notiicationStatus","error");
                        //Toast.makeText(choice.this, "Request error", Toast.LENGTH_LONG).show();
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

        Log.d("third func","inside sendNotification func");
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void send_location(){
        Log.d(" sv", "inside lcoation func");
        //Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        //placesClient = Places.createClient(this);
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Log.d("location",location.toString());
                if (location!=null){
                    Log.d("location", "location is not null");
                    String lati=String.valueOf(location.getLatitude());
                    String longi=String.valueOf(location.getLongitude());
                    Log.d(lati,longi);
                    //shownotification("location","latitude="+lati+"longitude="+longi);
                    JSONObject notificationBody = new JSONObject();
                    try{
                        notificationBody.put("longitude",longi);
                        notificationBody.put("latitude",lati);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    send("location","",notificationBody, getApplicationContext());
                }
            }
        });

    }

    public void sendAppUsageStaticInfo(List<UsageStatsWrapper> usageData){
        JSONObject notificationBody = new JSONObject();
        for(UsageStatsWrapper singleData : usageData){
            try {
                notificationBody.put(singleData.getAppName(), singleData.getUsageStats().getTotalTimeInForeground());
            } catch (Exception e){
                send("UsageInfo","Error",notificationBody,getApplicationContext());
            }
        }
        Log.d("usage",notificationBody.toString());
        send("UsageInfo","success",notificationBody,getApplicationContext());
    }


}
