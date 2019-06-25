package com.example.airtelsarvesh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThirdActivity extends Activity {
        EditText GetName,GetPhoneNumber, GetEmail, GetCountry, GetPassword, GetCpassword;
        Button Submit ;
        String Name, PhoneNumber, Email, Country, Password, Cpassword ;
        Boolean CheckEditTextEmpty ;
        FirebaseAuth firebaseAuth;
        private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        GetName = (EditText)findViewById(R.id.name);
        GetEmail = (EditText)findViewById(R.id.email);
        GetPhoneNumber = (EditText)findViewById(R.id.pno);
        GetCountry = (EditText)findViewById(R.id.country);
        GetPassword = (EditText)findViewById(R.id.pass);
        GetCpassword = (EditText)findViewById(R.id.cpass);
        Submit = (Button)findViewById(R.id.submit);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot( Name,PhoneNumber, Email, Country, Password, Cpassword);
                Email = GetEmail.getText().toString();
                Password = GetPassword.getText().toString();
                Cpassword = GetCpassword.getText().toString();

                if(CheckEditTextEmpty == true)
                {
                    if (!isValidEmail(Email)) {
                        Toast.makeText(ThirdActivity.this,"Invalid Email", Toast.LENGTH_LONG).show();
                    }

                    else if (!isValidPassword(Password)) {
                        Toast.makeText(ThirdActivity.this, "Password must be more than six characters", Toast.LENGTH_LONG).show();
                    }
                    else if(!isPasswordMatch(Password,Cpassword))
                    {
                        Toast.makeText(ThirdActivity.this, "Passwords must match", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(ThirdActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
                        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                //checking if success
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                                }else{
                                    Toast.makeText(ThirdActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                        ClearEditTextAfterDoneTask();
                        redirect(v);
                    }

                }
                else {

                    Toast.makeText(ThirdActivity.this,"Please Fill All the Fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void CheckEditTextIsEmptyOrNot(String Name,String PhoneNumber, String Email, String Country, String Password, String Cpassword ){

        Name = GetName.getText().toString();
        Email = GetEmail.getText().toString();
        PhoneNumber = GetPhoneNumber.getText().toString();
        Country = GetCountry.getText().toString();
        Password = GetPassword.getText().toString();
        Cpassword = GetCpassword.getText().toString();

        if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(PhoneNumber) || TextUtils.isEmpty(Email)|| TextUtils.isEmpty(Country)|| TextUtils.isEmpty(Password)|| TextUtils.isEmpty(Cpassword)){

            CheckEditTextEmpty = false ;

        }
        else {
            CheckEditTextEmpty = true ;
        }
    }

    public void ClearEditTextAfterDoneTask(){

        GetName.getText().clear();
        GetPhoneNumber.getText().clear();
        GetCountry.getText().clear();
        GetEmail.getText().clear();
        GetPassword.getText().clear();
        GetCpassword.getText().clear();

    }
    public void redirect(View v) {
        String button_text;
        button_text = ((Button) v).getText().toString();
        if (button_text.equals("Create Account")) {
            Intent i = new Intent(this, com.example.airtelsarvesh.MainActivity.class);
            startActivity(i);
        }
    }
    private boolean isPasswordMatch(String password,String cpassword)
    {
        if(password.equals(cpassword))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isValidEmail(String Email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    private boolean isValidPassword(String Password) {
        if (Password != null && Password.length() > 10) {
            return true;
        }
        return false;
    }
}