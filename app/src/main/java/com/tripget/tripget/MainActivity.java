package com.tripget.tripget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,
			GoogleApiClient.OnConnectionFailedListener,
			ReviewTripFragment.OnFragmentInteractionListener,
			NextTripFragment.OnFragmentInteractionListener,
			MyTripsFragment.OnFragmentInteractionListener,
			ReviewFragment.OnFragmentInteractionListener,
			NotificationFragment.OnFragmentInteractionListener{

	//Visual elements
	private TextView nameTextView;
	private TextView emailTextView;
	private ImageView photoImageView;
	private FloatingActionButton fab;

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
		setContentView(R.layout.activity_main);

		fragment = new NextTripFragment();
		callFragment();

		// ----- NO BORRAR, TENGO MIS DUDAS RESPECTO A LA LLAMADA EN BACKSTACK ---- //

		/*transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.main_content, fragment);
		transaction.addToBackStack(null);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();*/


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

		if(fragment instanceof NextTripFragment) {
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
            fragment = new NextTripFragment();
            callFragment();
        } else if (id == R.id.nav_my_trips) {
            fragment = new MyTripsFragment();
            callFragment();
        } else if (id == R.id.nav_new_story) {
            fragment = new ReviewTripFragment();
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

	// Review Fragment
    public void goToReview(View view) {
	    fragment = new ReviewFragment();
	    callFragment();
    }
}
