package com.example.a1406074.grivancecell.login;
import java.lang.String;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.a1406074.grivancecell.ui.Chat2Activity;
import com.example.a1406074.grivancecell.ui.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a1406074.grivancecell.FireChatHelper.ChatHelper;
import com.example.a1406074.grivancecell.R;
import com.example.a1406074.grivancecell.register.RegisterActivity;
import com.example.a1406074.grivancecell.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginUserActivity extends AppCompatActivity {

    private static final String TAG = LogInActivity.class.getSimpleName();
    private EditText mUserEmail;
    private EditText mUserPassword;
    //    @BindView(R.id.edit_text_email_login) EditText mUserEmail;
    //  @BindView(R.id.edit_text_password_log_in) EditText mUserPassWord;
    private Button Login_Button;
    private Button Register_Button;
    private ProgressDialog mProg;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;
    private DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Holo);
        setContentView(R.layout.activity_log_in);

        mdatabase = FirebaseDatabase.getInstance().getReference();
        mProg = new ProgressDialog(LoginUserActivity.this);
        Login_Button = (Button) findViewById(R.id.btn_login);
        Register_Button = (Button) findViewById(R.id.btn_register);
        mUserEmail = (EditText) findViewById(R.id.edit_text_email_login);
        mUserPassword = (EditText) findViewById(R.id.edit_text_password_log_in);
        mAuth = FirebaseAuth.getInstance();


        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Register = new Intent(LoginUserActivity.this, RegisterActivity.class);
                startActivity(Register);

            }
        });

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Database Link", mdatabase.getRef().toString());


                String Email = mUserEmail.getText().toString().trim();
                String Password = mUserPassword.getText().toString().trim();
                if (!(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password))) {

                    mProg.setMessage("Signing you in....");
                    mProg.show();
                    mAuth.signInWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            mProg.dismiss();

                            Intent main_Activity = new Intent(LoginUserActivity.this, Chat2Activity.class);
                            finish();
                            startActivity(main_Activity);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            mProg.dismiss();
                            Toast.makeText(LoginUserActivity.this, "Can't sign you in.." + e.getLocalizedMessage()
                                    , Toast.LENGTH_SHORT).show();


                        }
                    });


                } else {
                    Toast.makeText(LoginUserActivity.this, "Can't sign you in", Toast.LENGTH_SHORT).show();
                }


            }





    });


}
}