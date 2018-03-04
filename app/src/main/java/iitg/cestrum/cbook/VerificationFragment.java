package iitg.cestrum.cbook;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static iitg.cestrum.cbook.LoginActivity.TAG;


public class VerificationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LoginActivity activity;
    private Button verify;
    public VerificationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        activity = ((LoginActivity)getActivity());
        return inflater.inflate(R.layout.fragment_verification, container, false);

    }

    @Override
    public void onViewCreated(final View view,Bundle savedInstance){
        TextView t = view.findViewById(R.id.field_email_text);
        String email = mUser.getEmail();
        String text = "Verification mail is sent to " + email+ ". Press resend to send again. Hit continue after successful verification";
        t.setText(text);

        view.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.signOut();
            }
        });

        verify = view.findViewById(R.id.verify_email_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               verify.setEnabled(false);

               sendVerify();
            }
        });

        view.findViewById(R.id.sign_in_continue_form).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null && user.isEmailVerified()){
                    proceedNext();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sendVerify();
    }

    public void proceedNext() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    activity.registerUser();
                }
                else{
                    startActivity(new Intent(getContext(),MainActivity.class));
                }
            }
        });
    }

    public void sendVerify() {
        verify.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        verify.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(activity,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(activity,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    }

}
