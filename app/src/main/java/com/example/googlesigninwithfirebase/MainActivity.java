package com.example.googlesigninwithfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;
    BeginSignInRequest beginSignInRequest;

    private static final int RC_SIGN_IN = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    SignInClient oneTapClient;
    String idToken;

    Button buttonsignin;
    TextView textView;

    CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonsignin = findViewById(R.id.buttonsignin);
        textView = findViewById(R.id.text);
        callbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();

        //FirebaseAuth.getInstance().signOut();

       /* if(mAuth !=null){

            //startActivity(new Intent(MainActivity.this,Signin.class));
            FirebaseUser user = mAuth.getCurrentUser();

            textView.setText(user.getDisplayName()+" "+user.getEmail()+" "+user.getPhotoUrl());

        }else{

            Toast.makeText(MainActivity.this, "Please signin and tray again", Toast.LENGTH_SHORT).show();
        }*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn){

           /* startActivity(new Intent(MainActivity.this,LoginSuccessImage.class));
            finish();*/

            //AccessToken accessToken = AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code

                            try{

                                String fullname = object.getString("name");
                                Toast.makeText(MainActivity.this, fullname, Toast.LENGTH_SHORT).show();

                            }catch(JSONException e){

                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();

        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        //handleFacebookAccessToken(loginResult.getAccessToken());

                        Toast.makeText(MainActivity.this, "facebook:onSuccess:", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code

                        Log.d(TAG, "facebook:onCancel");

                        Toast.makeText(MainActivity.this, "facebook:onCancel1", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code

                        Log.d(TAG, "facebook:onCancel");

                        Toast.makeText(MainActivity.this, "facebook:onCancel", Toast.LENGTH_SHORT).show();
                    }
                });

        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //signIn();

                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));

            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



            }
        },2000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("hsguygj","firebaseauthwithgoogle"+account.getId());

                    //firebaseauthwithgoogle(account.getIdToken());

            } catch (ApiException e) {
                Toast.makeText(this, "Google signin is failed", Toast.LENGTH_SHORT).show();
            }
            //handleSignInResult(task);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseUser currentUser1 = mAuth.getCurrentUser();
        //updateUI(currentUser);

       // Log.d("hfydhghgc",currentUser.toString());
        //updateUI(currentUser);
    }

   /* public void firebaseauthwithgoogle(String idToken){

        if (idToken !=  null) {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(MainActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                                textView.setText(user.getDisplayName()+" "+user.getEmail()+" "+user.getPhoneNumber());
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                                //updateUI(null);

                                Toast.makeText(MainActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }*/

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user){

        if (user != null){

            Toast.makeText(this, "Login success fully", Toast.LENGTH_SHORT).show();
        }

    }

}