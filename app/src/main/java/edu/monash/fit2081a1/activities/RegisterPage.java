package edu.monash.fit2081a1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.monash.fit2081a1.R;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnButtonRegisterClick(View view) {
        Intent intent = new Intent(this, LoginPage.class);

        TextView tvUsername = findViewById(R.id.editTextUsername);
        TextView tvPassword = findViewById(R.id.editTextPassword);
        TextView tvPasswordConfirm = findViewById(R.id.editTextConfirmPassword);

        String usernameString = tvUsername.getText().toString();
        String passwordString = tvPassword.getText().toString();
        String passwordConfirmString = tvPasswordConfirm.getText().toString();

        // ensure all fields are filled in
        if (!usernameString.isEmpty() && !passwordString.isEmpty() && !passwordConfirmString.isEmpty()) {
            // check for pw and confirm pw is they're the same value
            if (passwordString.equals(passwordConfirmString)){
                saveDataToSharedPreference(usernameString, passwordString);
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in your details", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnButtonToLogInClick(View view) {
        // to Login page
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    private void saveDataToSharedPreference(String usernameValue, String passwordValue){
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString("KEY_USERNAME", usernameValue);
        editor.putString("KEY_PASSWORD", passwordValue);

        editor.apply();

    }
}