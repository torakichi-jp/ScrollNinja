package org.genshin.scrollninja.work.render.sprite;

import com.badlogic.gdx.math.Vector2;


/**
 * スプライトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class SpriteDef
{
	/** テクスチャのパス */
	public String textureFilePath;
	
	/** UVマップ */
	public UVMap uv;
	
	/** スプライトのローカル座標 */
	public Vector2 position;
	
	/** スプライトの大きさ */
	public Vector2 size;
	
	/** スプライトの中心座標 */
	public Vector2 origin;
}


/**
 * UVマップクラス
 */
class UVMap
{
	/** テクスチャ上でのX座標 */
	public int x;
	
	/** テクスチャ上でのY座標 */
	public int y;
	
	/** テクスチャ上での横幅 */
	public int width;
	
	/** テクスチャ上での縦幅 */
	public int height;
}