

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Stage implements StageBase {

	private Box2DDebugRenderer		renderer;		//
	private ArrayList<Item>			popItems;		//
	private ArrayList<Enemy>		popEnemys;		//
	private ArrayList<Weapon>		popWeapons;

	private int						stageNum;

	// コンストラクタ
	public Stage(int num){
		stageNum = num;

		renderer			= new Box2DDebugRenderer();
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update() {
		CollisionDetector.HitTest();			// これ最初にやってほしいかも？
		EnemyManager.Update();
		BackgroundManager.GetBackground(stageNum).update();
		PlayerManager.Update();
		ItemManager.Update();

		for(int i = 0; i< EffectManager.GetListSize(); i ++) {
			EffectManager.GetEffectForLoop(i).Update();
		}

		if( EnemyManager.normalEnemyList.size() == 0) {
			EnemyManager.CreateEnemy(Enemy.NORMAL, 0.0f, 0.0f);
		}

		updateCamera();
		GameMain.playerInfo.update();
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
		GameMain.spriteBatch.begin();									// 描画開始
		{
			BackgroundManager.GetBackground(stageNum).Draw(0);
			BackgroundManager.GetBackground(stageNum).Draw(1);
			StageObjectManager.Draw();
			PlayerManager.Draw();
			EnemyManager.Draw();
			EffectManager.GetEffect(PlayerManager.GetPlayer(0).GetNowAttack()).Draw();
			ItemManager.Draw();
			BackgroundManager.GetBackground(stageNum).Draw(2);
			GameMain.playerInfo.Draw();
		}
		GameMain.spriteBatch.end();										// 描画終了

		renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);
//		player.GetBody().setAwake(true);
	}

	//************************************************************
	// updateCamera
	// カメラ情報更新
	//************************************************************
	public void updateCamera() {
		// カメラはプレイヤーに追随
		GameMain.camera.position.set(PlayerManager.GetPlayer(0).body.getPosition().x,
							PlayerManager.GetPlayer(0).body.getPosition().y, 0);

		// カメラの移動制限
		if (GameMain.camera.position.x <
				-(BackgroundManager.GetBackground(stageNum).sprite.get(1).getWidth() * 0.5
																		- ScrollNinja.window.x * 0.5f) * 0.1f)
			GameMain.camera.position.x =
				-(BackgroundManager.GetBackground(stageNum).sprite.get(1).getWidth() * 0.5f
																		- ScrollNinja.window.x * 0.5f) *0.1f;
		if (GameMain.camera.position.x >
			(BackgroundManager.GetBackground(stageNum).sprite.get(1).getWidth() * 0.5
																		- ScrollNinja.window.x * 0.5f) * 0.1f)
			GameMain.camera.position.x =
				(BackgroundManager.GetBackground(stageNum).sprite.get(1).getWidth() * 0.5f
																		- ScrollNinja.window.x * 0.5f) * 0.1f;

		// 1333は実際の画像のサイズ
		if (GameMain.camera.position.y < -(1333 - ScrollNinja.window.y) * 0.5f * 0.1f)
			GameMain.camera.position.y = -(1333 - ScrollNinja.window.y) * 0.5f * 0.1f;
		if (GameMain.camera.position.y > (1333 - ScrollNinja.window.y) * 0.5f * 0.1f)
			GameMain.camera.position.y = (1333 - ScrollNinja.window.y) * 0.5f * 0.1f;

		GameMain.camera.update();
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

	public Player spawnPlayer(Player player) {
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
		PlayerManager.CreatePlayer( 0.0f, 0.0f );
		StageObjectManager.CreateStageObject(StageObject.ROCK, 0.0f, 0.0f);
		EnemyManager.CreateEnemy(Enemy.NORMAL, 20.0f, 30.0f);
	}

	@Override
	public void Release() {
	}

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}