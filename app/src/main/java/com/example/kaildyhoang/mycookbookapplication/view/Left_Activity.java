package com.example.kaildyhoang.mycookbookapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kaildyhoang.mycookbookapplication.ChatActivity;
import com.example.kaildyhoang.mycookbookapplication.R;
import com.example.kaildyhoang.mycookbookapplication.models.Follow;
import com.example.kaildyhoang.mycookbookapplication.models.NotificationObj;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Left_Activity extends Fragment{
    private RecyclerView recyclerView;
    private DatabaseReference followDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Follow,FollowViewHolder> mFollowdapter;

    public Left_Activity() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v_activity_left,container,false);

        mAuth = FirebaseAuth.getInstance();

        followDatabaseRef = FirebaseDatabase.getInstance().getReference();
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();

        mFollowdapter = new FirebaseRecyclerAdapter<Follow, FollowViewHolder>(
                Follow.class,
                R.layout.activity_friend_items,
                FollowViewHolder.class,
                followDatabaseRef.child("users").child(uid).child("idFriendsList"))
        {
            @Override
            protected void populateViewHolder(final FollowViewHolder viewHolder, final Follow model, int position) {
                final String notiKey = mFollowdapter.getRef(position).getKey();
                viewHolder.avatar(model.getAvatar());
                viewHolder.name(model.getName());
                viewHolder.email(model.getEmail());
                followDatabaseRef.child("users").child(uid).child("notifications").child("chat_notifications")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int badgeCount = 0;
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            NotificationObj notificationObj = childDataSnapshot.getValue(NotificationObj.class);
                            if(notificationObj.getReaded() == 0 && notificationObj.getNotiId().equalsIgnoreCase(notiKey)){
                                ++badgeCount;
                            }
                        }
                        if(badgeCount != 0){
                            viewHolder._txtVBadgeChat.setVisibility(View.VISIBLE);
                            viewHolder._txtVBadgeChat.setText(String.valueOf(badgeCount));
                        }else{
                            viewHolder._txtVBadgeChat.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    followDatabaseRef.child("users").child(uid).child("notifications").child("chat_notifications")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                NotificationObj notificationObj = childDataSnapshot.getValue(NotificationObj.class);
                                if(notificationObj.getReaded() == 0 && notificationObj.getNotiId().equalsIgnoreCase(notiKey)){
                                    Map<String, Object> isReaded = new HashMap<String, Object>();
                                    isReaded.put("readed", 1);
                                    followDatabaseRef.child("users/"+uid+"/notifications/chat_notifications/").child(childDataSnapshot.getKey()).removeValue();
                                }
                            }
                           }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    String keyFriend = model.getIdFriend();
                    Intent intent = new Intent(getContext(),ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyFriend",keyFriend);
                    intent.putExtra("MyBundleForChat",bundle);
                    startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(mFollowdapter);
        mFollowdapter.notifyDataSetChanged();
    }
    //    View holder for recycler view
    public static class FollowViewHolder extends RecyclerView.ViewHolder{

        private final TextView _txtVName,_txtVEmail, _txtVBadgeChat;
        private final ImageView _imgVAvatar;

        public FollowViewHolder(final View itemView) {
            super(itemView);
            //TextView
            _txtVName = (TextView) itemView.findViewById(R.id.textViewUserName);
            _txtVEmail = (TextView) itemView.findViewById(R.id.textViewEmailItem);
            _txtVBadgeChat = (TextView) itemView.findViewById(R.id.textViewBadgeChatMsg);

            _imgVAvatar=(ImageView) itemView.findViewById(R.id.imageViewAvatar);

        }
        private void badgeChat(String title){_txtVBadgeChat.setText(title);
        }
        private void name(String title){_txtVName.setText(title);
        }
        private void email(String title){_txtVEmail.setText(title);
        }
        private void avatar(String title){
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