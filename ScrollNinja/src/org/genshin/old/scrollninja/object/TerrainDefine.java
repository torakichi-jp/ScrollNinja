package org.genshin.old.scrollninja.object;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.FixtureDefLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 地形関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum TerrainDefine
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private TerrainDefine()
	{
		//---- 衝突関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalDefine.INSTANCE.XML_DIRECTORY_PATH + GlobalDefine.INSTANCE.COLLISION_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Terrain");
			
			FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement);
		}
	}
	
	/** Fixtureの定義情報 */
	final FixtureDefLoader FIXTURE_DEF_LOADER;
}
