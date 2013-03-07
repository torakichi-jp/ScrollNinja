package org.genshin.scrollninja;

import java.io.IOException;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * プロジェクト全体で使用する定数
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public enum GlobalDefine
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private GlobalDefine()
	{
		final XmlReader xmlReader = new XmlReader();
		Element rootElement = null;
		try
		{
			if( Gdx.files == null )
			{
				final class InternalFileHandle extends FileHandle { InternalFileHandle(String fileName) { super(fileName, FileType.Internal); } }
				rootElement = xmlReader.parse(new InternalFileHandle("data/xml/global_param.xml"));
			}
			else
			{
				rootElement = xmlReader.parse(Gdx.files.internal("data/xml/global_param.xml"));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//---- クライアント領域のサイズなど
		CLIENT_WIDTH = rootElement.getInt("ClientWidth", 640);
		CLIENT_HEIGHT = rootElement.getInt("ClientHeight", 480);
		FULLSCREEN = rootElement.getBoolean("Fullscreen", false);
		
		//---- 宇宙の法則
		// 世界の単位
		WORLD_SCALE = 128.0f / rootElement.getFloat("MaxWorldVelocity", 128.0f);
		INV_WORLD_SCALE = 1.0f / WORLD_SCALE;
		
		// 重力加速度
		GRAVITY = rootElement.getFloat("Gravity", 1000.0f) * -1.0f * WORLD_SCALE;
		
		//---- ディレクトリパス
		XML_DIRECTORY_PATH			= rootElement.get("XMLDirectoryPath");
		COLLISION_DIRECTORY_PATH	= rootElement.get("CollisionDirectoryPath");
		TEXTURE_DIRECTORY_PATH		= rootElement.get("TextureDirectoryPath");
		
		//---- XMLファイルのパス
		OBJECT_PARAM_XML_FILE_NAME		= rootElement.get("ObjectParamXMLFileName");
		COLLISION_PARAM_XML_FILE_NAME	= rootElement.get("CollisionParamXMLFileName");
		SPRITE_PARAM_XML_FILE_NAME		= rootElement.get("SpriteParamXMLFileName");
	}
	
	/** クライアント領域の横幅 */
	public final int CLIENT_WIDTH;

	/** クライアント領域の縦幅 */
	public final int CLIENT_HEIGHT;
	
	/** フルスクリーンフラグ */
	public final boolean FULLSCREEN;
	
	/** 世界の単位 */
	public final float WORLD_SCALE;
	
	/** 世界の単位を逆にしたもの（1/WORLD_SCALE） */
	public final float INV_WORLD_SCALE;
	
	/** 重力加速度（毎秒） */
	public final float GRAVITY;

	/** XMLファイルを格納しているディレクトリのパス */
	public final String XML_DIRECTORY_PATH;
	
	/** 衝突に関連するファイルを格納しているディレクトリのパス */
	public final String COLLISION_DIRECTORY_PATH;
	
	/** テクスチャを格納しているディレクトリのパス */
	public final String TEXTURE_DIRECTORY_PATH;
	
	/** オブジェクトパラメータを記述したXMLファイルのパス */
	public final String OBJECT_PARAM_XML_FILE_NAME;
	
	/** 衝突パラメータを記述したXMLファイルのパス */
	public final String COLLISION_PARAM_XML_FILE_NAME;
	
	/** スプライトパラメータを記述したXMLファイルのパス */
	public final String SPRITE_PARAM_XML_FILE_NAME;
	
	
	/**
	 * 優先順位の基本クラス
	 */
	private static abstract class AbstractPriority
	{
		/**
		 * 次の優先度を取得する。
		 * @return		次の優先度
		 */
		protected static int getNextPriority()
		{
			return tmp += 100;
		}

		/** 優先順位を計算するための作業用変数 */
		private static int tmp = 0;
	}
	
	/**
	 * 更新処理の優先順位
	 */
	public static class UpdatePriority extends AbstractPriority
	{
		/** 入力 */
		public static final int INPUT			= getNextPriority();
		
		/** デフォルト */
		public static final int DEFAULT		= getNextPriority();
		
		/** デフォルト */
		public static final int ATTACK		= getNextPriority();
		
		/** 背景オブジェクト */
		public static final int BACKGROUND	= getNextPriority();
		
		/** アニメーション */
		public static final int ANIMATION		= getNextPriority();
	}

	/**
	 * 描画処理の優先順位
	 */
	public static class RenderDepth extends AbstractPriority
	{
		/** 遠景 */
		public static final int FAR_BACKGROUND	= getNextPriority();

		/** 鉤縄 */
		public static final int KAGINAWA			= getNextPriority();

		/** 忍者 */
		public static final int NINJA				= getNextPriority();
		
		/** デフォルト */
		public static final int DEFAULT			= getNextPriority();
		
		/** エフェクト */
		public static final int EFFECT			= getNextPriority();
		
		/** 近景 */
		public static final int NEAR_BACKGROUND	= getNextPriority();
		
		/** マウスカーソル */
		public static final int CURSOR			= getNextPriority();
	}
}
