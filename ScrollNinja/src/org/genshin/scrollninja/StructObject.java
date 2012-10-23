package org.genshin.scrollninja;

public class StructObject {
	
	public int		type;			/* オブジェクトの種類
	 								 * ０				プレイヤー
	 								 * １０〜９９			アイテム
	 								 * １００〜９９９		敵
	 								 * １０００〜			ステージオブジェクト
	 								 */
	public float	positionX;		// 座標Ｘ
	public float	positionY;		// 座標Ｙ
	public int		priority;		// 優先度（数値が低い方が手前）
	
	/**
	 * コンストラクタ
	 */
	public StructObject(){
		type = 0;
		positionX = 0.0f;
		positionY = 0.0f;
		priority = 0;
	}
}
