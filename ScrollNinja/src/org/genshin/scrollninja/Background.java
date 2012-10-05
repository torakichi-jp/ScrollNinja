package org.genshin.scrollninja;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.Sprite;


public class Background extends ObJectBase{
	
	private static final Background Instance = new Background();			// このクラスの唯一のインスタンスを作ります
	
	// インスタンスを返す
	public static Background GetInstace() {
		return Instance;
	}
	
	private float zIndex;		// Zインデックス
	
	// コンストラクタ
	public Background() {
		zIndex = 0.0f;
	}
	
}