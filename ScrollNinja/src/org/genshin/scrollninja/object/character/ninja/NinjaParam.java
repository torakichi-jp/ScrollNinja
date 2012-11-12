/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja;

import java.io.IOException;

import org.genshin.scrollninja.utils.FixtureDefFromXML;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 忍者関連の定数等を定義する。
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
		Element root = XMLFactory.getInstance().get("data/xml/object.xml");
		root = root.getChildByName("Ninja");
		
		// 物理演算関連
		Element fixtureDef = root.getChildByName("FixtureDef");
		
		FIXTURE_DEF = new FixtureDefFromXML(fixtureDef);
		
		// 忍者の動き関連
		Element param = root.getChildByName("Param");
		
		// 走り
		RUN_ACCEL			= param.getFloat("RunAccel");
		RUN_MAX_VELOCITY	= param.getFloat("RunMaxVelocity");
		
		// ダッシュ
		DASH_ACCEL			= param.getFloat("DashAccel");
		DASH_MAX_VELOCITY	= param.getFloat("DashMaxVelocity");
		
		// ジャンプ
		JUMP_POWER			= param.getFloat("JumpPower");
		AERIAL_JUMP_COUNT	= param.getInt("AerialJumpCount");
	}
	
	/** Fixtureの定義情報 */
	final FixtureDefFromXML FIXTURE_DEF;
	
	/** 走りの加速度 */
	final float RUN_ACCEL;
	
	/** 走りの最高速度 */
	final float RUN_MAX_VELOCITY;

	/** ダッシュの加速度 */
	final float DASH_ACCEL;
	
	/** ダッシュの最高速度 */
	final float DASH_MAX_VELOCITY;
	
	/** ジャンプ力 */
	final float JUMP_POWER;
	
	/** 空中でジャンプできる回数 */
	final int AERIAL_JUMP_COUNT;
}
