package com.chenghui.agriculture.core.utils;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class BASE64Coding {
	private static BASE64Encoder encoder = new BASE64Encoder();
	private static BASE64Decoder decoder = new BASE64Decoder();

	public BASE64Coding() {
	}

	public static String getEncode(String s) {
		return encoder.encode(s.getBytes());
	}

	public static String getDecode(String s) {
		String recode = null;
		try {
			byte[] temp = decoder.decodeBuffer(s);
			recode = new String(temp);
		} catch (IOException ioe) {

		}
		return recode;
	}
}