package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.work.collision.CollisionObject;
import org.genshin.scrollninja.work.object.gui.Cursor;

import com.badlogic.gdx.physics.box2d.World;

/**
 * ゲームスクリーン
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class GameScreen extends AbstractDebugScreen		// FIXME リリース時はAbstractScreenを継承する。
{
	/**
	 * コンストラクタ
	 */
	public GameScreen()
	{		
		final World world = getWorld();

		//---- 衝突判定の実験場
		CollisionObject co = new CollisionObject("data/jsons/collision/ninja.json", world, null);
		
		
//		//---- カーソル
//		Cursor cursor = getCursor();
//
//		Global.currentUpdatableManager.add(cursor, 0);
//		Global.currentRenderableManager.add(cursor, 0);
//		
//		//---- ステージを生成する。
//		stage = new Stage(world);
//		
//		//---- 忍者を生成する。
//		AbstractObject player = new PlayerNinja(world, stage.getStartPosition(), getCursor());
//		
//		Global.currentUpdatableManager.add(player, 0);
//		Global.currentRenderableManager.add(player, 0);
//		
//		//---- カメラの追従設定
//		final CameraTranslater cameraTranslater = new CameraTranslater(getCamera());
//		cameraTranslater.addTargetObject(getCursor());
//		cameraTranslater.addTargetObject(player);
//		cameraTranslater.setTranslateArea(0.0f, 0.0f, 38.4f, 25.0f);
//
//		Global.currentUpdatableManager.add(cameraTranslater, 0);
	}

	@Override
	protected Cursor createCursor()
	{
		return new Cursor(2.0f);
	}
	
	
	/** ステージオブジェクト */
//	private final StageInterface stage;
}
