package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.stage.Stage;
import org.genshin.scrollninja.stage.StageInterface;
import org.genshin.scrollninja.work.object.gui.Cursor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * ゲームスクリーン
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class Hoge
{
	public Vector2 position;
	public Object test;
}
class Test
{
	public int test;
}
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
		return null;//new Cursor(2.0f);
	}
	
	
	/** ステージオブジェクト */
	private final StageInterface stage;
}
