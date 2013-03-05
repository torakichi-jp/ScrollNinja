package org.genshin.scrollninja.object.kaginawa;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 鉤縄関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum KaginawaDefine
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private KaginawaDefine()
	{
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		
		//---- 鉤縄の挙動関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalDefine.INSTANCE.XML_DIRECTORY_PATH + GlobalDefine.INSTANCE.OBJECT_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Kaginawa");
			SLACK_VELOCITY		= rootElement.getFloat("SlackVelocity") * worldScale;
			SHRINK_VELOCITY		= rootElement.getFloat("ShrinkVelocity") * worldScale;
			LENGTH				= rootElement.getFloat("Length") * worldScale;
		}
	}
	
	
	/** 鉤縄の伸びる速度（毎秒） */
	final float SLACK_VELOCITY;
	
	/** 鉤縄の縮む速度（毎秒） */
	final float SHRINK_VELOCITY;
	
	/** 鉤縄の長さ */
	final float LENGTH;
}
