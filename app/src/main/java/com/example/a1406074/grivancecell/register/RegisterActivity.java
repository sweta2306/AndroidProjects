package com.example.a1406074.grivancecell.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.a1406074.grivancecell.login.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a1406074.grivancecell.FireChatHelper.ChatHelper;
import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.model.User;
import com.example.a1406074.grivancecell.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.edit_text_name) EditText mUserName;
    @BindView(R.id.edit_text_email_register) EditText mUserEmailRegister;
    @BindView(R.id.edit_text_password_register) EditText mUserPassWordRegister;
    @BindView(R.id.edit_text_location)EditText getmUserLocation;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AlertDialog dialog;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        //setTheme(android.R.style.Theme_Holo);
        hideActionBar();
        bindButterKnife();
        setAuthInstance();
        setDatabaseInstance();
    }

    private void hideActionBar() {
       this.getSupportActionBar().hide();
    }

    private void bindButterKnife() {
        ButterKnife.bind(this);
    }

    private void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setDatabaseInstance() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.btn_register_user)
    public void registerUserClickListener(Button button) {
        onRegisterUser();
    }

    @OnClick(R.id.btn_cancel_register)
    public void cancelClickListener(Button button) {
        finish();
    }

    private void onRegisterUser() {
        if(getUserName().equals("") || getUserEmail().equals("") || getUserPassword().equals("")){
            showFieldsAreRequired();
        }else if(isIncorrectEmail(getUserEmail()) || isIncorrectPassword(getUserPassword())) {
            showIncorrectEmailPassword();
        }else {
            signUp(getUserEmail(), getUserPassword());
        }
    }

    private boolean isIncorrectEmail(String userEmail) {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
    }

    private boolean isIncorrectPassword(String userPassword) {
        return !(userPassword.length() >= 6);
    }

    private void showIncorrectEmailPassword() {
        showAlertDialog(getString(R.string.error_incorrect_email_pass), true);
    }

    private void showFieldsAreRequired() {
        showAlertDialog(getString(R.string.error_fields_empty), true);
    }

    private void showAlertDialog(String message, boolean isCancelable){

        dialog = ChatHelper.buildAlertDialog(getString(R.string.login_error_title),message,isCancelable,RegisterActivity.this);
        dialog.show();
    }

    private String getUserName() {
        return mUserName.getText().toString().trim();
    }

    private String getUserEmail() {
        return mUserEmailRegister.getText().toString().trim();
    }

    private String getUserPassword() {
        return mUserPassWordRegister.getText().toString().trim();
    }
    private String getUserLocation() {
        return getmUserLocation.getText().toString().trim();
    }


    private void signUp(final String email, String password) {

       // showAlertDialog("Registering...",true);
        mProgress.setMessage("Registering you....");
        mProgress.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                DatabaseReference Users_ref=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

                String date= new SimpleDateFormat("dd/mm/yyyy hh:mma").format(new Date(System.currentTimeMillis()));

                Log.v("Date",date);

                Users_ref.child("displayName").setValue(mUserName.getText().toString().trim());
                Users_ref.child("email").setValue(email);
                Users_ref.child("createdAt").setValue(date);
                Users_ref.child("avatarId").setValue("1");
                Users_ref.child("uid").setValue(mAuth.getCurrentUser().getUid());
                Users_ref.child("connection").setValue("online").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mProgress.dismiss();
                        Intent Login =new Intent(RegisterActivity.this, LogInActivity.class);
                        finish();
                        startActivity(Login);

                    }
                });







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                mProgress.dismiss();
                Log.v("Error",e.getLocalizedMessage());

            }
        });
        /*.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                dismissAlertDialog();

                if(task.isSuccessful()){
                    onAuthSuccess(task.getResult().getUser());
                }else {
                    showAlertDialog(task.getException().getMessage(), true);
                }
            }
        }); */
    }

    private void dismissAlertDialog() {
        dialog.dismiss();
    }

 /*   private void onAuthSuccess(FirebaseUser user) {
        createNewUser(user.getUid());
        goToMainActivity();
    }*/

   /* private void goToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    } */

  /*  private void createNewUser(String userId){
        User user = buildNewUser();
        mDatabase.child("users").child(userId).setValue(user);
    } */

  /*  private User buildNewUser() {
        return new User(
                getUserDisplayName(),
                getUserEmail(),
                UsersChatAdapter.ONLINE,
                ChatHelper.generateRandomAvatarForUser(),
                new Date().getTime()
        );
    }*/

}