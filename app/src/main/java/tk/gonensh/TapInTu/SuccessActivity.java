package tk.gonensh.TapInTu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SuccessActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_activity);

      //  create_user user = new create_user();
           TextView textv = (TextView) findViewById(R.id.success);
            textv.setText("Success, "+getIntent().getStringExtra("userName")+"!\n You are checked in.");

        Button success = (Button) findViewById(R.id.success_button);

        success.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent to_scan = new Intent(SuccessActivity.this, TapHereActivity.class);
                                           to_scan.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                           startActivity(to_scan);
                                           SuccessActivity.this.finish();
                                       }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_success_activity, menu);
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
