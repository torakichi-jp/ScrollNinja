package org.genshin.scrollninja;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// 制作メモ
// 10/5 制作開始
// 10/6 とりあえず表示まで。シングルトンを全部モノステートに。
// 10/8 コメント追加

public class GameMain implements Screen{
	private Game					ScrollNinjya;
	private OrthographicCamera		camera;				// カメラ
	private SpriteBatch				spriteBatch;		// スプライトバッチ
	private World					world;				// ワールドマトリクス
	private Box2DDebugRenderer		renderer;			//
	private Player					player;				// プレイヤー
	private Stage						stage;
	//private Background				background;

	// コンストラクタ
	public GameMain(Game game) {
		ScrollNinjya		= game;
		player				= new Player();
		camera				= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch 		= new SpriteBatch();
		world				= new World(new Vector2(0, -100.0f), true);
		renderer			= new Box2DDebugRenderer();
		stage				= new Stage("a");


		CreateStage();
		CreatePlayer();
		CreateStageObject();
		EnemyManager.CreateEnemy("1", 0, 0.0f, 400.0f);
	}

	//************************************************************
	// Update
	// 更新処理
	//************************************************************
	public void Update() {
		player.GetSprite().setPosition(player.GetPosition().x - 32, player.GetPosition().y - 32);
		player.GetSprite().setRotation((float) (player.GetBody().getAngle()*180/Math.PI));
		EnemyManager.Update();

		// 背景スクロール
		//stage.moveBackground(player);
		//camera.position.set(stage.GetCamPos().x , stage.GetCamPos().y,0);
		Background.moveBackground(player);
		camera.position.set(Background.GetCamPos().x , Background.GetCamPos().y , 0);
		
		camera.update();
		player.Update(world);
	}

	//************************************************************
	// Draw
	// 描画処理
	//************************************************************
	public void Draw() {
		// 全部クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatch.setProjectionMatrix(camera.combined);		// プロジェクション行列のセット
		spriteBatch.begin();									// 描画開始
		{
			Background.GetSprite()[0].draw(spriteBatch);
			Background.GetSprite()[2].draw(spriteBatch);
//			StageObjectManager.GetStageObject("block").GetSprite().draw(spriteBatch);
			player.Draw(spriteBatch);
			EnemyManager.GetEnemy("1").GetSprite().draw(spriteBatch);
		}
		spriteBatch.end();										// 描画終了

		renderer.render(world, camera.combined);
		world.step(Gdx.graphics.getDeltaTime(), 20, 20);
		player.GetBody().setAwake(true);
	}

	//************************************************************
	// CreateStage
	// ステージのあたり判定の作成
	//************************************************************
	private void CreateStage() {
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// ボディタイプ設定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体
		bd.position.set(-(Gdx.graphics.getWidth() / 2), -1024);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;		// 密度
		fd.friction		= 100.0f;	// 摩擦
		fd.restitution	= 0;		// 反発係数

		// ボディ作成
		Background.SetBody(world.createBody(bd));
		loader.attachFixture( Background.GetBody(), "bgTest", fd, 2048);
	}

	//************************************************************
	// CreateCharacter
	// プレイヤーの作成
	//************************************************************
	private void CreatePlayer() {
		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;		// 動く物体
		player.SetBody(world.createBody(def));

		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(16, 24);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 50;
		fd.friction		= 100.0f;
		fd.restitution	= 0;
		fd.shape		= poly;

		//
		player.GetBody().createFixture(fd);
		player.SetFixture(player.GetBody().createFixture(poly, 0));
		poly.dispose();
		player.GetBody().setBullet(true);			// すり抜け防止
		player.GetBody().setTransform(0, 300, 0);	// 初期位置
	}

	//************************************************************
	// CreateStageObject
	// ステージオブジェクトの作成
	//************************************************************
	private void CreateStageObject() {
		// 当たり判定読み込み
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/stageObject.json"));

		// Bodyのタイプを設定 Staticは動かない物体
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(0, 0);

		// Bodyの設定を設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;				// 密度
		fd.friction		= 100f;				// 摩擦
		fd.restitution	= 0;				// 反発係数

		// ステージオブジェクトの作成
		StageObjectManager.CreateStageObject("block");
		StageObjectManager.GetStageObject("block").SetBody(world.createBody(bd));

		// 各種設定を適用。引数は　Body、JSON中身のどのデータを使うか、FixtureDef、サイズ
		loader.attachFixture(StageObjectManager.GetStageObject("block").GetBody(), "gravestone", fd, 256);
	}

	@Override
	public void render(float delta) {
		// TODO 自動生成されたメソッド・スタブ
		long error = 0;
		int fps = 60;
		long idealSleep = (1000 << 16) / fps;
		long oldTime;
		long newTime = System.currentTimeMillis() << 16;

		oldTime = newTime;
		Update();
		Draw();

		newTime = System.currentTimeMillis() << 16;
		long sleepTime = idealSleep - (newTime - oldTime) - error; // 休止できる時間
		if (sleepTime < 0x20000) sleepTime = 0x20000; // 最低でも2msは休止
		oldTime = newTime;
		try {
			Thread.sleep(sleepTime >> 16);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} // 休止
		newTime = System.currentTimeMillis() << 16;
		error = newTime - oldTime - sleepTime; // 休止時間の誤差

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
