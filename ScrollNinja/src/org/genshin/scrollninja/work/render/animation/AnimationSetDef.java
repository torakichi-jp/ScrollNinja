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
	
	/** テクスチャアニメーションの初期化用定義配列 */
	public TextureAnimationPair[] animations;
}

class TextureAnimationPair
{
	/** アニメーションの名前 */
	public String name;
	
	/** アニメーションの初期化用定義 */
	public TextureAnimationDef animation;
}
