package com.tripget.tripget.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.tripget.tripget.Conexion.Constantes;
import com.tripget.tripget.Conexion.VolleySingleton;
import com.tripget.tripget.Fragments.BestBudgetFragment;
import com.tripget.tripget.Fragments.DetailTripFragment;
import com.tripget.tripget.Fragments.MyTripsFragment;
import com.tripget.tripget.Fragments.NotificationFragment;
import com.tripget.tripget.R;
import com.tripget.tripget.Fragments.TripFormFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,
			GoogleApiClient.OnConnectionFailedListener,
        TripFormFragment.OnFragmentInteractionListener,
        BestBudgetFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener,
        DetailTripFragment.OnFragmentInteractionListener,
        MyTripsFragment.OnFragmentInteractionListener{

    private static final String TAG = "users" ;
    //Visual elements
	private TextView nameTextView;
	private TextView emailTextView;
	private ImageView photoImageView;
	private FloatingActionButton fab;
    private String idTokenFinal;

	//Google Api Client

	private GoogleApiClient googleApiClient;

	//FirebaseAuth and listener

	private FirebaseAuth firebaseAuth;
	private FirebaseAuth.AuthStateListener firebaseAuthListener;

	//Fragments

	FragmentTransaction transaction;
	private Fragment fragment = null;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		setContentView(R.layout.activity_main);

		fragment = new BestBudgetFragment();
		callFragment();


		//SignIn Google

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		googleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		//Auth Firebase

		firebaseAuth = FirebaseAuth.getInstance();
		firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    HashMap <String,String> userHash = new LinkedHashMap<>();
                    userHash.put("account_id", user.getUid());
                    userHash.put("username", user.getDisplayName());
                    userHash.put("name", null);
                    userHash.put("last_name", null);
                    userHash.put("email", user.getEmail());
                    userHash.put("photo", String.valueOf(user.getPhotoUrl()));
                    loadAdapterUsers(userHash);
                    setUserData(user);
				} else {
					goLogInScreen();
				}
			}
		};

		//Menu Drawer

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setCheckedItem(R.id.nav_search);

	}

	private void callFragment() {

		if(fragment instanceof BestBudgetFragment) {
			getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
		} else {
			getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
		}

	}

    //Google SignIn Methods

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void logOut(){

        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    goLogInScreen();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Auth Firebase Method


    private void setUserData(FirebaseUser user) {

        //Screen
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        photoImageView = (ImageView)header.findViewById(R.id.userImg);
        nameTextView = (TextView)header.findViewById(R.id.userNameTxt);
        emailTextView = (TextView)header.findViewById(R.id.userMailTxt);

        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());

        Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(photoImageView);

    }

    //Menu Drawer Method

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            // Handle the camera action
            fragment = new BestBudgetFragment();
            callFragment();
        } else if (id == R.id.nav_my_trips) {
            fragment = new MyTripsFragment();
            callFragment();

        } else if (id == R.id.nav_notification) {
            fragment = new NotificationFragment();
            callFragment();

        } else if (id == R.id.nav_exit) {
            logOut();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //

    public void loadAdapterUsers(HashMap<String, String> userHash){

        //String user_save = userHash.get("username");

        //Log.d(TAG, user_save);

        JSONObject jobject = new JSONObject(userHash);

        Log.d(TAG, jobject.toString());
        VolleySingleton.getInstance(MainActivity.this).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.INSERT_USER,
                                jobject,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //getResponseUsers(response);
                                        Toast.makeText(MainActivity.this,"users add", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener(){

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "ERROR VOLLEY: " + error.getMessage());
                                    }
                                }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
    }

    private void getResponseUsers(JSONObject response) {

        Toast.makeText(MainActivity.this,"users add", Toast.LENGTH_SHORT).show();
    }
}
