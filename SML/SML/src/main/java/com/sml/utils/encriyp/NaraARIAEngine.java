package com.sml.utils.encriyp;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

/**
 * 프로그램명  : NaraARIAEngine
 * 날짜       : 2022-02-19 / 토요일 / 오후 2:59
 * 설명       :
 */
public class NaraARIAEngine extends ARIAEngine {

	private static final String PUBLIC_KEY = "[Naravision_KebiCrypt_Ctrl]";

	private int numberOfRounds;

	public NaraARIAEngine(int keySize, String privateKey) throws InvalidKeyException, GeneralSecurityException {
		super(keySize);

		switch (keySize) {
			case 128:
				this.numberOfRounds = 12;
				break;
			case 192:
				this.numberOfRounds = 14;
				break;
			case 256:
				this.numberOfRounds = 16;
		}

		byte[] key = PUBLIC_KEY.getBytes();
		byte[] masterKey = new byte[keySize / 8];
		System.arraycopy(key, 0, masterKey, 0, key.length > masterKey.length ? masterKey.length : key.length);
		super.setKey(masterKey);
		super.setupRoundKeys();

		key = privateKey.getBytes();
		masterKey = new byte[keySize / 8];
		System.arraycopy(key, 0, masterKey, 0, key.length > masterKey.length ? masterKey.length : key.length);
		byte[] encryptKey = encrypt(masterKey);
		super.setKey(encryptKey);
		super.setupRoundKeys();
	}

	private int getBlockSize(int length) {
		int blockSize = 0;
		if (length % numberOfRounds != 0) {
			blockSize = length + (numberOfRounds - (length % numberOfRounds));
		} else {
			blockSize = length;
		}
		return blockSize;
	}

	public byte[] encrypt(byte[] bytes) throws GeneralSecurityException {
		int blockSize = getBlockSize(bytes.length);
		byte[] input = new byte[blockSize];
		byte[] output = new byte[blockSize];
		System.arraycopy(bytes, 0, input, 0, bytes.length);
		try {
			for (int offset = 0; offset < blockSize; offset += numberOfRounds) {
				encrypt(input, offset, output, offset);
			}
		} catch (InvalidKeyException e) {
			throw e;
		}
		return output;
	}

	public byte[] decrypt(byte[] bytes) throws GeneralSecurityException {
		int blockSize = getBlockSize(bytes.length);
		byte[] input = new byte[blockSize];
		byte[] output = new byte[blockSize];
		System.arraycopy(bytes, 0, input, 0, bytes.length);
		try {
			for (int offset = 0; offset < blockSize; offset += numberOfRounds) {
				decrypt(input, offset, output, offset);
			}
		} catch (InvalidKeyException e) {
			throw e;
		}
		return output;
	}

	/**
	 * @deprecated
	 */
	public void encrypt(byte plain[], byte cipher[], int size) throws InvalidKeyException {
		int len = size;
		if (size % 16 != 0)
			len = (size / 16 + 1) * 16;
		int iLoop = len / 16;
		int iMod = size % 16;
		int iCopySize = 16;
		int offset = 0;
		byte in[] = new byte[16];
		for (int i = 0; i < iLoop; i++) {
			if (i >= iLoop - 1)
				if (iMod == 0)
					iCopySize = 16;
				else
					iCopySize = iMod;
			for (int j = 0; j < 16; j++)
				in[j] = 0;

			System.arraycopy(plain, offset, in, 0, iCopySize);
			encrypt(in, 0, cipher, offset);
			offset += iCopySize;
		}
	}

	/**
	 * @deprecated
	 */
	public void decrypt(byte cipher[], byte plain[], int size) throws InvalidKeyException {
		int iLoop = size / 16;
		int offset = 0;
		for (int i = 0; i < iLoop; i++) {
			decrypt(cipher, offset, plain, offset);
			offset += 16;
		}
	}
}
