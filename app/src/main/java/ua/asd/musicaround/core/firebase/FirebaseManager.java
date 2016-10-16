package ua.asd.musicaround.core.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import ua.asd.musicaround.BaseAppMusicAround;
import ua.asd.musicaround.activities.EmailPasswordActivity;
import ua.asd.musicaround.models.User;


public class FirebaseManager {
    private static FirebaseManager mFirebaseManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth.AuthStateListener mFireBaseAuthListener;
    private static final String TAG = "FirebaseManager";

    public static synchronized FirebaseManager getInstance() {
        if (mFirebaseManager == null) {
            mFirebaseManager = new FirebaseManager();
        }
        return mFirebaseManager;
    }

    public FirebaseManager() {
        this.mFirebaseAuth = mFirebaseAuth.getInstance();
        this.mFirebaseDB = mFirebaseDB.getInstance();
        this.mFirebaseAnalytics = mFirebaseAnalytics.getInstance(BaseAppMusicAround.getAppContext());
    }

    public interface IsUserLoginResult {
        void resultIsLogin(boolean resultLogin);
    }

    //-----------------Auth-----------------//
//    public void isUserLogin(final IsUserLoginResult isUserLoginResult) {
//        mFireBaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                isUserLoginResult.resultIsLogin(user != null);
//            }
//        };
//    }

    //Create new uswr
    public void createUser(final String username, final String email, String password, final String phone, final String avatar) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "createUserWithEmail:onComplete:" + task.getException().getMessage());
                } else {
                    Log.v(TAG, "createUserWithEmail:onComplete:" + task.getResult().getUser());
                    writeNewUserInDbFirebase(task.getResult().getUser().getUid(), username, email, task.getResult().getUser().getProviderId(), phone, avatar);
                }
            }
        });
    }

    private void writeNewUserInDbFirebase(String userUid, String username, String email, String provider, String phone, String avatar) {
        User user = new User(userUid, username, email, provider, phone, avatar);
        mFirebaseDB.getReference("user").child(user.getUserUid()).setValue(user);
    }

    //Check is user login
    public boolean checkUser() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }

    //Sign in with email and password
    public void signIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "signInWithEmail:failed:" + task.getException());
                }
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
            }
        });
    }

    public void signIn(String email, String password, OnCompleteListener<AuthResult> callback) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback);
    }

    //Get user info
    public FirebaseUser getCurrentUser() {
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser;
        } else {
            return null;
        }
    }

    //Send email to reset password
    public void sendResetPasswordEmail(String email) {
        mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v(TAG, "Email sent.");
                }
            }
        });
    }
}
