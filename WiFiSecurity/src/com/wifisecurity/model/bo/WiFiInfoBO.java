package com.wifisecurity.model.bo;

import java.util.ArrayList;
import java.util.List;

import com.wifisecurity.model.InfoWiFi;
import com.wifisecurity.model.WiFiConstant;

public class WiFiInfoBO {

	static final int PARAM_SIZE = 6;
	static final int FREQUENCY_2400_MIN = 2400;
    static final int FREQUENCY_2400_MAX = 2499;
	static final int FREQUENCY_5200 = 5200;
	static long HS_24 = 86400000000L;
	static long HS_72 = 259200000000L;
	private List<String> default_router;
	private float insurancePerc;
	
	InfoWiFi wifiInfo;
	
	public List<String> getDefaultRouters() {
		default_router = new ArrayList<String>();
		default_router.add("LINKSYS");
		default_router.add("OI WIFI FON");
        default_router.add("OI WIFI");
        default_router.add("OI_VELOX_WIFI_E2FA");
		default_router.add("D-LINK");
		default_router.add("DLINK");
		default_router.add("NETGEAR");
		default_router.add("TP-LINK");
		default_router.add("CISCO-LINK");
		default_router.add("INTELBRAS");
        default_router.add("ASUS");
        default_router.add("SMC");
        default_router.add("DELL");
        default_router.add("TRENDNET");
        default_router.add("3COM");

		return default_router;
	}
	
	public int getssid(InfoWiFi wifiInfo) {
		if (getDefaultRouters().contains(wifiInfo.getSsid().toUpperCase())){
			return WiFiConstant.LOW;
		} else
			if (wifiInfo.getSsid().isEmpty() || wifiInfo.getSsid() == null){
				return WiFiConstant.HIGH;
			} else 
				return WiFiConstant.MEDIUM;
	}
	
	public int getFrequency(InfoWiFi wifiInfo) {
		if (wifiInfo.getFrequency() < FREQUENCY_2400_MAX ){
			return WiFiConstant.MEDIUM;
		} else
			return WiFiConstant.HIGH;
	}
	
	public int getEncryption(InfoWiFi wifiInfo) {
		if (wifiInfo.getCapabilities().contains("WPA"))
			return WiFiConstant.MEDIUM;
		else
			if (wifiInfo.getCapabilities().contains("WPA2"))
				return WiFiConstant.HIGH;
			else
				return WiFiConstant.LOW;
	}
	
	public int getWPAAlgorithm(InfoWiFi wifiInfo) {
		if (wifiInfo.getCapabilities().contains("TKIP"))
			return WiFiConstant.LOW;
		else 
			if (wifiInfo.getCapabilities().contains("AES") || wifiInfo.getCapabilities().contains("CCMP") || wifiInfo.getCapabilities().contains("AES-CCMP")) 
				return WiFiConstant.HIGH;
			else
				return WiFiConstant.LOW;
	}
	
	public int getWPSAvailabe(InfoWiFi wifiInfo) {
		if (wifiInfo.getCapabilities().contains("WPS"))
			return WiFiConstant.LOW;
		else return WiFiConstant.HIGH;
	}
	
	public int getTimestamp(InfoWiFi wifiInfo) {
		if (wifiInfo.getTimestamp() < HS_24)
			return WiFiConstant.HIGH;
		else
			if (wifiInfo.getTimestamp() > HS_24 && wifiInfo.getTimestamp() < HS_72)
				return WiFiConstant.MEDIUM;
			else
				return WiFiConstant.LOW;
	}
	
	public float getSecurityofRouter(InfoWiFi wifiInfo) {
		insurancePerc = (getssid(wifiInfo) + getWPAAlgorithm (wifiInfo) + getFrequency(wifiInfo) + getWPSAvailabe(wifiInfo) +getEncryption(wifiInfo) + getTimestamp(wifiInfo))/PARAM_SIZE;
		return  insurancePerc;
	}
	
	public boolean isInsurance() {
		if (insurancePerc > 70)
			return true;
		else
			return false;
		
	} 
}
