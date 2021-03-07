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

public class RegisterActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_confirm_password;
    private Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //edit title
        setTitle("Đăng ký");

        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        btn_signin = findViewById(R.id.btn_singin);

        et_username.addTextChangedListener(new ValidatedTextInput(et_username));
        et_password.addTextChangedListener(new ValidatedTextInput(et_password));
        et_confirm_password.addTextChangedListener(new ValidatedTextInput(et_confirm_password));
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();//loi day chu dau :)))// nanminh chi bán lan r ma., uk1 mà chua sửa ở đây , nó cung ko báo lỗi gì , nên ko biết , quen cái get
                register(new User(username, password));
                startMainActivity(username,password);
                Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show(); // mi lam cho du 3 th (dung, sai pass va sai username nha.// ko giai thich code gi hết à

            }
        });
    }

    private void register(User user) {
        FirebaseSingleton.getInstance().insert(user);
    }

    private void startMainActivity(String username,String password) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class); // intent giup ban chay 1 cai gi do trong andorid: activty, camera, bo suu tap,...ok, tham

        // so thi no can context, va 1 type: cu the o day la RegisterActivity
        String[] user = {username, password};
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void checkUserName() {
        String userName = et_username.getText().toString();
        if (userName.isEmpty()) {
            et_username.setError("This field can not be blank"); // ban xu li coi ban//no lúc lên lúc ko á hả//
        }
        else {
            FirebaseSingleton.getInstance().databaseReference.child("user").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null)
                        et_username.setError("Username has exist.");
                    else et_username.setError(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void checkConfirmPassword() {
        String password = et_password.getText().toString();
        String confirmPassword = et_confirm_password.getText().toString();
        if (password.compareTo(confirmPassword) == 0) {
            et_confirm_password.setError(null);
            btn_signin.setEnabled(true);
        } else {
            et_confirm_password.setError("Confirm password does not match");
            btn_signin.setEnabled(false);
        }
    }


    private class ValidatedTextInput implements TextWatcher {
        private View view;

        public ValidatedTextInput(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.et_username:
                    checkUserName();
                    break;
                case R.id.et_password: // ban phai bo break di de no chay xuong // break là bạn mới them mà, minh co break đâu nhỉ . ban cu doc ve switch break di.
                    break;

                case R.id.et_confirm_password:
                    checkConfirmPassword();
                    break;
            }
        }

    }


}