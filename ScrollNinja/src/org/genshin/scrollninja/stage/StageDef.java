package org.genshin.scrollninja.stage;

import org.genshin.scrollninja.work.object.background.BackgroundDef;

import com.badlogic.gdx.math.Vector2;

/**
 * ステージの初期化用定義クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class StageDef
{
	/** ステージの大きさ */
	public Vector2 size;
	
	/** ステージ開始時のプレイヤーの初期座標 */
	public Vector2 startPosition;
	
	/** ステージの衝突判定を定義したファイルのパス */
	public String collisionFilePath;
	
	/** 遠景の背景レイヤーの初期化用定義の配列 */
	public BackgroundLayerDef[] farLayers;

	/** 近景の背景レイヤーの初期化用定義の配列 */
	public BackgroundLayerDef[] nearLayers;
}

/**
 * 背景レイヤーの初期化用定義クラス
 */
class BackgroundLayerDef
{
	/** 背景レイヤーの倍率 */
	public float scale;
	
	/** 背景オブジェクトの初期化用定義の配列 */
	public BackgroundDef[] backgrounds;
}
