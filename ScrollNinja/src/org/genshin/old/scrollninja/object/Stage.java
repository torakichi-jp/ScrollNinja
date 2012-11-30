

package org.genshin.old.scrollninja.object;

import java.util.ArrayList;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.object.StageDataList.StageData;
import org.genshin.old.scrollninja.object.item.Item;
import org.genshin.old.scrollninja.object.weapon.WeaponManager;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.render.CameraTranslater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Stage implements StageBase {

	private Box2DDebugRenderer		renderer;		//
	private ArrayList<Item>			popItems;		//
	private ArrayList<Enemy>		popEnemys;		//

	private int						stageNum;		// ステージナンバー
	private StageData	 			stageData;		// ステージのデータ
	
	private Cursor cursor;
	private CameraTranslater cameraTranslater;
	
	private boolean prevInput;
	private boolean renderDebug = false;

	// コンストラクタ
	public Stage(int num){
		stageNum = num;
		stageData = StageDataList.lead(stageNum);

		renderer = new Box2DDebugRenderer();
		
		cursor = new Cursor(GameMain.camera, 2.0f * GlobalParam.INSTANCE.WORLD_SCALE);
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update(float deltaTime) {
		cursor.update(deltaTime);
		CollisionDetector.HitTest();			// これ最初にやってほしいかも？
		EnemyManager.Update(deltaTime);
		WeaponManager.Update(deltaTime);
		PlayerManager.Update(deltaTime);
		ItemManager.Update(deltaTime);
		cameraTranslater.update(deltaTime);
		BackgroundManager.backgroundList.update(deltaTime);

		// TODO 今だけリポップ
//		if (EnemyManager.enemyList.get(0) == null && EnemyManager.enemyList.get(1) == null) {
//			EffectManager.enemyEffectList = new ArrayList<Effect>();
//			EnemyManager.dispose();
//		}
//
//		// TODO 今だけリポップ
//		if( EnemyManager.enemyList.size() == 0) {
//			for(StageData.EnemyData enemy : stageData.enemyData)
//			{
//				EnemyManager.CreateEnemy(enemy.type, enemy.position);
//			}
//		}

//		GameMain.playerInfo.update();
		
		
		// TODO 最終的には消すハズ
		if( Gdx.input.isKeyPressed(Keys.H) )
		{
			if(!prevInput)
			{
				renderDebug = !renderDebug;
			}
			prevInput = true;
		}
		else
		{
			prevInput = false;
		}
	}

	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public void Draw() {
		// 全部クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		GameMain.spriteBatch.setProjectionMatrix(GameMain.camera.combined);		// プロジェクション行列のセット
		GameMain.spriteBatch.begin();											// 描画開始
		{
			BackgroundManager.backgroundList.Draw(0);
			BackgroundManager.backgroundList.Draw(1);
			StageObjectManager.Draw();
			PlayerManager.Draw();
			EnemyManager.Draw();
			EffectManager.Draw();
			ItemManager.Draw();
			BackgroundManager.backgroundList.Draw(2);
//			GameMain.playerInfo.Draw();
			cursor.render();
		}
		GameMain.spriteBatch.end();										// 描画終了

		// TODO リリース前にこの処理をクリア直後に持ってくる
		if(renderDebug)	renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);

		/**
		 * ↑rendererの処理を前に持っていくと位置がずれることがあるので
		 * 微調整が必要な場合は一度確認した方がよさそうです。
		 */
	}

	//************************************************************
	// PopEnemy
	// 敵の出現タイミングの設定
	//************************************************************
	public void PopEnemy() {
/*		if( PlayerManager.GetPlayer("プレイヤー").GetPosition().x > 200 ) {
			EnemyManager.CreateEnemy(Enemy.NORMAL, 20.0f, 30.0f);

		}*/
	}

	// アイテムポップ
	public void popItem() {

	}

	public void moveBackground() {

	}

	public PlayerNinja spawnPlayer(PlayerNinja player) {
		return player;
	}

	public int timeCount(int nowTime) {
		// 制限時間 or 経過時間加算
		nowTime += 1;

		return nowTime;
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Stage GetStage() { return this; }
	public int GetStageNum(){ return stageNum; }

	@Override
	public void Init() {
		PlayerManager.CreatePlayer(stageData.startPosition, cursor);
		// TODO 後でステージオブジェクトリスト追加
//		StageObjectManager.CreateStageObject(StageObject.ROCK, 200.0f, 38.0f);
		for(StageData.EnemyData enemy : stageData.enemyData)
		{
			EnemyManager.CreateEnemy(enemy.type, enemy.position);
		}
		
		cameraTranslater = new CameraTranslater(GameMain.camera);
		cameraTranslater.addTargetObject(cursor);
		cameraTranslater.addTargetObject(PlayerManager.GetPlayer(0));

		cameraTranslater.setTranslateArea(0.0f, 0.0f, stageData.size.x, stageData.size.y);
	}

	@Override
	public void Release() {
	}

	/**************************************************
	 * dispose()
	 * 解放処理
	 * 他の画面の移動する場合は行わなければ描画がおかしくなる
	 * TODO 検証不足につき他で不具合が出るかも…
	 **************************************************/
	public void dispose() {
		EffectManager.dispose();
		WeaponManager.dispose();
		PlayerManager.dispose();
		EnemyManager.dispose();
		BackgroundManager.dispose();
		ItemManager.dispose();
		StageObjectManager.dispose();

		renderer.dispose();
	}

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}