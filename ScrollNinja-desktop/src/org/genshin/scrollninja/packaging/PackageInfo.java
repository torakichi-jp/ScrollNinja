package org.genshin.scrollninja.packaging;

import java.util.ArrayList;

public class PackageInfo {
	public class AssetInfo {
		String path;
		String name;
		String md5;
	}
	
	String releaseVersion;
	ArrayList<AssetInfo> assets;
}
