package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.utils.FixtureDefFromXML;
import org.genshin.scrollninja.utils.XMLFactory;

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
		Element root = XMLFactory.getInstance().get("data/xml/object_param.xml");
		root = root.getChildByName("Ninja");
		
		//---- 忍者の挙動関連
		// 走り
		RUN_ACCEL			= root.getFloat("RunAccel");
		RUN_MAX_VELOCITY	= root.getFloat("RunMaxVelocity");
		
		// ダッシュ
		DASH_ACCEL			= root.getFloat("DashAccel");
		DASH_MAX_VELOCITY	= root.getFloat("DashMaxVelocity");
		
		// ジャンプ
		JUMP_POWER			= root.getFloat("JumpPower");
		AERIAL_JUMP_COUNT	= root.getInt("AerialJumpCount");
		
		//---- 衝突関連
		Element bodyFixtureDef = root.getChildByName("BodyFixtureDef");
		BODY_FIXTURE_DEF = new FixtureDefFromXML(bodyFixtureDef);
		Element footFixtureDef = root.getChildByName("FootFixtureDef");
		FOOT_FIXTURE_DEF = new FixtureDefFromXML(footFixtureDef);
	}
	
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
	
	/** 上半身Fixtureの定義情報 */
	final FixtureDefFromXML BODY_FIXTURE_DEF;
	
	/** 下半身Fixtureの定義情報 */
	final FixtureDefFromXML FOOT_FIXTURE_DEF;
}
