package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.stage.Stage;
import org.genshin.scrollninja.stage.StageInterface;
import org.genshin.scrollninja.work.object.TestObject;
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

		//---- ステージを生成する。
		stage = new Stage(world, "data/jsons/stage/stage_test.json");
		
//		//---- 忍者を生成する。
//		AbstractObject player = new PlayerNinja(world, stage.getStartPosition(), getCursor());
//		
//		Global.currentUpdatableManager.add(player, 0);
//		Global.currentRenderableManager.add(player, 0);
//		
//		//---- カメラの追従設定
		final CameraTranslater cameraTranslater = new CameraTranslater();
		final Vector2 stageSize = stage.getSize();
		cameraTranslater.addTargetObject(new TestObject(stage.getStartPosition()));
//		cameraTranslater.addTargetObject(getCursor());
//		cameraTranslater.addTargetObject(player);
//		cameraTranslater.setTranslateArea(0.0f, 0.0f, stageSize.x, stageSize.y);
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
