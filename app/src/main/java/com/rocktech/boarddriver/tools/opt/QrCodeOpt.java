package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class QrCodeOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_QRCODE;

	@Override
	public int getOptCode() {
		return optCode;
	}

	/**
	 * 
	 * @param x
	 *            条码开始的X轴坐标
	 * @param y
	 *            条码开始的Y轴坐标
	 * @param qrVersion
	 *            QR代码型号版本 版本1的QR码1边由21个码元构成。版本每高一级，构成QR码一边的码元就增加4个
	 * @param unitHeight
	 *            QR码元的高度点数 也是宽度点数
	 * @param level
	 *            容错率等级 见 OptTable.QRCODE_LEVEL_L 等字体定义
	 * @param content
	 *            二维码内容
	 * @return
	 */
	public static QrCodeOpt create(int x, int y, int qrVersion, int unitHeight,
	        int level, String content) {
		QrCodeOpt opt = new QrCodeOpt();
		opt.x = x;
		opt.y = y;
		opt.qrVersion = qrVersion;
		opt.unitHeight = unitHeight;
		opt.level = level;
		opt.content = content;
		return opt;
	}
	public static QrCodeOpt fromJson( JSONObject jsonObject ) throws JSONException {
		
		return create( jsonObject.getInt("x") ,
				jsonObject.getInt("y") , 
				jsonObject.getInt("qrVersion"), 
				jsonObject.getInt("unitHeight"),
				jsonObject.getInt("level"),
				jsonObject.getString("content")) ;
	}
	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		String strLevel = "L" ;
		if( level == OptTable.QRCODE_LEVEL_L )
		{
			strLevel = "L" ;
		}else if(level == OptTable.QRCODE_LEVEL_M)
		{
			strLevel = "M" ;
		}else if(level == OptTable.QRCODE_LEVEL_Q)
		{
			strLevel = "Q" ;
		}else if(level == OptTable.QRCODE_LEVEL_H)
		{
			strLevel = "H" ;
		}
		//qrVersion
		String strRtn = "BARCODE QR " + x + " " + y + " M 2 U " + unitHeight + " \r\n";
    	strRtn += strLevel+"A," + content + "\r\n";
    	strRtn += "ENDQR\r\n" ;
    	return strRtn ;
		
	}
	
	int x;
	int y;
	int qrVersion;
	int unitHeight;
	int level;
	String content;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getQrVersion() {
		return qrVersion;
	}

	public int getUnitHeight() {
		return unitHeight;
	}

	public int getLevel() {
		return level;
	}

	public String getContent() {
		return content;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setQrVersion(int qrVersion) {
		this.qrVersion = qrVersion;
	}

	public void setUnitHeight(int unitHeight) {
		this.unitHeight = unitHeight;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
