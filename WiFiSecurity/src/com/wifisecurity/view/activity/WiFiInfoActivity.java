package com.wifisecurity.view.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.wifisecurity.R;
import com.wifisecurity.model.InfoWiFi;
import com.wifisecurity.model.bo.WiFiInfoBO;
import com.wifisecurity.presenter.WiFiRouterAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiInfoActivity extends ActionBarActivity {

	TextView txtEmptyList;
	TextView editWiFi;
	ListView lstWifiRouter;
	InfoWiFi wifiInfo;
	List <InfoWiFi> wifiList;
	WifiManager wifiManager;
	WiFiInfoBO wifiInfoBO;
	WiFiRouterAdapter adapter;
	AlertDialog dialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);

    	initialize();
    	createListRouters();
//    	refresh();
    	
    	lstWifiRouter.setOnItemClickListener(new  OnItemClickListener() {
    	
    		@Override
    		public void onItemClick(AdapterView<?> parent, 
    				View view, int position, long id) {
    			
    			InfoWiFi infoWifi = (InfoWiFi) parent.getAdapter().getItem(position);
    			setWiFiInformationDialog(infoWifi);
    		}
		});
    }

    private void initialize() {
    	
    	txtEmptyList = (TextView) findViewById(R.id.txtEmptyList);
    	editWiFi = (TextView) findViewById(R.id.txtEmptyList);
    	lstWifiRouter = (ListView) findViewById(R.id.list_wifiRouter);
    	wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

    	wifiInfoBO = new WiFiInfoBO();
    	wifiList = new ArrayList<InfoWiFi>();
    	
    	dialog = new AlertDialog.Builder(this).create();
    }
  
    public void setWiFiInformationDialog(InfoWiFi infoWifi) {
    	if (infoWifi.getSsid().isEmpty())
    		dialog.setTitle(R.string.hiddenSSID);
    	else 
    		dialog.setTitle(infoWifi.getSsid());
    	
		dialog.setMessage("Frequência: " + infoWifi.getFrequency() + "\n\n" +
				"- Capacidades: " + infoWifi.getCapabilities() + "\n\n" +
				"- Timestamp: " + infoWifi.getTimestamp() + "\n\n" +
				"- Nivel de segurança: " + infoWifi.getSecurityofRouter() + "%");
		
		dialog.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
            	refresh();
                return true;
            case R.id.action_about:
            	showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    public void showAbout() {
		Intent intent = new Intent (WiFiInfoActivity.this, AboutActivity.class);
		startActivity(intent);
    }
    
    
    public void refresh() {
    	adapter.notifyDataSetChanged();
    	
    	Toast.makeText(this, R.string.refresh_list, Toast.LENGTH_SHORT).show();
    }
    
    
    public void createListRouters() {
    	
    	if (wifiManager.getScanResults().size() > 0) {
    	
	    	for (int i = 0; i < wifiManager.getScanResults().size(); i++) {
	    		wifiInfo = new InfoWiFi();
	    		wifiInfo.setCapabilities(wifiManager.getScanResults().get(i).capabilities);
	    		wifiInfo.setSsid(wifiManager.getScanResults().get(i).SSID);
	    		wifiInfo.setFrequency(wifiManager.getScanResults().get(i).frequency);
	    		wifiInfo.setTimestamp(wifiManager.getScanResults().get(i).timestamp);
	    		wifiInfo.setSecurityofRouter(wifiInfoBO.getSecurityofRouter(wifiInfo));
	    		wifiList.add(wifiInfo);
	
	    		adapter = new WiFiRouterAdapter(this, wifiList);
	    		lstWifiRouter.setAdapter(adapter);
	    	}
    	} else
    		txtEmptyList.setText(R.string.emptyList);
    }
}
