package ua.asd.musicaround.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import ua.asd.musicaround.R;
import ua.asd.musicaround.core.firebase.FirebaseManager;


public class EmailPasswordActivity extends BaseActivity {
    private EditText vEditName;
    private EditText vEditEmail;
    private EditText vEditPassword;
    private EditText vEditPhone;
    private FloatingActionButton vGetAvatar;
    private Button vGetStartedButton;
    private FirebaseDatabase mFirebaseDB;
    private String pathToAvatar;


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
        vGetAvatar = (FloatingActionButton) findViewById(R.id.avatar);
        vGetStartedButton.setOnClickListener(this);
    }

    private void createAccount(String name, String email, String password, String phone) {
        FirebaseManager.getInstance().createUser(name, email, password, phone, getAvatarBase64(pathToAvatar));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                getPhoto();
                break;
            case R.id.get_started_button:
                onGetStartedButtonPressed();
                break;
        }
    }

    private void onGetStartedButtonPressed() {
        String name = vEditName.getText().toString();
        String email = vEditEmail.getText().toString();
        String password = vEditPassword.getText().toString();
        String phone = vEditPhone.getText().toString();
        if (validateForm()) {
            createAccount(name, email, password, phone);
            new Intent(EmailPasswordActivity.this, MapsActivity.class);
            finish();
        }
    }

    // TODO: 28.09.2016 SB refact form validation
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
            vEditEmail.setError(null);
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

    private String getAvatarBase64(String path) {
        if (path != null) {
            Bitmap avatarBitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            avatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        } else {
            return null;
        }
    }

    private void getPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setAction("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri chosenImageUri = null;
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    chosenImageUri = data.getData();
                }
        }
        try {

            if (chosenImageUri != null) {
                final Cursor cursor = getContentResolver().query(chosenImageUri, null, null, null, null);
                cursor.moveToFirst();
                pathToAvatar = cursor.getString(0);
                cursor.close();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
