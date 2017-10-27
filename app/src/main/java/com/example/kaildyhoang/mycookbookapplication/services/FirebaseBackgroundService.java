package com.example.kaildyhoang.mycookbookapplication.services;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.kaildyhoang.mycookbookapplication.MainActivity;
import com.example.kaildyhoang.mycookbookapplication.NotificationActivity;
import com.example.kaildyhoang.mycookbookapplication.models.NotificationObj;
import com.example.kaildyhoang.mycookbookapplication.R;
import com.example.kaildyhoang.mycookbookapplication.view.Left_Activity;
import com.example.kaildyhoang.mycookbookapplication.view.MainActivity_View;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ChildEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 onCreate(): Hệ thống sẽ gọi phương thức này khi service được khởi tạo.
 onStartCommand(): Hệ thống sẽ gọi phương thức này khi một component hoạt động. Chúng ta dừng service khi công việc hoàn thành bằng cách gọi stopSelf() hoặc stopService().
 onBind(): Hoạt động khi gọi bindService(), cho phép client giao tiếp với service.
 onUnbind(): Ngắt tất cả kết nối client – service.
 onRebind(): Cho phép client kết nối lại với service sau khi đã gọi onUnbind().
 onDestroy(): Hủy bỏ service
 */

public class FirebaseBackgroundService extends Service {
    private Firebase firebase, msgFirebase;
    private FirebaseAuth mAuth;
    private ChildEventListener handler, msgHandle;
    private FirebaseUser userAuth;
    private String uID;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        uID = userAuth.getUid();

        firebase = new Firebase("https://cookbookapplication-396d8.firebaseio.com/users/"+uID+"/notifications/general_notifications");

        msgFirebase = new Firebase("https://cookbookapplication-396d8.firebaseio.com/users/"+uID+"/notifications/chat_notifications");

        handler = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Toast.makeText(getApplicationContext(),dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                NotificationObj notificationObj = dataSnapshot.getValue(NotificationObj.class);
                postNotif(notificationObj.getNotiMsg());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        msgHandle = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Toast.makeText(getApplicationContext(),dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                NotificationObj notificationObj = dataSnapshot.getValue(NotificationObj.class);
                msgNotif("A message from "+notificationObj.getName()+": "+notificationObj.getNotiMsg());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        firebase.addChildEventListener(handler);

        msgFirebase.addChildEventListener(msgHandle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void postNotif(String notifiString) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle(notifiString)
                .setContentText("Subject")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText("")).build();
        //  .addAction(R.drawable.line, "", pIntent).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, n);
    }

    private void msgNotif(String notifiString) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity_View.class);
        intent.putExtra("Service", "Chat");
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(notifiString)
                .setContentText("Subject")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText("")).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, n);
    }
}
