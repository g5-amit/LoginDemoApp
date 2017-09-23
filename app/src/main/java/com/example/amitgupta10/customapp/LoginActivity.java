package com.example.amitgupta10.customapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //setup for facebook functionalities
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        Button mFbshareContentBtn = (Button) findViewById(R.id.fb_share_content_button);
        mFbshareContentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showfacebookDialog(view);
            }
        });

        Button mFbsharePhotoBtn = (Button) findViewById(R.id.fb_share_photo_button);
        mFbsharePhotoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showfacebookDialog(view);
            }
        });

        Button mFbshareVideoBtn = (Button) findViewById(R.id.fb_share_video_button);
        mFbshareVideoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showfacebookDialog(view);
            }
        });

        Button mFbshareMultimediaBtn = (Button) findViewById(R.id.fb_share_multimedia_button);
        mFbshareMultimediaBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showfacebookDialog(view);
            }
        });

        //Login using Facebook
        LoginButton mFbSignInButton = (LoginButton) findViewById(R.id.fb_sign_in_button);
//        mFbSignInButton.setReadPermissions("email");
//        mFbSignInButton.setReadPermissions(Arrays.asList("user_status"));
        mFbSignInButton.setReadPermissions(Arrays.asList("public_profile"));

        mFbSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken()!=null) {
                    accessToken = loginResult.getAccessToken();
                    String info = "User ID: " + accessToken.getUserId() + "\n" +
                            "Auth Token: " + accessToken.getToken();
                    Log.d("amit",info);

                    Set set = accessToken.getPermissions();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        Log.d("amit","\n"+ it.next());
                    }

                    if(Profile.getCurrentProfile()!=null) {
                        profile = Profile.getCurrentProfile();
                        String id = profile.getId();
                        Log.d("amit", "\n id= " + id);
                        String name = profile.getName();
                        Log.d("amit", "\n name= " + name);
                        String firstName = profile.getFirstName();
                        Log.d("amit", "\n firstname= " + firstName);
                        String middleName = profile.getMiddleName();
                        Log.d("amit", "\n middleName= " + middleName);
                        String lastName = profile.getLastName();
                        Log.d("amit", "\n lastName= " + lastName);
                        Uri linkUri = profile.getLinkUri();
                        Log.d("amit", "\n linkUri= " + linkUri);
                        Uri photoUri = profile.getProfilePictureUri(400, 400);
                        Log.d("amit", "\n photoUri= " + photoUri);
                    }

                    handleSignInResult(profile);

                }else {
                    handleSignInResult(null);
                }
            }

            @Override
            public void onCancel() {
                handleSignInResult(null);
            }

            @Override
            public void onError(FacebookException exception) {
                handleSignInResult(null);
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                accessToken = currentAccessToken;
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                profile = currentProfile;

                Log.d("amit", "new profile info");
                if(profile!=null) {
                    String id = profile.getId();
                    Log.d("amit", "\n id= " + id);
                    String name = profile.getName();
                    Log.d("amit", "\n name= " + name);
                    String firstName = profile.getFirstName();
                    Log.d("amit", "\n firstname= " + firstName);
                    String middleName = profile.getMiddleName();
                    Log.d("amit", "\n middleName= " + middleName);
                    String lastName = profile.getLastName();
                    Log.d("amit", "\n lastName= " + lastName);
                    Uri linkUri = profile.getLinkUri();
                    Log.d("amit", "\n linkUri= " + linkUri);
                    Uri photoUri = profile.getProfilePictureUri(400, 400);
                    Log.d("amit", "\n photoUri= " + photoUri);
                }
            }
        };

        //Login using Facebook

    }

    private void handleSignInResult(Profile profile){
        Intent intent = new Intent(this, MainActivity.class);
        if(profile!=null) {
            String id = profile.getId();
            intent.putExtra(MainActivity.UserID, id);
            String name = profile.getName();
            intent.putExtra(MainActivity.Title, name);
            Uri photoUri = profile.getProfilePictureUri(400, 400);
            intent.putExtra(MainActivity.PicUri, photoUri.toString());
        }
        startActivity(intent);
    }

    private void showfacebookDialog(View view){
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            switch(view.getId()){
                case R.id.fb_share_content_button:
                    ShareLinkContent linkContent = shareLinkOnFacebook();
                    ShareDialog linkContentDialog = new ShareDialog(this);
                    if (linkContentDialog.canShow(ShareLinkContent.class)){
                        linkContentDialog.show(linkContent);
                    }
                    break;
                case R.id.fb_share_photo_button:
                    new DownloadImageTask(view.getId()).execute("http://graphico.in/wp-content/uploads/2014/10/Cute-sweet-cupid-spreading-love-on-valentines-day.jpg");
                    break;
                case R.id.fb_share_video_button:
                    ShareVideoContent videoContent = shareVideoOnFacebook();
                    ShareDialog videoContentDialog = new ShareDialog(this);
                    if (videoContentDialog.canShow(SharePhotoContent.class)){
                        videoContentDialog.show(videoContent, ShareDialog.Mode.AUTOMATIC);
                    }
                    break;
                case R.id.fb_share_multimedia_button:
                    new DownloadImageTask(view.getId()).execute("http://graphico.in/wp-content/uploads/2014/10/Cute-sweet-cupid-spreading-love-on-valentines-day.jpg");
                    break;
                default:

            }
        }
    }

    private ShareLinkContent shareLinkOnFacebook(){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.kamloopsbcnow.com/watercooler/news/news/Now_You_KNow/17/02/12/A_history_of_Valentine_s_Day/"))
                .setContentTitle("Wow! Great")
                .setContentDescription("how i can write it")
                .setImageUrl(Uri.parse("http://graphico.in/wp-content/uploads/2014/10/Cute-sweet-cupid-spreading-love-on-valentines-day.jpg"))
                .build();

        return content;

    }

    private void sharePhotoOnFacebook(Bitmap image){
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent photoContent = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog dialog = new ShareDialog(this);
        if (dialog.canShow(SharePhotoContent.class)){
            dialog.show(photoContent);
        }
    }

    private ShareVideoContent shareVideoOnFacebook(){
        Uri videoFileUri = Uri.fromFile(new File((Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Video/VID1.mp4")));
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoFileUri)
                .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(video)
                .build();
        return content;
    }

    private void shareMultimediaOnFacebook(Bitmap image){
        Uri videoFileUri = Uri.fromFile(new File((Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Video/VID1.mp4")));
        SharePhoto sharePhoto1 = new SharePhoto.Builder().setBitmap(image).build();
        SharePhoto sharePhoto2 = new SharePhoto.Builder().setBitmap(image).build();
        ShareVideo shareVideo1 = new ShareVideo.Builder().setLocalUrl(videoFileUri).build();
        ShareVideo shareVideo2 = new ShareVideo.Builder().setLocalUrl(videoFileUri).build();

        ShareMediaContent shareMediaContent = new ShareMediaContent.Builder()
                .addMedium(sharePhoto1)
                .addMedium(sharePhoto2)
                .addMedium(shareVideo1)
                .addMedium(shareVideo2)
                .build();

        ShareDialog dialog = new ShareDialog(this);
        if (dialog.canShow(ShareMediaContent.class)){
            dialog.show(shareMediaContent, ShareDialog.Mode.AUTOMATIC);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        int viewId;
        public DownloadImageTask(int id) {
            viewId = id;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            mIcon11 = getBitmapFromURL(urldisplay);
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(viewId == R.id.fb_share_photo_button){
                sharePhotoOnFacebook(result);
            }else {
                shareMultimediaOnFacebook(result);
            }
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private void fbLogin(){

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager!=null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}

