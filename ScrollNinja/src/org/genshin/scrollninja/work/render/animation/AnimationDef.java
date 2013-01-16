package org.genshin.scrollninja.work.render.animation;

import java.awt.Point;

/**
 * アニメーションの初期化用定義
 * @author kou
 * @since		1.0
 * @version		1.0
 */
class AnimationDef
{
	/** テクスチャのパス */
	public String texture;
	
	/** UVマップの大きさ */
	public Point uvSize;
	
	/** アニメーションの初期コマ */
	public Point start;
	
	/** アニメーションのフレーム番号を再生順に格納した配列 */
	public int[] frames;
	
	/** アニメーションの再生速度（1コマあたりの経過フレーム数） */
	public int time;
	
	/** ループフラグ */
	public boolean looping;
}
