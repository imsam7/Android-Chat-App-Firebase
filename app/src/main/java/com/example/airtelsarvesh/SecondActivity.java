package com.example.airtelsarvesh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends Activity {
    EditText GetPassword, GetEmail;
    Button Submit;
    String Password, Email;
    Boolean CheckEditTextEmpty;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    public final static String MESSAGE_KEY ="send_email";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        GetPassword = (EditText) findViewById(R.id.pass);
        GetEmail = (EditText) findViewById(R.id.email_address);
        Submit = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();

/*        if(firebaseAuth.getCurrentUser() != null){
            finish();

            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
        }*/
        progressDialog = new ProgressDialog(this);

        Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot(Password, Email);
                Password = GetPassword.getText().toString();
                Email = GetEmail.getText().toString();
                if (CheckEditTextEmpty == true) {
                    if (!isValidEmail(Email)) {
                        Toast.makeText(SecondActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                    } else if (!isValidPassword(Password)) {
                        Toast.makeText(SecondActivity.this, "Password must be more than six characters", Toast.LENGTH_LONG).show();
                    } else {

                        firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                //if the task is successfull
                                if (task.isSuccessful()) {
                                    //start the profile activity
                                    Toast.makeText(SecondActivity.this, "Logging In", Toast.LENGTH_LONG).show();
                                    ClearEditTextAfterDoneTask();
                                    startActivity(new Intent(getApplicationContext(), chat.class));
                                } else {
                                    Toast.makeText(SecondActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {

                    Toast.makeText(SecondActivity.this, "Please Fill All the Fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(String Password, String Email) {

        Password = GetPassword.getText().toString();
        Email = GetEmail.getText().toString();

        if (TextUtils.isEmpty(Password) || TextUtils.isEmpty(Email)) {

            CheckEditTextEmpty = false;

        } else {
            CheckEditTextEmpty = true;
        }
    }

    public void ClearEditTextAfterDoneTask() {

        GetPassword.getText().clear();
        GetEmail.getText().clear();
    }

    private boolean isValidEmail(String Email) {
        String EMAIL_PATTERN = "^^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String Password) {
        if (Password != null && Password.length() > 6) {
            return true;
        }
        return false;
    }

    public void redirect(View v) {
        String button_text;
        button_text = ((Button) v).getText().toString();
        if (button_text.equals("Login")) {
            Intent i = new Intent(this, com.example.airtelsarvesh.chat.class);
            i.putExtra(MESSAGE_KEY,Email);
            startActivity(i);
        }
    }
}