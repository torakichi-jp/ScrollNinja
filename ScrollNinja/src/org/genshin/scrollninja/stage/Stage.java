package org.genshin.scrollninja.stage;

import java.io.IOException;
import java.util.ArrayList;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.object.background.BackgroundDef;
import org.genshin.scrollninja.work.object.background.BackgroundLayer;
import org.genshin.scrollninja.work.object.terrain.Terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * ステージ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Stage implements StageInterface
{
	/**
	 * コンストラクタ
	 * @param world				所属先となるWorldオブジェクト
	 * @param stageDefFile		ステージの初期化用定義ファイルのパス
	 */
	public Stage(World world, String stageDefFile)
	{
		//---- ステージの初期化用定義をファイルから読み込む
		final ObjectMapper objectMapper = new ObjectMapper();
		StageDef stageDef = null;
		
		try
		{
			stageDef = objectMapper.readValue(Gdx.files.internal(stageDefFile).read(), StageDef.class);
		}
		catch (JsonParseException e) { e.printStackTrace(); }
		catch (JsonMappingException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		
		// エラーチェック
		if(stageDef == null)
		{
			size = null;
			startPosition = null;
			terrain = null;
			return;
		}
		
		//---- フィールドに値を設定する。
		size = stageDef.size.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		startPosition = stageDef.startPosition.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
		
		//---- 地形オブジェクトを生成する。
		terrain = new Terrain(stageDef.collisionFilePath, world, Vector2.tmp.set(-size.x * 0.5f, -size.y * 0.5f));
		
		//---- 描画オブジェクトを生成する。
		createBackgroundLayers(stageDef.farLayers, GlobalDefine.RenderDepth.FAR_BACKGROUND);
		createBackgroundLayers(stageDef.nearLayers, GlobalDefine.RenderDepth.NEAR_BACKGROUND);
	}

	@Override
	public void dispose()
	{
		//---- 地形オブジェクトを破棄する。
		if(terrain != null)
		{
			terrain.dispose();
		}
		
		//---- 背景レイヤーを破棄する。
		for(BackgroundLayer backgroundLayer : backgroundLayers)
		{
			backgroundLayer.dispose();
		}
		backgroundLayers.clear();
	}

	@Override
	public boolean isDisposed()
	{
		return backgroundLayers.isEmpty();
	}

	@Override
	public Vector2 getSize()
	{
		return size;
	}

	@Override
	public Vector2 getStartPosition()
	{
		return startPosition;
	}
	
	/**
	 * 背景レイヤーオブジェクトを生成する。
	 * @param backgroundLayerDefs		背景レイヤーオブジェクトの初期化用定義の配列
	 * @param renderDepth				背景レイヤーの描画処理の優先順位
	 */
	private void createBackgroundLayers(BackgroundLayerDef[] backgroundLayerDefs, int renderDepth)
	{
		//---- エラーチェック
		if(backgroundLayerDefs == null)
			return;
		
		//---- 背景レイヤー、背景オブジェクトを生成する
		for(BackgroundLayerDef backgroundLayerDef : backgroundLayerDefs)
		{
			// エラーチェック
			if(backgroundLayerDef.backgrounds == null)
				continue;
			
			// 背景レイヤー生成
			final BackgroundLayer backgroundLayer = new BackgroundLayer(size, backgroundLayerDef.scale, renderDepth);
			backgroundLayers.add(backgroundLayer);
			
			// 背景オブジェクト生成
			for(BackgroundDef backgroundDef : backgroundLayerDef.backgrounds)
			{
				if(backgroundDef.position != null)
					backgroundDef.position.mul(GlobalDefine.INSTANCE.WORLD_SCALE);
				backgroundLayer.createBackground(backgroundDef);
			}
		}
	}
	
	
	/** ステージの大きさ */
	private final Vector2 size;
	
	/** プレイヤーの初期座標 */
	private final Vector2 startPosition;
	
	/** 地形オブジェクト */
	private final Terrain terrain;
	
	/** 背景レイヤーの配列 */
	private final ArrayList<BackgroundLayer> backgroundLayers = new ArrayList<BackgroundLayer>();
}
