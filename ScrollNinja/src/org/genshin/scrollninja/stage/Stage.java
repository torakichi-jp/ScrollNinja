package org.genshin.scrollninja.stage;

import java.util.ArrayList;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.background.BackgroundDef;
import org.genshin.scrollninja.object.background.BackgroundLayer;
import org.genshin.scrollninja.object.character.enemy.TestEnemy;
import org.genshin.scrollninja.object.terrain.Terrain;
import org.genshin.scrollninja.utils.JsonUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


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
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		
		//---- ステージの初期化用定義をファイルから読み込む
		final StageDef stageDef = JsonUtils.read(stageDefFile, StageDef.class);
		
		// エラーチェック
		if(stageDef == null)
		{
			size = null;
			startPosition = null;
			terrain = null;
			return;
		}
				
		//---- フィールドに値を設定する。
		size = stageDef.size.mul(worldScale);
		startPosition = stageDef.startPosition.mul(worldScale);
		
		//---- 地形オブジェクトを生成する。
		terrain = new Terrain(stageDef.collisionFilePath, world, Vector2.tmp.set(-size.x * 0.5f, -size.y * 0.5f));
		
		//---- 描画オブジェクトを生成する。
		backgroundLayers.ensureCapacity(stageDef.farLayers.length + stageDef.nearLayers.length);
		createBackgroundLayers(stageDef.farLayers, GlobalDefine.RenderDepth.FAR_BACKGROUND);
		createBackgroundLayers(stageDef.nearLayers, GlobalDefine.RenderDepth.NEAR_BACKGROUND);
		
		//---- 敵オブジェクトを生成する。
		for(EnemyDef def : stageDef.enemies)
		{
			new TestEnemy(world, def.position.mul(worldScale));
		}
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
	private final ArrayList<BackgroundLayer> backgroundLayers = new ArrayList<BackgroundLayer>(1);
}
