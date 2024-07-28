package edu.monash.fit2081a1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.monash.fit2081a1.R;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);

        String storedUser = sharedPreferences.getString("KEY_USERNAME", "");
        TextView tvUsername = findViewById(R.id.etUsername);

        tvUsername.setText(storedUser);
    }

    public void onLogInButtonClick(View view){
        Intent intent = new Intent(this, DashboardActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);

        TextView tvUsername = findViewById(R.id.etUsername);
        TextView tvPassword = findViewById(R.id.etPassword);

        String usernameString = tvUsername.getText().toString();
        String passwordString = tvPassword.getText().toString();

        String storedUser = sharedPreferences.getString("KEY_USERNAME", "");
        String storedPassword = sharedPreferences.getString("KEY_PASSWORD", "");

        // check if fields are filled in
        if (!usernameString.isEmpty() && !passwordString.isEmpty()){

            // ensure the login user is the same as the registered user
            if (usernameString.equals(storedUser) && passwordString.equals(storedPassword)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Authentication failure: Username or Password incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in your details", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegistrationButtonClick(View view){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }
}