package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    public static final String USERNAME = "loginusername";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recuperación de elementos
        TextView welcomeTextView = findViewById(R.id.myWelcome);
        String welcomeMessage = welcomeTextView.getText().toString();
        Bundle extras = getIntent().getExtras();

        String username = extras.getString(USERNAME);
        welcomeMessage = welcomeMessage + " , " + username;
        welcomeTextView.setText(welcomeMessage);
        //TextView
        TextView myTextView = findViewById(R.id.text);
        //EditText
        EditText myEdit1 =findViewById(R.id.myEdit1);
        EditText myEdit2 =findViewById(R.id.myEdit2);
        EditText myEdit3 =findViewById(R.id.myEdit3);
        EditText myEdit4 = findViewById(R.id.myEdit4);
        EditText myEdit5 = findViewById(R.id.myEdit5);

        //Button
        Button mybutton = findViewById(R.id.myButton);
        Button myresetbutton = findViewById(R.id.myResetButton);


       //Escuchar al boton
       mybutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Code is executed when button is clicked
               String name = String.valueOf(myEdit1.getText());
               String lastName = String.valueOf(myEdit2.getText());
               String ageString = String.valueOf(myEdit3.getText());
               String degree = String.valueOf(myEdit4.getText());
               String semester = String.valueOf(myEdit5.getText());

               myTextView.setText(buildStudentData(name, lastName, ageString,degree, semester));
           }
       });

       myresetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                myEdit1.setText("");
                myEdit2.setText("");
                myEdit3.setText("");
                myEdit4.setText("");
                myEdit5.setText("");
                myTextView.setText("Student Data UPS");
            }
        });
    }
    public  String buildStudentData(String name, String lastName, String ageString, String degree, String semester) {
        return "Student Data UPS \n\n" + "Name: " + name + "\n" +
                "Lastname:" + lastName + "\n" + "Age: " + ageString + " years\n" +
                "Degree: " + degree + "\n" + "BirthYear: " +calculateBirthYear(ageString) + "\n" + "Enrollment Year: " +calculateSemester(semester);
    }

    public String calculateBirthYear(String ageString){
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int age = Integer.parseInt(ageString);
        int birthYear = year - age;
        return String.valueOf(birthYear);
    }

    public String calculateSemester(String semester){
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int numSemester = Integer.parseInt(semester);
        int realSemester = numSemester / 2;
        int yearSemester = year - realSemester;
        return String.valueOf(yearSemester);
    }
}