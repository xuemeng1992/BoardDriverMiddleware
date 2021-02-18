package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class BarCodeOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_BARCODE;

	public int getOptCode() {
		return optCode;
	}

	int barcodeType;
	int width;
	double ratio;

	int height;
	int x;
	int y;
	String number; // 编码内容

	/**
	 * 
	 * @param barcodeType
	 *            条码类型 见 OptTable.BARCODE_TYPE_128 定义
	 * @param width
	 *            窄条码的宽度点数
	 * @param ratio
	 *            宽条码和窄条码的比率
	 * @param height
	 *            条码的高度点数
	 * @param x
	 *            条码开始的X轴坐标
	 * @param y
	 *            条码开始的Y轴坐标
	 * @param number
	 *            条码数据
	 * @return
	 */
	public static BarCodeOpt create(int barcodeType, int width, double ratio,
	        int height, int x, int y, String number) {
		BarCodeOpt opt = new BarCodeOpt();
		opt.barcodeType = barcodeType;
		opt.width = width;
		opt.ratio = ratio;
		opt.height = height;
		opt.x = x;
		opt.y = y;
		opt.number = number;
		return opt;
	}
	
	public static BarCodeOpt fromJson( JSONObject jsonObject ) throws JSONException {
		
		return create( jsonObject.getInt("barcodeType") ,
				jsonObject.getInt("width") , 
				jsonObject.getDouble("ratio"), 
				jsonObject.getInt("height"),
				jsonObject.getInt("x"),
				jsonObject.getInt("y"),
				jsonObject.getString("number")) ;
	}
	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		String strCode = "128" ;
		if( barcodeType == OptTable.BARCODE_TYPE_128 )
			strCode = "128" ;
		int iRatio = 0x00 ;
		if( ratio == 1.5 ){
			iRatio = 0 ;
		} else if( ratio == 2.0 ){
			iRatio = 1 ;
		}else if( ratio == 2.5 ){
			iRatio = 2 ;
		}else if( ratio == 3.0 ) {
			iRatio = 3 ;
		}else if( ratio == 3.5 ){
			iRatio = 4 ;
		}
		return "BARCODE " + strCode +  " " + width + " " + iRatio + " " + height + " " + x + " " + y + " " + number + "\r\n" ;
	}
	
	public int getBarcodeType() {
		return barcodeType;
	}

	public int getWidth() {
		return width;
	}

	public double getRatio() {
		return ratio;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getNumber() {
		return number;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}

	public void setBarcodeType(int barcodeType) {
		this.barcodeType = barcodeType;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	

}
