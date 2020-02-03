package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class addcontactActivity extends AppCompatActivity {
    static final int PICK_CONTACT_REQUEST = 1;
    private static final String TAG = "MyActivity";
String file="a.txt";
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);
        File f = new File(file);

        Button b= findViewById(R.id.button3);
        Button b1= findViewById(R.id.button4);





       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               try {
                   FileOutputStream stream = openFileOutput(file, getApplicationContext().MODE_PRIVATE);
                   stream.write("".getBytes());
                   stream.close();
                   Toast.makeText(getApplicationContext(), "All picked contacts has been removed", Toast.LENGTH_SHORT).show();
               } catch (FileNotFoundException e) {
                   Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
               } catch (IOException e) {
                   Toast.makeText(getApplicationContext(), "File Not error", Toast.LENGTH_SHORT).show();
               }


               t.setText("");


           }
       });



        t= (TextView) findViewById(R.id.textView);
        if(f.exists())
        {
        }
        else
        {

            try {
                FileWriter writer = new FileWriter(file);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }


        FileInputStream fis = null;

        try {
            fis = openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            t.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK);

                // Show user only the contacts that include phone numbers.
                pickContactIntent.setDataAndType(Uri.parse("content://contacts"),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = resultIntent.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                t.append("\n");
                t.append(number);


                try {  FileOutputStream fos = openFileOutput(file, getApplicationContext().MODE_APPEND);
                    fos.write("\n".getBytes());
                    fos.write(number.getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }



            }
        }
    }
}
