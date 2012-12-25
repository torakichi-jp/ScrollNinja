package org.genshin.scrollninja.packaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;

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
			fileURL = new URL(address);

			connection = fileURL.openConnection();
			inputStream = (InputStream) connection.getInputStream();
			
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); 
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			
			String streamContent = responseStrBuilder.toString();
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.readValue(streamContent, PackageInfo.class);
			System.out.println(streamContent);
			
			inputStream.close();
		} catch (IOException e) {
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
