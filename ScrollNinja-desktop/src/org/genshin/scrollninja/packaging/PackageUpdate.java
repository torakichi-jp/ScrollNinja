package org.genshin.scrollninja.packaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;;

public class PackageUpdate {
	private PackageInfo remotePackageInfo;
	
	public Boolean CheckForUpdates(String address) {
		remotePackageInfo = getPackageInfoStream(address);
		
		return false;
	}
	
	public PackageInfo getPackageInfoStream(String address) {
		PackageInfo info = null;
		InputStream inputStream = null;
		URLConnection connection = null;

		try {
			URL fileURL;
			byte[] buf;
			int ByteRead = 0;
			int ByteWritten = 0;
			fileURL = new URL(address);

			connection = fileURL.openConnection();
			inputStream = (InputStream) connection.getInputStream();
			
			/*JsonFactory jFactory = new JsonFactory();
			JsonParser jParser = jFactory.createJsonParser(inputStream.toString());*/
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.readValue(inputStream.toString(), PackageInfo.class);
			
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return info;
	}

	private String getMD5(String path) {
		/*
		 * MessageDigest md; try { md = MessageDigest.getInstance("MD5"); }
		 * catch (NoSuchAlgorithmException e1) { // TODO Auto-generated catch
		 * block e1.printStackTrace(); } InputStream is; try { is = new
		 * FileInputStream("file.txt"); } catch (FileNotFoundException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } try { is = new
		 * DigestInputStream(is, md); // read stream to EOF as normal... }
		 * finally { // is.close(); }
		 */
		return "PPP";// md.digest();
	}
}
