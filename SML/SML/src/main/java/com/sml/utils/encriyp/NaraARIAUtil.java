package com.sml.utils.encriyp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

/**
 * 프로그램명  : NaraARIAUtil
 * 날짜       : 2022-02-19 / 토요일 / 오후 2:59
 * 설명       :
 */
public class NaraARIAUtil {

	public static String ariaEncrypt(String str, String privateKey) throws Exception {
		if (str == null || str.equals(""))
			return "";
		NaraARIAEngine aria = new NaraARIAEngine(256, privateKey);
		byte[] b = aria.encrypt(str.getBytes());
		return toHexString(b);
	}

	public static void ariaFileEncrypt(String privateKey, String src, String dest)
			throws IOException, InvalidKeyException, GeneralSecurityException {
		File f = new File(src);
		FileInputStream fis = new FileInputStream(src);

		long length = f.length();
		byte[] b = new byte[(int) length];

		try {
			int offset = 0;
			int numRead = 0;

			while (offset < b.length && (numRead = fis.read(b, offset, b.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < b.length) {
				throw new IOException(f.getName());
			}
		} finally {
			fis.close();
		}

		NaraARIAEngine instance = new NaraARIAEngine(256, privateKey);
		int len = b.length;

		if ((len % 16) != 0) {
			len = (len / 16 + 1) * 16;
		}

		byte[] c = new byte[len];
		System.arraycopy(b, 0, c, 0, b.length);
		instance.encrypt(b, c, b.length);
		FileOutputStream fos = new FileOutputStream(dest);

		try {
			fos.write(c);
		} catch (IOException e) {

		} finally {
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
	}

	public static String ariaDecrypt(String strHex, String privateKey) throws Exception {
		if (strHex == null || strHex.equals(""))
			return "";
		NaraARIAEngine aria = new NaraARIAEngine(256, privateKey);
		byte[] b = toByteArray(strHex);
		b = aria.decrypt(b);
		return new String(b).trim();
	}

	public static void ariaFileDecrypt(String privateKey, String src, String dest)
			throws IOException, GeneralSecurityException {
		File f = new File(src);
		FileInputStream fis = new FileInputStream(src);

		long length = f.length();
		byte[] b = new byte[(int) length];

		try {
			int offset = 0;
			int numRead = 0;
			while (offset < b.length && (numRead = fis.read(b, offset, b.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < b.length) {
				throw new IOException(f.getName());
			}
		} finally {
			fis.close();
		}

		NaraARIAEngine instance = new NaraARIAEngine(256, privateKey);
		int len = b.length;
		if ((len % 16) != 0) {
			len = (len / 16 + 1) * 16;
		}

		byte[] c = new byte[len];
		System.arraycopy(b, 0, c, 0, b.length);
		instance.decrypt(b, c, b.length);

		FileOutputStream fos = new FileOutputStream(dest);

		try {
			fos.write(c);
		} catch (IOException e) {

		} finally {
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
	}

	private static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		String hexString = null;
		for (int i = 0; i < bytes.length; i++) {
			hexString = Integer.toHexString(0xFF & bytes[i]);
			if (hexString.length() == 1) {
				sb.append('0');
			}
			sb.append(hexString.toUpperCase());
		}
		return sb.toString();
	}

	private static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
		}
		return digit;
	}

	private static byte[] toByteArray(String hexString) {
		char[] data = hexString.toCharArray();
		int len = data.length;
		if ((len & 0x01) != 0) {
			throw new IllegalArgumentException("Odd number of characters.");
		}
		byte[] out = new byte[len >> 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

}
