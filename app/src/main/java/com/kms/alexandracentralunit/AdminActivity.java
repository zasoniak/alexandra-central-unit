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


/**
 * Created by Mateusz Zasoński on 2014-11-19.
 * AdminActivity - main activity for future administrator work
 */
public class AdminActivity extends Activity {

    BroadcastReceiver receiver;
    private ArrayList<String> partsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ListView listView = (ListView) findViewById(R.id.home_parts_list);
        partsArrayList = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partsArrayList);
        listView.setAdapter(adapter);
        ConfigurationDatabaseHelper.getInstance(getApplicationContext());
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String part = intent.getStringExtra(CoreService.GADGET);
                partsArrayList.add(part);
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
