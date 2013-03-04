package org.genshin.scrollninja.object.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * 背景オブジェクトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class BackgroundDef
{
	/** スプライトを定義したファイルのパス */
	public 	String spriteFilePath;
	
	/** アニメーションを定義したファイルのパス */
	public String animationFilePath;
	
	/** 再生するアニメーションの名前 */
	public String animationName;
	
	/** 背景オブジェクトの座標 */
	public Vector2 position;
	
	/** 色 */
	public Color color;
}
