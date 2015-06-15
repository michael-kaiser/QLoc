package com.example.qloc.controller.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.BitMapWorkerTask;
import com.example.qloc.model.data.HttpFacade;
import com.example.qloc.model.exceptions.BadLoginException;
import com.example.qloc.model.exceptions.BadRegistrationException;

public class LoginActivity extends Activity{

    private EditText email;
    private EditText pwd;
    private TextView errorMsg;
    private boolean regSuccess;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView back1 = (ImageView) findViewById(R.id.map_background);
        ImageView back2 = (ImageView) findViewById(R.id.map_background2);
        BitMapWorkerTask.loadBitmap(R.drawable.map_top, back1, this);
        BitMapWorkerTask.loadBitmap(R.drawable.map_lower,back2, this);
        email = (EditText) findViewById(R.id.login_email);
        pwd = (EditText) findViewById(R.id.login_pwd1);
        errorMsg = (TextView) findViewById(R.id.error_message);

    }

    public void onButtonRegister(View v){
        v.setAlpha(1.0f);
        try {
            registrationDialog();
        } catch (BadRegistrationException e) {
            Log.d(TAG, "Bad registration");
        }

    }

    public void onButtonLogin(View v){
        CookieManager.getInstance().removeAllCookie();
        v.setAlpha(1.0f);
        try {
            login();
        } catch (BadLoginException e) {
            errorMsg.setVisibility(View.VISIBLE);
        }
    }

    private void registrationDialog() throws BadRegistrationException{
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.registration_dialog);
        final EditText email = (EditText) dialog.findViewById(R.id.reg_email);
        final EditText pwd = (EditText) dialog.findViewById(R.id.reg_pwd);
        final EditText pwd2 = (EditText) dialog.findViewById(R.id.reg_pwd2);
        final TextView errorMail = (TextView) dialog.findViewById(R.id.error_message_mail_dialog);
        final TextView errorPwd = (TextView) dialog.findViewById(R.id.error_message_pwd_dialog);
        final ImageButton buttonReg = (ImageButton) dialog.findViewById(R.id.reg_btn);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String strPwd = pwd.getText().toString();
                String strPwd2 = pwd2.getText().toString();
                if(checkPwd(strPwd,strPwd2)){
                    errorPwd.setVisibility(View.INVISIBLE);
                    regSuccess = registration(mail, strPwd);
                    dialog.dismiss();
                }else{
                    errorPwd.setVisibility(View.VISIBLE);
                }

            }
        });
        dialog.show();
        if(!regSuccess){
            throw new BadRegistrationException();
        }else{
            Intent i = new Intent(this, MainScreen.class);
            startActivity(i);
        }

    }
    private void login() throws BadLoginException {
        boolean check = HttpFacade.getInstance().login(email.getText().toString(), pwd.getText().toString());
        if(!check){
            throw new BadLoginException();
        }else {
            ProfileActivity.setUsername(email.getText().toString());
            Intent i = new Intent(this, MainScreen.class);
            startActivity(i);
        }

    }

    private boolean registration(String email, String pwd){
        return HttpFacade.getInstance().register(email, pwd);
    }

    private boolean checkPwd(String pwd1, String pwd2){
        if(pwd1.equals(pwd2)){
            return true;
        }
        return false;
    }
}
