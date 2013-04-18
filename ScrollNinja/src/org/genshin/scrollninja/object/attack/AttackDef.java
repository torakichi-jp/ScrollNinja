package org.genshin.scrollninja.object.attack;

/**
 * 攻撃の初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class AttackDef
{
	/** 攻撃力 */
	float power;
	
	/** 衝突ファイルのパス */
	String collisionFilePath;
	
	/** 攻撃の種類 */
	AttackTypeInterface type;
}
