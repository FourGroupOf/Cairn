package com.GOF.cairn.ui.start;
//ctrl alt L

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.IntentHelper;
import com.GOF.cairn.R;
import com.GOF.cairn.SavedPOI;
import com.GOF.cairn.UserI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class actRegister extends AppCompatActivity {

    EditText ledtRegisterEmail, ledtRegisterPassword, ledtRegisterPasswordCon,ledtRegisterHomeAddr;
    Button lbtnGoToLogin, lbtnRegister;
    RadioButton lrbtnMetricReg,lrbtnImperialReg;
    RadioGroup lrdgMSystemReg;
    Spinner lspedtFavLandmark;

    UserI newUser;
    public View view;


    IntentHelper helpIt = new IntentHelper();
    private FirebaseAuth mAuth;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();                      //connect get my Fb instance (DB)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_register);
        assignComponents();
        mAuth = FirebaseAuth.getInstance();

        lbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!captureDetails()){
                    Toast.makeText(actRegister.this, "Please make sure all details are entered and in correct format ",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(ledtRegisterEmail.getText().toString().trim(), ledtRegisterPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {                                          // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(actRegister.this, "Welcome to Cairn: " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                    AuthHold.getInstance().fireUser = mAuth.getCurrentUser();       // Sets instance for local global use

                                    //Store UserData
                                   DatabaseReference myRefUsersS = database.getReference("Users").child(AuthHold.getInstance().fireUser.getUid()).child("UserI");
                                    myRefUsersS.push().setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(actRegister.this, "User Registered", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(actRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    helpIt.logout(actRegister.this); //logs user out after register to ensure proper login flow

                                } else {                                                                        // If registration fails
                                    Toast.makeText(actRegister.this, "Reg Auth failed. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(e -> Toast.makeText(actRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                helpIt.logout(actRegister.this); //logs user out after register to ensure proper login flow
            }
        });



        lbtnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpIt.openIntentLogin(actRegister.this);
            }
        });
    }
    private boolean captureDetails(){  //captures inputs and returns false if invalid

        final String luEmail = ledtRegisterEmail.getText().toString().trim();
        final String luPass = ledtRegisterPassword.getText().toString().trim();
        final String luPassC = ledtRegisterPasswordCon.getText().toString().trim();

         if (!luPass.equals(luPassC)){  //checks if password = confirmPassword
             Toast.makeText(actRegister.this, "Passwords do not match - try again", Toast.LENGTH_SHORT).show();
             return false;
         }

        final String luHomeAddr = ledtRegisterHomeAddr.getText().toString().trim();
        final boolean luMS = lrbtnMetricReg.isChecked();
        final String luFaveLand = lspedtFavLandmark.getSelectedItem().toString();

        if (luEmail.equals("") || luPassC.equals("") ||luHomeAddr.equals("")){
            Toast.makeText(actRegister.this, "Please ensure all details entered", Toast.LENGTH_SHORT).show();
            return false;
        }
        List<SavedPOI> lsFL = new ArrayList<>();
        newUser = new UserI(luEmail,luFaveLand,luHomeAddr,lsFL,luMS);
        return true;
    }

    private void assignComponents() {
        ledtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        ledtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        ledtRegisterPasswordCon = findViewById(R.id.edtRegisterConPassword);

        lbtnGoToLogin = findViewById(R.id.btnGoToLogin);

        lrbtnMetricReg = findViewById(R.id.rdbtnMetricReg);
        lrbtnImperialReg = findViewById(R.id.rdbtnImperialReg);
        lrdgMSystemReg = findViewById(R.id.rdgMSystemReg);
        lrbtnMetricReg.setChecked(true);

        ledtRegisterHomeAddr = findViewById(R.id.edtRegisterHomeAddr);
        lbtnRegister = findViewById(R.id.btnRegisterFull);

        lspedtFavLandmark = findViewById(R.id.spedtFavLandmark);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.arrLandmarkTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                         // Specify the layout to use when the list of choices appears
        lspedtFavLandmark.setAdapter(adapter);                                                                  // Apply the adapter to the spinner


    }
}