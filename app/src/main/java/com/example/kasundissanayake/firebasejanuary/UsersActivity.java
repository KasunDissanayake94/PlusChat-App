package com.example.kasundissanayake.firebasejanuary;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView mRecyclertView;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolBar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclertView = (RecyclerView) findViewById(R.id.users_list);
        mRecyclertView.setHasFixedSize(true);
        mRecyclertView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.uers_single_list,
                UsersViewHolder.class,
                mDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                viewHolder.setDisplayName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setThumb_Image(model.getImage(),getApplicationContext());
                final  String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profile_intend = new Intent(UsersActivity.this, ProfileActivity.class);
                        profile_intend.putExtra("user_id",user_id);
                        startActivity(profile_intend);
                    }
                });

            }
        };
        mRecyclertView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
        }
        public void setDisplayName(String name){
            TextView userNameText = (TextView) mView.findViewById(R.id.user_single_name);
            userNameText.setText(name);
        }
        public void setStatus(String status){
            TextView userStatus = (TextView) mView.findViewById(R.id.user_single_status);
            userStatus.setText(status);
        }
        public void setThumb_Image(String thumb_image, Context context){
            CircleImageView circleImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(context).load(thumb_image).placeholder(R.drawable.blank_user).into(circleImageView);
        }
    }
}
