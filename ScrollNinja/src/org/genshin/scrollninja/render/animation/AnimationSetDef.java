package org.genshin.scrollninja.render.animation;

import org.genshin.engine.utils.Point;

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
	public TextureAnimationPair[] textureAnimations;
	
	/** UVスクロールアニメーションの初期化用定義配列 */
	public UVScrollAnimationPair[] uvScrollAnimations;
}

class TextureAnimationPair
{
	/** アニメーションの名前 */
	public String name;
	
	/** アニメーションの初期化用定義 */
	public TextureAnimationDef animation;
}

class UVScrollAnimationPair
{
	/** アニメーションの名前 */
	public String name;
	
	/** アニメーションの初期化用定義 */
	public UVScrollAnimationDef animation;
}