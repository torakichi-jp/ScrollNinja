package org.genshin.scrollninja;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class StageEditor implements Screen {
	// 定数
	private static final int MAX_STAGE			= 1;
	private static final int MAX_PLAYER			= 1;			// 全部種類です
	private static final int MAX_ENEMY			= 1;
	private static final int MAX_STAGEOBJECT	= 1;

	// 変数
	private static int 			stageNum;
	private static int			priority;
	private static int			count[] = new int[10];			// トリガー取得の為のカウント用変数
	private static Vector2		position = new Vector2( -64.0f, -32.0f);
	private static Box2DDebugRenderer	renderer = new Box2DDebugRenderer();

	/**
	 * 初期化
	 * とりあえず全部生成
	 */
	public static void Init() {
		for( int i = 0; i < EnemyManager.enemyList.size(); i ++ ) {
			EnemyManager.DeleteEnemy(i);
		}
		for( int i = 0; i < MAX_STAGE; i ++) {
//			BackgroundManager.CreateBackground(i + 1, false);
		}
		for( int i = 0; i < MAX_PLAYER; i ++ ) {
//			PlayerManager.CreatePlayer(new Vector2(0.0f, 0.0f));
		}
		for( int i = 0; i < MAX_ENEMY; i ++ ) {
//			EnemyManager.CreateEnemy(i, new Vector2(i * 30.0f, 0.0f));
		}
		for( int i = 0; i < MAX_STAGEOBJECT; i ++ ) {
//			StageObjectManager.CreateStageObject(i, new Vector2(0.0f, 0.0f));
		}

		priority = 9;
		
		for(int i = 0; i < 10; i++ ) {
			count[i] = 0;
		}
	}

	/**
	 * 更新
	 */
	private void Update() {
//		ChangeStage();
		Move();
		Mouse.Update();
		Priority();

		// キャラクター
		StructObjectManager.Update(priority);

		//TODO		トリガー実装はよ
		if( Gdx.input.isKeyPressed(Keys.B)) {
			count[0] ++;
		}
		else {
			count[0] = 0;
		}

		if( Gdx.input.isKeyPressed(Keys.K)) {
			count[1] ++;
		}
		else {
			count[1] = 0;
		}
		
		if( Gdx.input.isKeyPressed(Keys.L)) {
			count[2] ++;
		}
		else {
			count[2] = 0;
		}
		
		if( Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
			count[3] ++;
		}
		else {
			count[3] = 0;
		}
		
		if( count[0] == 1 ) {
			StructObjectManager.CreateStructObject(StructObject.ROCK_OBJECT);
		}
		if( count[1] == 1 ) {
			FileOperation.Save();
		}
		if( count[2] == 1 ) {
			FileOperation.Load();
		}
		if( count[3] == 1 ) {
			for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
				if( StructObjectManager.GetStructObject(i).GetHold() ) {
					StructObjectManager.DeleteStructObject(i);
				}
			}
		}
		
		System.out.println(count[0]);

//		System.out.println("マウスX:" + (Mouse.GetPosition().x * 0.1 - 64.0 ));
//		System.out.println("マウスY:" + (Mouse.GetPosition().y * 0.1 - 36.0 ));
//		System.out.println("て　き :" + EnemyManager.enemyList.get(0).GetPosition());
//		System.out.println("カメラX:" + GameMain.camera.position.x);
//		System.out.println("カメラY:" + GameMain.camera.position.y);
	}

	/**
	 * 描画
	 */
	private void Draw() {
		// 全部クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		GameMain.spriteBatch.setProjectionMatrix(GameMain.camera.combined);		// プロジェクション行列のセット
		GameMain.spriteBatch.begin();									// 描画開始
		{
			BackgroundManager.backgroundList.Draw(Background.FAR);
			BackgroundManager.backgroundList.Draw(Background.MAIN);

			for( int j = 0; j <= priority; j ++ ) {
				for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
					if( StructObjectManager.GetStructObject(i).GetPriority() == j ) {
						StructObjectManager.GetStructObject(i).Draw();
					}
				}
			}


		}
		GameMain.spriteBatch.end();										// 描画終了
		renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);
	}

	/**
	 * マウス処理
	 */
/*	private void Mouse() {
		if(Mouse.LeftClick()) {
			for( int i = 0; i < EnemyManager.normalEnemyList.size(); i++ ) {
				if( EnemyManager.enemyList.get(i).GetPosition().x - 1.6 < (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) &&
					EnemyManager.enemyList.get(i).GetPosition().x + 1.6 > (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) &&
					EnemyManager.enemyList.get(i).GetPosition().y - 2.4 < (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 ) &&
					EnemyManager.enemyList.get(i).GetPosition().y + 2.4 > (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 )) {
					EnemyManager.enemyList.get(i).SetPosition((float)((Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x)),(float)((GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 )));
				}
			}
		}
	}*/

	/**
	 * カメラ移動
	 */
	private void Move() {
		GameMain.camera.position.set(position.x, position.y, 0);

		if (GameMain.camera.position.x < -(BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale) {
			GameMain.camera.position.x = -(BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.A) ) {
			position.x --;
		}
		if (GameMain.camera.position.x > (BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale) {
			GameMain.camera.position.x = (BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.D) ) {
			position.x ++;
		}

		if (GameMain.camera.position.y < -(1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale) {
			GameMain.camera.position.y = -(1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.S) ) {
			position.y --;
		}
		if (GameMain.camera.position.y > (1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale) {
			GameMain.camera.position.y = (1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.W) ) {
			position.y ++;
		}

		GameMain.camera.update();
	}

	/**
	 * ステージ遷移
	 */
	private void ChangeStage() {
		if( Gdx.input.isKeyPressed(Keys.NUM_1)) {
			stageNum = 1;
		}

		if( Gdx.input.isKeyPressed(Keys.NUM_2)) {
			stageNum = 2;
		}

		if( Gdx.input.isKeyPressed(Keys.NUM_3)) {
			stageNum = 3;
		}
	}

	/**
	 * 優先度の変更
	 */
	private void Priority() {
		boolean hold = false;

		for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
			hold = StructObjectManager.GetStructObject(i).GetHold();
			if( hold ) {
				return;
			}
		}

		if( !hold ) {
			if( Gdx.input.isKeyPressed(Keys.NUM_0)) {
				priority = 0;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_1)) {
				priority = 1;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_2)) {
				priority = 2;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_3)) {
				priority = 3;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_4)) {
				priority = 4;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_5)) {
				priority = 5;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_6)) {
				priority = 6;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_7)) {
				priority = 7;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_8)) {
				priority = 8;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_9)) {
				priority = 9;
			}
		}
	}

	@Override
	public void render(float delta) {
		Update();
		Draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
