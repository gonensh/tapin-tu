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


public class CreateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        Button button2 = (Button) findViewById(R.id.firebase_button);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.user_name);
                String user_name = name.getText().toString();

                EditText id = (EditText) findViewById(R.id.user_id);
                String user_id = id.getText().toString();
                if(user_id==null || user_name == null){
                    Toast.makeText(CreateActivity.this, "Please enter...", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent2 = new Intent(CreateActivity.this, SuccessActivity.class);

                intent2.putExtra("user_name",user_name);
                intent2.putExtra("user_id",user_id);

                startActivity(intent2);
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
