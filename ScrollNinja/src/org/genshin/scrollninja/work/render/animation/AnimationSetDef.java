package org.genshin.scrollninja.work.render.animation;

import java.awt.Point;

/**
 * アニメーションセットの初期化用定義
 * @author kou
 * @since		1.0
 * @version		1.0
 */
class AnimationSetDef
{
	/** UVマップの大きさ */
	public Point uvSize;
	
	/** アニメーションの初期化用定義配列 */
	public AnimationPair[] animations;
	
}

class AnimationPair
{
	/** アニメーションの名前 */
	public String name;
	
	/** アニメーションの初期化用定義 */
	public AnimationDef animation;
}
