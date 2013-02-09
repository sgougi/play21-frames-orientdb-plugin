/*
 * Copyright since 2013 Shigeru GOUGI (sgougi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wingnest.play2.frames.plugin.orientdb;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import play.Configuration;
import play.Play;

import com.orientechnologies.orient.core.id.ORID;
import com.wingnest.play2.frames.plugin.exceptions.FramesUnexpectedException;


final public class CustomIdManager {

	private static CustomIdHandler CUSTOM_ID_HANDLER = null;

	public String encodeCustomId(ORID identity) {
		if ( identity.isTemporary() ) {
			throw new IllegalStateException("Temporary object does not ganerates custom id : " + identity.toString());
		}
		if ( CUSTOM_ID_HANDLER == null ) {
			final String rid = identity.toString();
			return Codec.byteToHexString(rid.getBytes()); 
		}
		return CUSTOM_ID_HANDLER.encode(identity);
	}			

	public String decodeCustomId(String customId) {
		if ( CUSTOM_ID_HANDLER == null ) {
			return new String(Codec.hexStringToByte(customId));
		}
		return CUSTOM_ID_HANDLER.decode(customId);
	}

	public void setCustomIdHandler(final CustomIdHandler handler) {
		CUSTOM_ID_HANDLER = handler;
	}

	public interface CustomIdHandler {
		String encode(ORID identity);

		String decode(String encodedId);
	}

	public static class Crypt {
		// from play1.2.5
		public static String encryptAES(String value) {
			final Configuration c = Play.application().configuration();
			return encryptAES(value, c.getString("application.secret").substring(0, 16));
		}

		public static String encryptAES(String value, String privateKey) {
			try {
				byte[] raw = privateKey.getBytes();
				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
				return Codec.byteToHexString(cipher.doFinal(value.getBytes()));
			} catch ( Exception ex ) {
				throw new FramesUnexpectedException(ex);
			}
		}

		public static String decryptAES(String value) {
			final Configuration c = Play.application().configuration();
			return decryptAES(value, c.getString("application.secret").substring(0, 16));
		}

		public static String decryptAES(String value, String privateKey) {
			try {
				byte[] raw = privateKey.getBytes();
				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				return new String(cipher.doFinal(Codec.hexStringToByte(value)));
			} catch ( Exception ex ) {
				throw new FramesUnexpectedException(ex);
			}
		}
	}

	public static class Codec {
		// from play1.2.5
		public static byte[] hexStringToByte(String hexString) {
			try {
				return Hex.decodeHex(hexString.toCharArray());
			} catch ( DecoderException e ) {
				throw new FramesUnexpectedException(e);
			}
		}

		public static String byteToHexString(byte[] bytes) {
			return String.valueOf(Hex.encodeHex(bytes));
		}
	}

}
