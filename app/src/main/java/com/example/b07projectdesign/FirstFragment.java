package com.example.b07projectdesign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07projectdesign.databinding.FragmentLoginBinding;
import com.google.firebase.database.DataSnapshot;

public class FirstFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

//        DatabaseHandler dooby = new DatabaseHandler();

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate Login Credentials
                String email = binding.tbEmail.getText().toString();
                String password = binding.tbPassword.getText().toString();
                Log.d("AUTHENTICATION", email + " " + password);
                Listener authCallback = new Listener() {
                    @Override
                    public void onSuccess(String snapshot) {
                        Log.d("AUTHENTICATION", "OH YEAH");
                    }

                    @Override
                    public void onFailure(String data) {
                        Log.d("NOOOOOOO", data);
                    }

                    @Override
                    public void onComplete(String data) {}
                };
                DatabaseHandler.getUser(email, password, authCallback);

                // Proceed Anyway Lmao
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_studentFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}