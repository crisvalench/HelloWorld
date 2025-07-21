package com.example.helloworld;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String BASE_URL = "http://10.0.2.2:8080/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText myUser = findViewById(R.id.myUser);
        EditText myPassword = findViewById(R.id.myPassword);
        Button loginButton = findViewById(R.id.myLoginBtn);

        //Get user credentials Map

        Map<String, String> storeCredentials = MyApplication.userCredentials;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameSubmitted = String.valueOf(myUser.getText());
                String passwordSubmitted = String.valueOf(myPassword.getText());

                if ((usernameSubmitted != null && !usernameSubmitted.isBlank()) &&
                        passwordSubmitted != null && !passwordSubmitted.isBlank()) {

//                    if (storeCredentials.containsKey(usernameSubmitted)) {
//                        String storePassword = storeCredentials.get(usernameSubmitted);
//                        if (storePassword.equals(passwordSubmitted)) {
//
//                            Toast.makeText(LoginActivity.this, "Login Successful 🙋🏻‍♀️", LENGTH_LONG).show();
//                            //First Login, Then Main
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra(MainActivity.USERNAME, usernameSubmitted);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Invalid Credentials 👎", LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(LoginActivity.this, "User does'nt exists 👎", LENGTH_LONG).show();
//                    }
                    sendLoginRequest(usernameSubmitted, passwordSubmitted);
                } else {
                    validateErrorMessage(myUser, usernameSubmitted, "User");
                    validateErrorMessage(myPassword, passwordSubmitted, "Password");
                    Toast.makeText(LoginActivity.this, "USER AND PASSWORD CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                }

            }
            private void validateErrorMessage(EditText editText, String text, String fieldName){
                if(text == null || text.isBlank()) {
                    editText.setError(fieldName + " cannot be empty");
                }
            }
        });
    }
    private void sendLoginRequest(String username, String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                OutputStream out = null;
                String responseString = null;
                int responseCode = -1;
                try{
                    //build URL
                    URL url = new URL(BASE_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    //build request {"username":username, "password":password}
                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("user", username);
                    jsonRequest.put("password", password);
                    String jsonInputString = jsonRequest.toString();

                    //Get OutputStream from URL Connection + Send Request (Input)
                    out = urlConnection.getOutputStream();
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    out.write(input, 0, input.length);
                    out.flush();
                    out.close();

                    responseCode = urlConnection.getResponseCode();

                    handleResponseCode(responseCode, username);

                }catch(Exception exception){
                    System.out.println("Exception" + exception.getMessage());
                } finally {
                    if(reader != null){
                        try{reader.close();} catch (IOException e) {e.printStackTrace();}
                    }
                    if(out != null){
                        try{out.close();} catch (IOException e) {e.printStackTrace();}
                    }
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void handleResponseCode(int responseCode, String username) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(responseCode == 200){
                    Toast.makeText(LoginActivity.this, "Login Successful 🦾", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.USERNAME, username);
                    startActivity(intent);
                    finish();
                }
                if(responseCode == 400) {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials 👎", LENGTH_LONG).show();
                }
            }
        });
    }
}