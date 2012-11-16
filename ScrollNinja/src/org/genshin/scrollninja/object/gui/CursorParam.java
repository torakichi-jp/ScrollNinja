package org.genshin.scrollninja.object.gui;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.utils.SpriteLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * カーソル関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum CursorParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private CursorParam()
	{
		//---- スプライト関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.SPRITE_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Cursor");
			
			SPRITE_LOADER = new SpriteLoader(rootElement);
		}
	}

	
	/** スプライトの定義情報 */
	final SpriteLoader SPRITE_LOADER;
}
