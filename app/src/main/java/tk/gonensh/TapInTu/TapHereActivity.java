package tk.gonensh.TapInTu;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import tk.gonensh.TapInTu.record.ParsedNdefRecord;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class TapHereActivity extends Activity {

    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private LinearLayout mTagContent;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;

    private AlertDialog mDialog;

    private Firebase eventRef,userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap_here);

        //get Event name from intent
        String event_name = getIntent().getStringExtra("event_name");
        //setup Firebase
        Firebase.setAndroidContext(this);
        String firebaseUrl = getResources().getString(R.string.firebaseUrl);
        userRef = new Firebase(firebaseUrl+"/users");

        eventRef = new Firebase(firebaseUrl+"/events/"+event_name);

        //mTagContent = (LinearLayout) findViewById(R.id.list);
        resolveIntent(getIntent());

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }


        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }
    //////////
    private void tagHandler(String tagId){
        //CHECK IF USER EXISTS
        if(userExists(tagId))
            goToSuccess(tagId);
        else
            goToCreateUser(tagId);

        Firebase checkinRef = eventRef.child("checkins").push();

        String timestamp = new Date().toString();
        Checkin checkin = new Checkin(tagId,timestamp);

        //Push to Firebase
        checkinRef.setValue(checkin);

        System.out.println("NFC read ID: "+tagId);

    }

    boolean userExists(String tagId){
        //ToDo: Check with Firebase
        Log.e("FB Querying ", tagId);
        Query userQueryRef = userRef.orderByChild("userId").equalTo(tagId);


        userQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                System.out.println("FB Added: "+snapshot.getKey());
                //ToDo: send user/timestamp to Firebase
                //goToSuccess(tagId);
            }
            public void onChildChanged(DataSnapshot snapshot, String previousChild) {
                System.out.println("FB Changed: "+snapshot.getKey());
                Log.e("FB Changed ",snapshot.getKey());
            }
            public void onChildMoved(DataSnapshot snapshot, String previousChild) {
                System.out.println("FB Moved: "+snapshot.getKey());
            }
            public void onChildRemoved(DataSnapshot snapshot, String previousChild) {
                System.out.println("FB Removed: "+snapshot.getKey());
            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                System.out.println("FB Removed: "+snapshot.getKey());
            }
            @Override
            public void onCancelled(FirebaseError e) {
                Log.e("Firebase Error: ", e.getMessage());
            }


        });
        return false;
    }

    void goToSuccess(String tagId){
        Intent successIntent = new Intent(TapHereActivity.this,SuccessActivity.class);

        startActivity(successIntent);
    }

    void goToCreateUser(String tagId){
        Intent createUserIntent = new Intent(TapHereActivity.this,CreateActivity.class);
        createUserIntent.putExtra("tagId", tagId);
        startActivity(createUserIntent);
    }

    /////////

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                /*byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);*/
                Parcelable par = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Tag tag = (Tag) par;
                String sTag = getHex(tag.getId());
                tagHandler(sTag);
            }

        }
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

}


