package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class NopOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_NOP;

	public int getOptCode() {
		return optCode;
	}
    public static NopOpt fromJson( JSONObject jsonObject ) throws JSONException {
		
		return new NopOpt() ;
	}
	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		return "";
	}
}
