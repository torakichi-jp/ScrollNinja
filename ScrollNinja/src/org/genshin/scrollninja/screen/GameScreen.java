package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.object.ninja.AbstractNinja;
import org.genshin.scrollninja.object.ninja.Ninja;
import org.genshin.scrollninja.stage.Stage;
import org.genshin.scrollninja.stage.StageInterface;
import org.genshin.scrollninja.work.collision.CollisionListener;
import org.genshin.scrollninja.work.object.gui.Cursor;
import org.genshin.scrollninja.work.object.utils.CameraTranslater;

import com.badlogic.gdx.math.Vector2;
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
		
		//---- 衝突判定の監視
		world.setContactListener(new CollisionListener());
		
		//---- ステージを生成する。
		stage = new Stage(world, "data/jsons/stage/stage_test.json");
		
		//---- 忍者を生成する。
		final AbstractNinja ninja = new Ninja(world, stage.getSize(), getCursor());
		
		//---- カメラの追従設定
		final CameraTranslater cameraTranslater = new CameraTranslater();
		final Vector2 stageSize = stage.getSize();
		cameraTranslater.addTargetObject(getCursor());
		cameraTranslater.addTargetObject(ninja);
		cameraTranslater.setTranslateArea(-stageSize.x * 0.5f, -stageSize.y * 0.5f, stageSize.x, stageSize.y);
	}

	@Override
	protected Cursor createCursor()
	{
		return new Cursor(2.0f);
	}
	
	
	/** ステージオブジェクト */
	private final StageInterface stage;
}
