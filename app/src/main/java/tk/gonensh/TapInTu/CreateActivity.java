package tk.gonensh.TapInTu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.core.Repo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CreateActivity extends Activity {

    String tagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        tagId = getIntent().getStringExtra("tagId");
        Button goBackButton = (Button) findViewById(R.id.firebase_button);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.user_name);
                String user_name = name.getText().toString();

                EditText id = (EditText) findViewById(R.id.user_id);
                String user_id = id.getText().toString();
                if (user_id == null || user_name == null) {
                    Toast.makeText(CreateActivity.this, "Please enter...", Toast.LENGTH_LONG).show();
                    return;
                }

                String firebaseUrl = getResources().getString(R.string.firebaseUrl);

                //submit new user to Firebase
                Firebase fb = new Firebase(firebaseUrl);
                Firebase userRef = fb.child("users/"+tagId);

                User user = new User(user_name, user_id);

                userRef.setValue(user);

                Intent successIntent = new Intent(CreateActivity.this, SuccessActivity.class);
                successIntent.putExtra("userName", user_name);
                successIntent.putExtra("tagId", user_id);
                startActivity(successIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
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
}
