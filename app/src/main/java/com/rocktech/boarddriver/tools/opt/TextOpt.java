package com.rocktech.boarddriver.tools.opt;

import org.json.JSONException;
import org.json.JSONObject;

public class TextOpt implements IPrintOpt {

	int optCode = OptTable.OPT_CODE_TEXT;

	@Override
	public int getOptCode() {
		return optCode;
	}

	/**
	 * 
	 * @param x
	 *            x轴起始坐标
	 * @param y
	 *            y轴起始坐标
	 * @param fontType
	 *            字体类型 见 OptTable.FONT_TYPE_MATRX_16x16 等字体定义
	 * @param isBold
	 *            是否加粗
	 * @param content
	 *            文字内容
	 * @return
	 */
	public static TextOpt create(int x, int y, int fontType, boolean isBold,
	        String content) {
		TextOpt opt = new TextOpt();
		opt.x = x;
		opt.y = y;
		opt.fontType = fontType;
		opt.isBold = isBold;
		opt.content = content;
		return opt;
	}
	public static TextOpt fromJson( JSONObject jsonObject ) throws JSONException {
		
		return create( jsonObject.getInt("x") ,
				jsonObject.getInt("y") , 
				jsonObject.getInt("fontType"), 
				jsonObject.getBoolean("isBold"),
				jsonObject.getString("content")) ;
	}
	@Override
	public String toCpcl() {
		// TODO Auto-generated method stub
		String strFont = "55" ;
		String strSize = "0" ;
		if( this.fontType == OptTable.FONT_TYPE_MATRX_16x16 )
		{
			strFont = "55" ; strSize = "0" ;
		}else if( this.fontType == OptTable.FONT_TYPE_MATRX_24x24 )
		{
			strFont = "24" ; strSize = "0" ;
		}else if( this.fontType == OptTable.FONT_TYPE_MATRX_24x48 )
		{
			strFont = "24" ; strSize = "3" ;
		}else if( this.fontType == OptTable.FONT_TYPE_MATRX_36x36 )
		{
			strFont = "36" ; strSize = "0" ;
		}else if( this.fontType == OptTable.FONT_TYPE_MATRX_48x48 )
		{
			strFont = "48" ; strSize = "0" ;
		}else if( this.fontType == OptTable.FONT_TYPE_MATRX_72x72 )
		{
			strFont = "72" ; strSize = "0" ;
		}
        String strRst = "TEXT " + strFont + " " + strSize + " " + x + " " + y + " " +  content + "\r\n";
        
        if( isBold )
        {
        	strRst = "SETBOLD 1 \r\n" + strRst + "SETBOLD 0 \r\n";
        }
		return  strRst ;
	}
	
	int x; // x轴起始坐标
	int y; // y轴起始坐标
	int fontType; // 字体类型
	boolean isBold; // 是否加粗
	String content; // 文字内容

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getFontType() {
		return fontType;
	}

	public boolean isBold() {
		return isBold;
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

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
