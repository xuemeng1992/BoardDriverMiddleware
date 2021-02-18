package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;


public class ReadyOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_READY;

	@Override
	public int getOptCode() {
		return optCode;
	}

	public static ReadyOpt create() {
		ReadyOpt opt = new ReadyOpt();
		return opt;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}
    
	public static ReadyOpt fromJson( JSONObject jsonObject ) throws JSONException {
		
		return new ReadyOpt() ;
	}
	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		int iHeightPot = 1440 ;
		int iWidthPot = 800 ;
		String strRtn = "! 10 203 203 "+ iHeightPot +" 1\r\n" ;
	   	strRtn += "PAGE-WIDTH "+iWidthPot+"\r\n" ;
	   	return strRtn ;
	}
}
