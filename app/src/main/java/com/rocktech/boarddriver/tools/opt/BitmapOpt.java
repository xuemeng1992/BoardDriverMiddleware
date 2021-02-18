package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class BitmapOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_BITMAP;

	public int getOptCode() {
		return optCode;
	}

	int width;
	int height;
	int x;
	int y;
	String octetStr;

	/**
	 * 
	 * @param width
	 *            点阵图像宽度点数 8的整数倍
	 * @param height
	 *            点阵图像高度点数
	 * @param x
	 *            点阵图像开始的X轴坐标
	 * @param y
	 *            点阵图像开始的Y轴坐标
	 * @param byteString
	 *            图像十六进制数据
	 * @return
	 */
	public static BitmapOpt create(int width, int height, int x, int y,
	        String octetStr) {
		BitmapOpt opt = new BitmapOpt();
		opt.width = width;
		opt.height = height;
		opt.x = x;
		opt.y = y;
		opt.octetStr = octetStr;
		return opt;
	}

	public static BitmapOpt fromJson( JSONObject jsonObject ) throws JSONException {
		
		return create( 
				jsonObject.getInt("width") , 
				jsonObject.getInt("height"),
				jsonObject.getInt("x"),
				jsonObject.getInt("y"),
				jsonObject.getString("octetStr")) ;
	}
	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		width = ( width >> 3 ) ;
		return "EG " + width + " " + height + " " + x + " " + y + " " +  octetStr + "\r\n" ; 
	}
	
	public int getWidth() {
		return width;
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

	public String getOctetStr() {
		return octetStr;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}

	public void setWidth(int width) {
		this.width = width;
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

	public void setOctetStr(String octetStr) {
		this.octetStr = octetStr;
	}

}
