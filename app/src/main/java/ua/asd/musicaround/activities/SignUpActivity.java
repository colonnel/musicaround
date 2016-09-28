package ua.asd.musicaround.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ua.asd.musicaround.R;


public class SignUpActivity extends BaseActivity {
    private Button vSignUpFbButton;
    private Button vSignUpVkButton;
    private Button vSignUpEmailButton;
    private TextView vLoginText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        vSignUpFbButton = (Button) findViewById(R.id.fb_sign_button);
        vSignUpVkButton = (Button) findViewById(R.id.vk_sign_button);
        vSignUpEmailButton = (Button) findViewById(R.id.email_sign_button);
        vLoginText = (TextView) findViewById(R.id.log_in_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_sign_button:
                Toast.makeText(SignUpActivity.this, "Sorry! Need TODO.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vk_sign_button:
                Toast.makeText(SignUpActivity.this, "Sorry! Need TODO.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.email_sign_button:
                Intent intent = new Intent(SignUpActivity.this, EmailPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.log_in_text:
                Intent intent1 = new Intent(SignUpActivity.this, EmailLoginActivity.class);
                startActivity(intent1);
                finish();
                System.out.println();
                break;
        }
    }
}
