package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String[] userStr = {};
        if (intent.getStringArrayExtra("user") != null) {
            userStr = intent.getStringArrayExtra("user");
        }


        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_singin);

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

        if(userStr.length != 0 ){
            et_username.setText(userStr[0]);
            et_password.setText(userStr[1]);
        }

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
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class); // intent giup ban chay 1 cai gi do trong andorid: activty, camera, bo suu tap,...ok, tham
        // so thi no can context, va 1 type: cu the o day la RegisterActivity
        startActivity(intent); // r thi start thoi.oki
    }

    private void startMenuRoomActivity(String username) { // password nen giu kin ti mi
        Intent intent = new Intent(MainActivity.this, MenuRoomActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void checkUser(String userName, String password) {
        FirebaseSingleton.getInstance().databaseReference.child("user").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // cai nay ban lam sau nha// lam ,g thoing bao password sai
                User user = snapshot.getValue(User.class);
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

    private void login(String username) { // Hip. tao 1 activty khac cho sign up di. fragment cu tu tu. ok , ma cái chuyển acti
        startMenuRoomActivity(username);
    }


}