package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caroonline.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private Button btn_google_login;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            PlayerInfo.playerName = user.getDisplayName();
            Intent intent = new Intent(getApplicationContext(), MenuRoomActivity.class);//ban biet sao ng ta chi ban viet vay k.
            startActivity(intent);
        } // nên mỗi lần bạn chạy acticity này nó sẽ kiểm tra trc đó bạn đã đăng nhập chưa.ok
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String[] userStr = {};
        if (intent.getStringArrayExtra("user") != null) {
            userStr = intent.getStringArrayExtra("user");
            btn_login.setEnabled(true);
        }


        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_singin);
        btn_google_login = findViewById(R.id.btn_google_login);

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    btn_login.setEnabled(false);
                else btn_login.setEnabled(true);
            }
        });

        if (userStr.length != 0) {
            et_username.setText(userStr[0]);
            et_password.setText(userStr[1]);
        }


        btn_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_username.getText().toString();
                String password = et_password.getText().toString();
                checkUser(userName, password);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });

        createRequest();
    }

    private void createRequest() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken()); // khi ban dang nhap thanh cong thi nó sẽ luuw lại phiên đăng nhập này.


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "alo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "error";

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            PlayerInfo.playerName = user.getDisplayName();

                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show(); // oke la r ne,. nho dung may cai signin ( ke cả của fb) thì nhớ thêm sha1 key nha.
                            Intent intent =new Intent(getApplicationContext(),MenuRoomActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "a lo", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class); // intent giup ban chay 1 cai gi do trong andorid: activty, camera, bo suu tap,...ok, tham
        // so thi no can context, va 1 type: cu the o day la RegisterActivity
        startActivity(intent); // r thi start thoi.oki
    }


    private void checkUser(String userName, String password) {
        FirebaseSingleton.getInstance().databaseReference.child("user").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // cai nay ban lam sau nha// lam ,g thoing bao password sai
                User user = snapshot.getValue(User.class);// m bấm làm gì  // lam tiep di mi. chuyen qua lay string.sao phải chuyển , luc trước van chay bt ma, thi gio no v a. lấy
                if (user != null) {
                    if (user.getPassword().compareTo(password) == 0) {
                        login(userName);

                    } else {
                        et_password.setError("Password wrong"); // vi du v.
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Username or password is wrong", Toast.LENGTH_SHORT).show(); // mi lam cho du 3 th (dung, sai pass va sai username nha.// ko giai thich code gi hết à
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startMenuRoomActivity(String username) {
        Intent intent = new Intent(MainActivity.this, MenuRoomActivity.class);
        startActivity(intent);
    }

    private void login(String username) { // Hip. tao 1 activty khac cho sign up di. fragment cu tu tu. ok , ma cái chuyển acti
        PlayerInfo.playerName = username;
        startMenuRoomActivity(username);
    }


}