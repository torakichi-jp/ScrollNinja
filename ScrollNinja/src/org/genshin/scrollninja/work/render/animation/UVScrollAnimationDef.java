package org.genshin.scrollninja.work.render.animation;

import java.awt.Point;

/**
 * UVスクロールアニメーションの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class UVScrollAnimationDef
{
	/** テクスチャファイルのパス */
	public String textureFilePath;
	
	/** UVマップの大きさ */
	public Point uvSize;
	
	/** アニメーション開始時のUV座標 */
	public Point start;
	
	/** アニメーション終了時のUV座標 */
	public Point end;
	
	/** アニメーション開始から終了までの時間（単位：秒） */
	public float time;
	
	/** ループフラグ */
	public boolean looping;
}
