package ua.asd.musicaround.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import ua.asd.musicaround.R;
import ua.asd.musicaround.core.firebase.FirebaseManager;

public class SplashActivity extends BaseActivity {

    private CircularFillableLoaders vLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindViews();
        LoadViewTask loadViewTask = new LoadViewTask();
        loadViewTask.execute();
    }

    private void bindViews() {
        vLogo = (CircularFillableLoaders) findViewById(R.id.logo);
    }

    @Override
    public void onClick(View view) {
        //no need
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            vLogo.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Get the current thread's token
                synchronized (this) {
                    //Initialize an integer (that will act as a counter) to zero
                    int counter = 0;
                    //While the counter is smaller than four
                    while (counter <= 4) {
                        //Wait 850 milliseconds
                        this.wait(850);
                        //Increment the counter
                        counter++;
                        //Set the current progress.
                        //This value is going to be passed to the onProgressUpdate() method.
                        publishProgress(counter * 25);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            vLogo.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            if (FirebaseManager.getInstance().checkUser()) {
                Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, EmailPasswordActivity.class);
                startActivity(intent);
                finish();
            }
//            FirebaseManager.getInstance().isUserLogin(new FirebaseManager.IsUserLoginResult() {
//                @Override
//                public void resultIsLogin(boolean result) {
//                    if (result) {
//                        Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(SplashActivity.this, EmailPasswordActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            });
        }
    }
}
