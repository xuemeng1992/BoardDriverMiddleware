package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class LineOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_LINE;

	public int getOptCode() {
		return optCode;
	}

	/**
	 * 
	 * @param beginX
	 *            x轴起始坐标
	 * @param beginY
	 *            y轴起始坐标
	 * @param endX
	 *            x轴结束坐标
	 * @param endY
	 *            y轴结束坐标
	 * @param width
	 *            线段宽度
	 * @return
	 */
	public static LineOpt create(int beginX, int beginY, int endX, int endY, int width) {
		LineOpt opt = new LineOpt();
		opt.beginX = beginX;
		opt.beginY = beginY;
		opt.endX = endX;
		opt.endY = endY;
		opt.width = width;
		return opt;
	}

	public static LineOpt fromJson(JSONObject jsonObject) throws JSONException {

		return create(jsonObject.getInt("beginX"), jsonObject.getInt("beginY"), jsonObject.getInt("endX"),
				jsonObject.getInt("endY"), jsonObject.getInt("width"));
	}

	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		return "LINE " + beginX + " " + beginY + " " + endX + " " + endY + " " + width + " " + "\r\n";
	}

	int beginX; // x轴起始坐标
	int beginY; // y轴起始坐标

	int endX; // x轴结束坐标
	int endY; // y轴结束坐标

	int width; // 线段宽度

	public int getBeginX() {
		return beginX;
	}

	public int getBeginY() {
		return beginY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public int getWidth() {
		return width;
	}

	public void setOptCode(int optCode) {
		this.optCode = optCode;
	}

	public void setBeginX(int beginX) {
		this.beginX = beginX;
	}

	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
