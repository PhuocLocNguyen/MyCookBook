package com.example.kaildyhoang.mycookbookapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kaildyhoang.mycookbookapplication.models.NotificationObj;
import com.example.kaildyhoang.mycookbookapplication.models.User;
import com.example.kaildyhoang.mycookbookapplication.services.FirebaseBackgroundService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference notificationDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private String uID;
    private Calendar calendar;
    private FirebaseRecyclerAdapter<NotificationObj, NotificationActivity.NotificationViewHolder> mNotificationAdapter;
    private TextView _txtVNothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        startService(new Intent(this,FirebaseBackgroundService.class));
        initialiseScreen();
    }
    private void initialiseScreen(){
        _txtVNothing = (TextView) findViewById(R.id.textViewNothing);

        notificationDatabaseRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        Toast.makeText(NotificationActivity.this,"Wait! Fetching list...", Toast.LENGTH_SHORT).show();

        loadRecy();
    }
    private void loadRecy(){
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        uID = userAuth.getUid();
        calendar = Calendar.getInstance();
        final DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        notificationDatabaseRef.child("users/"+uID+"/notifications/general_notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long length = dataSnapshot.getChildrenCount();
                if(length == 0){
                    _txtVNothing.setVisibility(View.VISIBLE);
                }else{
                    _txtVNothing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mNotificationAdapter = new FirebaseRecyclerAdapter<NotificationObj, NotificationViewHolder>(
                NotificationObj.class,
                R.layout.activity_friend_items,
                NotificationViewHolder.class,
                notificationDatabaseRef.child("users/"+uID+"/notifications/general_notifications"))
        {
            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, final NotificationObj model, final int position) {
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = new Date( model.getNotiAt() * 1000L + TimeZone.getDefault().getRawOffset());

                viewHolder.userName(model.getName());
                viewHolder.notiMsg(model.getNotiMsg());
                viewHolder.avatarPicture(model.getAvatar());
                viewHolder.atDate(simpleDateFormat.format(date));

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String notiKey = mNotificationAdapter.getRef(position).getKey();
                        Map<String, Object> isReaded = new HashMap<String, Object>();
                        isReaded.put("readed", 1);
                        notificationDatabaseRef.child("users/"+uID+"/notifications/general_notifications/").child(notiKey).updateChildren(isReaded);

                        String keyUser = model.getNotiId();
                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keyFriend",keyUser);
                        intent.putExtra("MyBundleFromSearch",bundle);
                        startActivity(intent);
                    }
                });
            }


        };
        recyclerView.setAdapter(mNotificationAdapter);
        mNotificationAdapter.notifyDataSetChanged();
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder{

        private final TextView  _txtVUserName, _txtVNotiMsg, _txtVAtDate;
        private final ImageView _imgVAvatar;

        public NotificationViewHolder(final View itemView) {
            super(itemView);

            //TextView
            _txtVUserName = (TextView) itemView.findViewById(R.id.textViewEmailItem);
            _txtVNotiMsg = (TextView) itemView.findViewById(R.id.textViewUserName);
            _txtVAtDate = (TextView) itemView.findViewById(R.id.textViewAtDateItem);

            //ImageView
            _imgVAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);

        }
        private void userName(String title){_txtVUserName.setText(title);
        }
        private void atDate(String title){_txtVAtDate.setText(title);
        }
        private void notiMsg(String title){_txtVNotiMsg.setText(title);
        }
        private void avatarPicture(String title){
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    .placeholder(R.drawable.load_icon)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(_imgVAvatar);
        }
    }
}
