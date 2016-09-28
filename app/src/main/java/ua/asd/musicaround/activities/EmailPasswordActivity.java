package ua.asd.musicaround.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import ua.asd.musicaround.R;
import ua.asd.musicaround.core.firebase.FirebaseManager;


public class EmailPasswordActivity extends BaseActivity {
    private EditText vEditName;
    private EditText vEditEmail;
    private EditText vEditPassword;
    private EditText vEditPhone;
    private Button vGetStartedButton;
    private FirebaseDatabase mFirebaseDB;


    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        vEditName = (EditText) findViewById(R.id.edit_name);
        vEditEmail = (EditText) findViewById(R.id.edit_email);
        vEditPassword = (EditText) findViewById(R.id.edit_pass);
        vEditPhone = (EditText) findViewById(R.id.edit_phone);
        vGetStartedButton = (Button) findViewById(R.id.get_started_button);
        vGetStartedButton.setOnClickListener(this);
    }

    private void createAccount(String name, String email, String password, String phone) {
        FirebaseManager.getInstance().createUser(name, email, password, phone, null);
    }


    @Override
    public void onClick(View view) {
        String name = vEditName.getText().toString();
        String email = vEditEmail.getText().toString();
        String password = vEditPassword.getText().toString();
        String phone = vEditPhone.getText().toString();
        if (validateForm()) {
            createAccount(name, email, password, phone);
        }
    }

    private boolean validateForm() {
        Toast toast = Toast.makeText(EmailPasswordActivity.this, "Required", Toast.LENGTH_SHORT);
        String name = vEditName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            vEditName.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        } else {
            vEditName.setError(null);
        }
        String email = vEditEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            vEditEmail.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        } else {
            vEditEmail.setError(null);
        }
        String password = vEditPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            vEditPassword.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        } else {
            if (password.length() < 6) {
                Toast.makeText(EmailPasswordActivity.this, "Password must be more then 6 charsets", Toast.LENGTH_SHORT).show();
            }
            vEditPassword.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        }
        String phone = vEditPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            vEditPhone.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        } else {
            vEditPhone.setError(null);
        }
        return true;
    }
}
