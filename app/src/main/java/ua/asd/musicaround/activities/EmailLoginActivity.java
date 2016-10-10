package ua.asd.musicaround.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ua.asd.musicaround.R;
import ua.asd.musicaround.core.firebase.FirebaseManager;


public class EmailLoginActivity extends BaseActivity {
    private EditText vEditEmail;
    private EditText vEditPassword;
    private TextView vTextCreateAccount;
    private TextView vTextForgotPassword;
    private Button vGetSartButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        vEditEmail = (EditText) findViewById(R.id.edit_email_login);
        vEditPassword = (EditText) findViewById(R.id.edit_pass_login);
        vTextCreateAccount = (TextView) findViewById(R.id.create_account_text);
        vTextForgotPassword = (TextView) findViewById(R.id.forgot_password_text);
        vGetSartButton = (Button) findViewById(R.id.get_started_button_login);
        vTextCreateAccount.setOnClickListener(this);
        vTextForgotPassword.setOnClickListener(this);
        vGetSartButton.setOnClickListener(this);

    }

    private boolean validateForm() {
        Toast toast = Toast.makeText(EmailLoginActivity.this, "Required", Toast.LENGTH_SHORT);
        boolean emailIs = false;
        boolean passwordIs = false;
        if (TextUtils.isEmpty(vEditEmail.getText().toString())) {
            vEditEmail.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        } else {
            vEditEmail.setError(null);
            emailIs = true;
        }
        if (TextUtils.isEmpty(vEditPassword.getText().toString())) {
            vEditPassword.setBackgroundResource(R.drawable.edittext_error);
            toast.show();
        } else {
            vEditPassword.setError(null);
            passwordIs = true;
        }
        if (emailIs && passwordIs) {
            return true;
        } else {
            return false;
        }
    }

    private void logInUser(String email, String password) {
        // TODO: 28.09.2016 SB refact checking is auth success

        FirebaseManager.getInstance().signIn(email, password);
        if (FirebaseManager.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(EmailLoginActivity.this, MapsActivity.class));
            finish();
        } else {
            Toast.makeText(EmailLoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_account_text:
                startActivity(new Intent(EmailLoginActivity.this, EmailPasswordActivity.class));
                finish();
                break;
            case R.id.forgot_password_text:
                startActivity(new Intent(EmailLoginActivity.this, ResetPasswordActivity.class));
                finish();
                break;
            case R.id.get_started_button_login:
                if (validateForm()) {
                    logInUser(vEditEmail.getText().toString(), vEditPassword.getText().toString());
                }
                break;
        }
    }
}
