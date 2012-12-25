package org.genshin.scrollninja.packaging;

import java.util.ArrayList;

public class PackageInfo {
	public class AssetInfo {
		String path;
		String name;
		String md5;
	}
	
	public String version;
	public ArrayList<AssetInfo> assets;
}
