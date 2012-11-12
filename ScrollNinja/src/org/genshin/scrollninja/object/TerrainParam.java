package org.genshin.scrollninja.object;

import org.genshin.scrollninja.utils.FixtureDefFromXML;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 地形関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum TerrainParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	TerrainParam()
	{
		Element root = XMLFactory.getInstance().get("data/xml/object.xml");
		root = root.getChildByName("Terrain");
		
		// 物理演算関連
		Element fixtureDef = root.getChildByName("FixtureDef");
		
		FIXTURE_DEF = new FixtureDefFromXML(fixtureDef);
	}
	
	/** Fixtureの定義情報 */
	final FixtureDefFromXML FIXTURE_DEF;
}
