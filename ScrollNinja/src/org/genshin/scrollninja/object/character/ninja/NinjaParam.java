/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja;

/**
 * 忍者の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum NinjaParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	NinjaParam()
	{
		// 走り
		RUN_ACCEL			= 5.0f;
		RUN_VELOCITY_MAX	= 20.0f;
		
		// ダッシュ
		DASH_ACCEL			= 10.0f;
		DASH_VELOCITY_MAX	= 40.0f;
		
		// ジャンプ
		JUMP_POWER				= 70.0f;
		AERIAL_JUMP_COUNT_MAX	= 1;
	}
	
	/** 走りの加速度 */
	final float RUN_ACCEL;
	
	/** 走りの最高速度 */
	final float RUN_VELOCITY_MAX;

	/** ダッシュの加速度 */
	final float DASH_ACCEL;
	
	/** ダッシュの最高速度 */
	final float DASH_VELOCITY_MAX;
	
	/** ジャンプ力 */
	final float JUMP_POWER;
	
	/** 空中でジャンプできる回数 */
	final int AERIAL_JUMP_COUNT_MAX;
}
