package org.genshin.scrollninja.object.effect;


/**
 * 定義をファイルから読み込むエフェクトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class FileEffectDef
{
	/** スプライト定義ファイルのパス */
	public String spriteFilePath;
	
	/** アニメーション定義ファイルのパス */
	public String animationFilePath;
	
	/** 再生するアニメーション名の名前 */
	public String animationName;
	
	/** エフェクトの初期化用定義 */
	public EffectDef effectDef;
}
