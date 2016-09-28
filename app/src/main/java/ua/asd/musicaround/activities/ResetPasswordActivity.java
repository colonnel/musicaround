package ua.asd.musicaround.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ua.asd.musicaround.R;
import ua.asd.musicaround.core.firebase.FirebaseManager;

public class ResetPasswordActivity extends BaseActivity {
    private EditText vEditEmail;
    private Button vResetPasswordButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        vEditEmail = (EditText) findViewById(R.id.edit_email_reset);
        vResetPasswordButton = (Button) findViewById(R.id.reset_password_button);
        vResetPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String email = vEditEmail.getText().toString();
        if (!TextUtils.isEmpty(email)) {
            FirebaseManager.getInstance().sendResetPasswordEmail(email);
        }
    }
}
