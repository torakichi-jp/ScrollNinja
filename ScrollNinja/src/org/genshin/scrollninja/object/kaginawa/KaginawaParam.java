package org.genshin.scrollninja.object.kaginawa;

import org.genshin.scrollninja.utils.FixtureDefFromXML;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 鉤縄関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum KaginawaParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	KaginawaParam()
	{
		Element root = XMLFactory.getInstance().get("data/xml/object_param.xml");
		root = root.getChildByName("Kaginawa");

		//---- 鉤縄の挙動関連
		SLACK_VELOCITY		= root.getFloat("SlackVelocity");
		SHRINK_VELOCITY		= root.getFloat("ShrinkVelocity");
		LENGTH				= root.getFloat("Length");
		COLLISION_RADIUS	= root.getChildByName("FixtureDef").getChildByName("CircleShape").getFloat("Radius");
		
		//---- 衝突関連
		FIXTURE_DEF = new FixtureDefFromXML(root);
	}
	
	
	/** 鉤縄の伸びる速度（毎秒） */
	final float SLACK_VELOCITY;
	
	/** 鉤縄の縮む速度（毎秒） */
	final float SHRINK_VELOCITY;
	
	/** 鉤縄の長さ */
	final float LENGTH;
	
	/** 衝突判定の半径（仮） */
	final float COLLISION_RADIUS;
	
	/** Fixtureの定義情報 */
	final FixtureDefFromXML FIXTURE_DEF;
}
