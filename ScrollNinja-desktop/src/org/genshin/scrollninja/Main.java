package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.genshin.scrollninja.packaging.PackageUpdate;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		//---- アプリケーションの設定
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Scroll Ninja";
		cfg.useGL20 = false;
		cfg.resizable = false;
		
		// フルスクリーンモード・ウィンドウモードの設定
		if(GlobalDefine.INSTANCE.FULLSCREEN)
		{
			final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			cfg.width = d.width;
			cfg.height = d.height;
			cfg.fullscreen = true;
		}
		else
		{
			cfg.width = GlobalDefine.INSTANCE.CLIENT_WIDTH;
			cfg.height = GlobalDefine.INSTANCE.CLIENT_HEIGHT;
		}
		
		// アイコンの設定
		cfg.addIcon("data/textures/icon/128.png", FileType.Internal);
		cfg.addIcon("data/textures/icon/32.png", FileType.Internal);
		cfg.addIcon("data/textures/icon/16.png", FileType.Internal);
		
		//---- Check for updates [更新を確認]
		PackageUpdate update = new PackageUpdate();
		update.CheckForUpdates("http://files.genshin.org/ScrollNinja/data/package.json");
		
		//---- アプリケーションを生成
		new LwjglApplication(new ScrollNinja(), cfg);
	}
}