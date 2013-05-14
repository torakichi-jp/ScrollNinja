package org.genshin.scrollninja.render.sprite;

import java.awt.Rectangle;

import com.badlogic.gdx.math.Vector2;


/**
 * スプライトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class SpriteDef
{
	/** テクスチャのパス */
	public String textureFilePath;
	
	/** UVマップ */
	public Rectangle uv;
	
	/** スプライトのローカル座標 */
	public Vector2 position;
	
	/** スプライトの大きさ */
	public Vector2 size;
	
	/** スプライトの中心座標 */
	public Vector2 origin;
}
