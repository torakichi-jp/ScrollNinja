package org.genshin.scrollninja;

import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * プロジェクト全体で共有するパラメータ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public enum GlobalParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private GlobalParam()
	{
		Element rootElement = XMLFactory.getInstance().get("data/xml/global_param.xml");
		
		// 世界の単位
		WORLD_SCALE = rootElement.getFloat("WorldScale", 0.1f);
		
		// ディレクトリパス
		String XMLDirectoryPath		= rootElement.get("XMLDirectoryPath");
		COLLISION_DIRECTORY_PATH	= rootElement.get("CollisionDirectoryPath");
		TEXTURE_DIRECTORY_PATH		= rootElement.get("TextureDirectoryPath");
		
		// XMLファイルのパス
		OBJECT_PARAM_XML_FILE_PATH		= XMLDirectoryPath + rootElement.get("ObjectParamXMLFileName");
		COLLISION_PARAM_XML_FILE_PATH	= XMLDirectoryPath + rootElement.get("CollisionParamXMLFileName");
		SPRITE_PARAM_XML_FILE_PATH		= XMLDirectoryPath + rootElement.get("SpriteParamXMLFileName");
	}
	
	/** 世界の単位 */
	public final float WORLD_SCALE;
	
	/** 衝突に関連するファイルを格納しているディレクトリのパス */
	public final String COLLISION_DIRECTORY_PATH;
	
	/** テクスチャを格納しているディレクトリのパス */
	public final String TEXTURE_DIRECTORY_PATH;
	
	/** オブジェクトパラメータを記述したXMLファイルのパス */
	public final String OBJECT_PARAM_XML_FILE_PATH;
	
	/** 衝突パラメータを記述したXMLファイルのパス */
	public final String COLLISION_PARAM_XML_FILE_PATH;
	
	/** スプライトパラメータを記述したXMLファイルのパス */
	public final String SPRITE_PARAM_XML_FILE_PATH;
}
