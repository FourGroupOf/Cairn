package com.GOF.cairn.ui.map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.IntentHelper;
import com.GOF.cairn.R;
import com.GOF.cairn.SavedPOI;
import com.GOF.cairn.ui.start.actLogin;

import java.util.ArrayList;

public class actAddL extends AppCompatActivity {

    public Button lbtnAdd, lbtnCancel;
    public EditText ledtLandmarkHe;
    IntentHelper helpIt = new IntentHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_l);
        assignComponents();


        lbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newLandMarkName = ledtLandmarkHe.getText().toString().trim();
                if (newLandMarkName.equals("") || newLandMarkName == null){
                    Toast.makeText(actAddL.this, "Please enter a name to save", Toast.LENGTH_SHORT).show();
                    return;
                }

                SavedPOI newPOI = new SavedPOI(newLandMarkName,AuthHold.getInstance().lati,AuthHold.getInstance().longi);

                if (AuthHold.getInstance().loggedInUser.lsFavLandmarks==null){
                    AuthHold.getInstance().loggedInUser.lsFavLandmarks = new ArrayList<>();
                }
                AuthHold.getInstance().loggedInUser.lsFavLandmarks.add(newPOI);
                Toast.makeText(actAddL.this, AuthHold.getInstance().updateUser(), Toast.LENGTH_SHORT).show();
                helpIt.openIntentHome(actAddL.this);
            }
        });
        lbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpIt.openIntentHome(actAddL.this);
            }
        });
    }

    private void assignComponents() {
        lbtnAdd = findViewById(R.id.btnAdd);
        lbtnCancel = findViewById(R.id.btnCancel);
        ledtLandmarkHe = findViewById(R.id.edtNewLandmarkName);

    }


}