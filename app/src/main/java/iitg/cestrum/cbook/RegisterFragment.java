package iitg.cestrum.cbook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstance){
        final Button submit = view.findViewById(R.id.register_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                String firstName = ((EditText) view.findViewById(R.id.register_firstName)).getText().toString();
                String lastName = ((EditText) view.findViewById(R.id.register_lastName)).getText().toString();
                String rollNo = ((EditText) view.findViewById(R.id.register_rollNo)).getText().toString();
                String semester = ((EditText) view.findViewById(R.id.register_semester)).getText().toString();
                String branch = ((EditText) view.findViewById(R.id.register_branch)).getText().toString();

                Map<String,String> obj = new HashMap<>();
                obj.put("firstName",firstName);
                obj.put("lastName",lastName);
                obj.put("rollNo",rollNo);
                obj.put("semester",semester);
                obj.put("branch",branch);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                firestore.collection("users").document(user.getUid()).set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getContext(),MainActivity.class));
                    }
                });

            }
        });
    }

}
