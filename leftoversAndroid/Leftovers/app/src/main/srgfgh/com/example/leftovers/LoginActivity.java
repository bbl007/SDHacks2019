package com.example.leftovers;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    /*
   In this method control is passed to this class from the splash screen
   if the user has already logged in on this device then the app will just go
   straight to the the map.
    */
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent launchMap = new Intent(this, MainActivity.class);
            startActivity(launchMap);
            finish();
        }
    }


    /*
    This method sets the content view of the login page. from the login page
    the user can reset their password if they forgot. They can also register
    an account if they have never created an account with us before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
    }


    /*
    This method launches the forgot password activity

    protected void forgotPass(View view) {
        Intent forgotPassView = new Intent(this,
                ForgotPassActivity.class);
        startActivity(forgotPassView);
    }
*/

    /*
    This method handles the login of the application. the useres credentials
    are stored in Firebase and if the login fails the user will be notified
    with a login failed Toast message.
     */
    protected void login(View view) {
        mAuth = FirebaseAuth.getInstance();

        EditText emailText = (EditText) findViewById(R.id.loginEmail);
        EditText passText = (EditText) findViewById(R.id.loginPass);

        String email = emailText.getText().toString();
        String password = passText.getText().toString();

        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the
                                // signed-in user's information
                                Log.d("LOGIN", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                mAuth = FirebaseAuth.getInstance();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LOGIN","signInWithEmail:failure",
                                        task.getException());
                                Toast.makeText(LoginActivity.this,
                                        "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Please enter all information.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /*
    This method handles the launch registration activity if the user wishes to
    create an account with our application User would create an account with us
    to save their filter preferences.
     */
    protected void launchRegistration(View view) {
        System.out.println("LAUNCH REGISTRATION");
        Intent launchRegistration = new Intent(this,
                RegistrationActivity.class);
        startActivity(launchRegistration);
    }

    /*
    App will close if back button is pressed at the login screen
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}
