package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class OverOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_OVER;

	@Override
	public int getOptCode() {
		return optCode;
	}

	public static OverOpt create() {
		OverOpt opt = new OverOpt();
		return opt;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}
	 public static OverOpt fromJson( JSONObject jsonObject ) throws JSONException {
			
		return new OverOpt() ;
	}
	@Override
	public String toCpcl() {
			// TODO Auto-generated method stub
			return "GAP-SENSE\r\nFORM\r\n" + "PRINT\r\n" ;
	}
}
