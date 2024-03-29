package com.example.phishnet;

import static com.example.phishnet.SMSMessageListener.RunCallbacks;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.phishnet.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int RECEIVE_SMS_CODE = 100;
    private static final int SEND_SMS_CODE = 101;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SMSMessageListener smsReceiver = new SMSMessageListener();

        setSupportActionBar(binding.toolbar);
        checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_CODE);
        checkPermission(Manifest.permission.SEND_SMS, SEND_SMS_CODE);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        SolaceCredentials.getCredentials(this);

        Receiver.startReceiving();
        new Thread(() -> {
            while(true) {
                // Receive message
                String messageRaw = Receiver.getNextMessage();
                if(messageRaw == null || messageRaw.isEmpty()) Thread.yield();

                else {
                    System.out.println(messageRaw);
                    String[] messageParts = messageRaw.replaceAll("\"|\\{|\\}","").split(",");

                    for (int i = 0; i < messageParts.length; i++) {
                        messageParts[i] = messageParts[i].substring(messageParts[i].indexOf(":")+1).trim();
                        System.out.println(messageParts[i]);
                    }

                    UUID messageId = UUID.fromString(messageParts[0]);
                    UUID conversationId = UUID.fromString(messageParts[1]);
                    int verdict = Integer.parseInt(messageParts[2]);

                    // Update stored message if its not a scam/spam
                    ConversationsData.updateMessageFlag(conversationId,messageId,verdict);
                    ConversationsData.saveConversations(this);
                    updateMessageUI();
                }
            }
        }).start();

    }


    // Method to update UI with a new message
    public void updateMessageUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RunCallbacks();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == RECEIVE_SMS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Receive SMS Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Receive SMS  Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        if (requestCode == SEND_SMS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Receive SMS Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Receive SMS  Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
    }
}