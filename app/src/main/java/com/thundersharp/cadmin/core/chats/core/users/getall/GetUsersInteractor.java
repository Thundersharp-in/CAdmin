package com.thundersharp.cadmin.core.chats.core.users.getall;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.core.chats.model.Chat;
import com.thundersharp.cadmin.core.chats.model.User;
import com.thundersharp.cadmin.core.chats.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GetUsersInteractor implements GetUsersContract.Interactor {
    private static final String TAG = "GetUsersInteractor";

    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;

    private GetUsersContract.OnGetChatUsersListener mOnchatUserListner;



    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }




    @Override
    public void getAllUsersFromFirebase() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();


                List<User> users = new ArrayList<>();

                while (dataSnapshots.hasNext()) {

                   // Iterator<DataSnapshot> datafinal = dataSnapshots.next().child("personal_data").getChildren().iterator();

                    DataSnapshot dataSnapshotChild = dataSnapshots.next().child("personal_data");

                    dataSnapshotChild.getRef();

                    User user= dataSnapshotChild.getValue(User.class);


                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatUsersFromFirebase() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.ARG_CHAT_ROOMS)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chat chat = new Chat();
                final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
                final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;

                Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();
                List<User> users=new ArrayList<>();
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    dataSnapshotChild.getRef();
                    User user = dataSnapshot.getValue(User.class);

                    if (dataSnapshot.hasChild(room_type_1 )|| dataSnapshotChild.hasChild(room_type_2)) {
                        users.add(user);
                    }

                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
}
