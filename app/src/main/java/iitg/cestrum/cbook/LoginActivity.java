package iitg.cestrum.cbook;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    public static final String TAG = "EmailPassword";

    public static final int STEP_ONE = 100;
    public static final int STEP_TWO = 200;
    public static final int STEP_THREE = 300;
    private FirebaseFirestore firestore;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    public ProgressDialog mProgressDialog;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private int step;
    boolean flag = false;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        Bundle b = getIntent().getExtras();
        if(b != null)
            step = b.getInt("STEP");
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        switch(step){
            case STEP_ONE:
                    getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new LoginFragment()).commit();
                break;
            case STEP_TWO:
                    getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new VerificationFragment()).commit();
                break;
            case STEP_THREE:
                    getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new RegisterFragment()).commit();
                break;

            default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new LoginFragment()).commit();
        }

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
    // [END on_start_check_user]


    public void signOut() {
        mAuth.signOut();
        getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new LoginFragment()).commit();
    }

    public void registerUser() {
        getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new RegisterFragment()).commit();
    }

    public void verifyEmail() {
        getSupportFragmentManager().beginTransaction().replace(R.id.auth_fragment_container,new VerificationFragment()).commit();
    }

    public boolean registrationComplete() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        firestore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(! documentSnapshot.exists()){
                    flag = false;
                    registerUser();
                }
                else{
                    flag = true;
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });

        return flag;
    }

}



