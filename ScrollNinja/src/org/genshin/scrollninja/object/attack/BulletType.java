package org.genshin.scrollninja.object.attack;

/**
 * 銃弾型の攻撃の初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class BulletType implements AttackTypeInterface
{
	/** スプライトの初期化用定義ファイルのパス */
	public String spriteFilePath;
	
	/** アニメーションの初期化用定義ファイルのパス */
	public String animationFilePath;
	
	/** 再生するアニメーションの名前 */
	public String animationName;
	
	/** 弾丸の速度 */
	public float velocity;
	
	/** 弾丸の回転速度 */
	public float angularVelocity;
}
