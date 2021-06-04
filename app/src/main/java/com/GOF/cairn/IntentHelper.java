package com.GOF.cairn;

import android.content.Context;
import android.content.Intent;

import com.GOF.cairn.ui.start.actLogin;
import com.GOF.cairn.ui.start.actRegister;

public class IntentHelper { //helps to swap between screens

    public void openIntent(Context context, String name, int imgID){
        Intent i = new Intent(context,MainActivity.class);
        i.putExtra("name", name);
        i.putExtra("imgGeneral", imgID);
        context.startActivity(i);
    }
    public void openIntentLogin(Context context){
        Intent i = new Intent(context, actLogin.class);
        context.startActivity(i);
    }
    public void openIntentHome(Context context){
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    public void openIntentRegister(Context context) {
        Intent i = new Intent(context, actRegister.class);
        context.startActivity(i);
    }


//
//    public void logout(Context context){
//        Intent i = new Intent(context, actLogin.class);
//        context.startActivity(i);
//    }
}
