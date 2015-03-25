package com.wifisecurity.view.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.wifisecurity.R;
import com.wifisecurity.bo.WiFiInfoBO;
import com.wifisecurity.model.InfoWiFi;
import com.wifisecurity.view.WiFiRouterAdapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
	AlertDialog informationDialog;
	ProgressDialog progressBar;
	
	/**
     * Initialize objects.
     */
    private void initialize() {
    	
    	txtEmptyList = (TextView) findViewById(R.id.txtEmptyList);
    	editWiFi = (TextView) findViewById(R.id.txtEmptyList);
    	lstWifiRouter = (ListView) findViewById(R.id.list_wifiRouter);
    	wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

    	wifiInfoBO = new WiFiInfoBO();
    	wifiList = new ArrayList<InfoWiFi>();
    	
    	informationDialog = new AlertDialog.Builder(this).create();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);

    	initialize();
    	createListRouters();
    	
    	lstWifiRouter.setOnItemClickListener(new  OnItemClickListener() {
    	
    		@Override
    		public void onItemClick(AdapterView<?> parent, 
    				View view, int position, long id) {
    			InfoWiFi infoWifi = (InfoWiFi) parent.getAdapter().getItem(position);
    			setWiFiInformationDialog(infoWifi);
    		}
		});
    }
    
    /**
     * Show informations about network in a dialog.
     */
    public void setWiFiInformationDialog(InfoWiFi infoWifi) {
    	String title;
    	
    	informationDialog.setTitle(R.string.network_information);
    
    	if (infoWifi.getSsid().isEmpty())
    		title = "• " + getString(R.string.ssid)+ ": " + getString (R.string.hiddenSSID) + "\n\n";
    	else 
      		title = "• " + getString(R.string.ssid)+ ": " + infoWifi.getSsid() + "\n\n";
    	
    	title += 	"• " + getString(R.string.frequency) + ": " + infoWifi.getFrequency() + " MHz\n\n" +
    				"• " + getString(R.string.capabilities) + ": "  + infoWifi.getCapabilities() + "\n\n" +
    				"• " + getString(R.string.security_level) + ": " + infoWifi.getSecurityofRouter() + "%\n";

    	informationDialog.setMessage(title);
		informationDialog.show();
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
    
    /**
     * Show about screen.
     */
    public void showAbout() {
		Intent intent = new Intent (WiFiInfoActivity.this, AboutActivity.class);
		startActivity(intent);
    }
    
    public void refresh() {
    	
    	
    	progressBar = ProgressDialog.show(this, getString(R.string.refresh), getString(R.string.refreshing), false,false);
    	
    	new Thread(new Runnable() {  
            @Override
            public void run() {
                  try
                  {
                        Thread.sleep(3000);
                  } catch(Exception e){}
	                  progressBar.dismiss();
            }
      }).start();
    	refreshList();
    	txtEmptyList.setText("");
    	
    }
    
    
    public void refreshList() {
    	wifiList.clear();
    	createListRouters();
    	Toast.makeText(WiFiInfoActivity.this, R.string.refresh_list, Toast.LENGTH_SHORT).show();
    }
    
    public void createListRouters() {
    	
    	if (wifiManager.getScanResults().size() > 0) {
    	
	    	for (int i = 0; i < wifiManager.getScanResults().size(); i++) {
	    		wifiInfo = new InfoWiFi();
	    		wifiInfo.setCapabilities(wifiManager.getScanResults().get(i).capabilities);
	    		wifiInfo.setSsid(wifiManager.getScanResults().get(i).SSID);
	    		wifiInfo.setFrequency(wifiManager.getScanResults().get(i).frequency);
//	    		wifiInfo.setTimestamp(wifiManager.getScanResults().get(i).timestamp);
	    		wifiInfo.setSecurityofRouter(wifiInfoBO.getSecurityofRouter(wifiInfo));
	    		wifiList.add(wifiInfo);
	
	    		adapter = new WiFiRouterAdapter(this, wifiList);
	    		lstWifiRouter.setAdapter(adapter);
	    	}
    	} else
    		txtEmptyList.setText(R.string.emptyList);
    }
    
}
