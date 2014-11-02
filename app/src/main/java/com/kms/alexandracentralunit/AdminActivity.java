package com.kms.alexandracentralunit;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kms.alexandracentralunit.data.database.sqlite.ConfigurationDatabaseHelper;

import java.util.ArrayList;


public class AdminActivity extends Activity {

    BroadcastReceiver receiver;
    private ArrayAdapter adapter;
    private ArrayList<String> gadgetArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ListView listView = (ListView) findViewById(R.id.gadget_list);
        gadgetArrayList = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gadgetArrayList);
        listView.setAdapter(adapter);
        ConfigurationDatabaseHelper.getInstance(getApplicationContext());
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String gadget = intent.getStringExtra(CoreService.GADGET);
                gadgetArrayList.add(gadget);
            }
        };
    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(CoreService.UPDATE_MESSAGE));
        Intent intent = new Intent(this, CoreService.class);
        startService(intent);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
