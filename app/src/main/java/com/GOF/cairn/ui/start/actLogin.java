package com.GOF.cairn.ui.start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.IntentHelper;
import com.GOF.cairn.R;
import com.GOF.cairn.UserI;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class actLogin extends AppCompatActivity {

    EditText ledtUsername,ledtPassword;
    Button lbtnLogin,lbtnGoToRegister;
    private FirebaseAuth mAuth;

    IntentHelper helpIt = new IntentHelper();                                       // for switching forms
    public FirebaseDatabase database = FirebaseDatabase.getInstance();              // connect get my Fb instance (DB)
    public DatabaseReference myRefFitnessR;                                         // for user data record
    public DatabaseReference myRefUsers;



    @Override
    protected void onStart() {
        super.onStart();

        if (AuthHold.getInstance().fireUser != null) {  // checks if user is not already logged in.
            Toast.makeText(actLogin.this, "Welcome back " + AuthHold.getInstance().fireUser.getEmail(), Toast.LENGTH_SHORT).show();
            helpIt.openIntentHome(actLogin.this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);
        assignComponents();
        mAuth = FirebaseAuth.getInstance();                 //for authentication

        lbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String luEmail =  ledtUsername.getText().toString().trim();                                                                   //get email
                final String luPass = ledtPassword.getText().toString().trim();                                                                     //get password
                if (luEmail.equals("") || luPass.equals("")){
                    Toast.makeText(actLogin.this, "Enter details to login",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(luEmail,luPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {                                                   //firebase authentication methods
                        if (task.isSuccessful()) {                                                                                                  //success login
                            AuthHold.getInstance().fireUser = mAuth.getCurrentUser();                                                               //sets local instance of firebase user
                            Toast.makeText(actLogin.this, "Logged in " + mAuth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();  //displays welcome message

                            //////////////////////////////////////////////////////////////////////////////////////////////
                            //Find User Info
                            //////////////////////////////////////////////////////////////////////////////////////////////

                            myRefUsers = database.getReference("Users").child(AuthHold.getInstance().fireUser.getUid()).child("UserI");      //get the user's folder
                            myRefUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    for (DataSnapshot userValue : snapshot.getChildren()){
                                        AuthHold.getInstance().loggedInUser = userValue.getValue(UserI.class);                                    //converts snapshot to UserInfo Class and sets user to local instance
                                    }
                                    Toast.makeText(actLogin.this, "User Data Downloaded", Toast.LENGTH_SHORT).show();
                                    if (AuthHold.getInstance().loggedInUser != null){
                                        helpIt.openIntentHome(actLogin.this);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(actLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {                                                                                                                          //failed login
                            Toast.makeText(actLogin.this, "Credentials Incorrect, try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {                                                                                                           //retry login
                        Toast.makeText(actLogin.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        lbtnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpIt.openIntentRegister(actLogin.this);
            }
        });
    }



    private void assignComponents() {
        ledtPassword = findViewById(R.id.edtLoginPassword);
        ledtUsername = findViewById(R.id.edtLoginUsername);
        lbtnLogin = findViewById(R.id.btnLogin);
        lbtnGoToRegister = findViewById(R.id.btnGoToRegister);
    }
    private boolean isEmpty(String na,String psw){

        return na.equals("") || psw.equals("");
    }
}