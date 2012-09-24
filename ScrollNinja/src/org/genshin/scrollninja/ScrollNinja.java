package org.genshin.scrollninja;

import java.util.List;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class ScrollNinja extends Game {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	// スプライト
	private Sprite stageSpr;
	private Sprite bgSpr;
	private Sprite charaSpr;
	//private Sprite sprObj;

	// シミュレーションの場所
	private World world;
	private Box2DDebugRenderer renderer;
	// シミュレーションされる物体
	private Body charaBody;
	//private Body objBody;
	private Body groundBody;
	// 物体設定用？
	//private Fixture playerPhysicsFixture;
	private Fixture playerSensorFixture;

	// 回転用。置きっぱなし
	//float rotation;
	// 移動用
	private Vector2 move = new Vector2();
	private Vector2 charaPos = new Vector2();
	private Vector2 groundPos = new Vector2();
	// 画面の端にいるかいないか
	private Boolean edge = true;

	// キャラクターが地面にいるかいないか。最初は落ちてくるのでfalse
	private Boolean isGround = false;
	// ジャンプ処理用
	private int jump = 2;  // 0:地面　1:ジャンプ中　2:落下中
	private int jumpHeight = 0;

	@Override
	public void create() {
		//rotation = 0;

		// ウインドウサイズ取得
		//float w = Gdx.graphics.getWidth();
		//float h = Gdx.graphics.getHeight();

		// カメラをウインドウサイズで作成
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// スプライトバッチ作成
		batch = new SpriteBatch();

		// シミュレーション世界の作成
		// 第二引数を真にすると、物理エンジン内で活動的に動いていない物体に対して衝突検知などの
		// 演算を行わないように設定できる、というものらしい。
		/** 重力がきちんと反映されてない？　要検証… **/
		world = new World(new Vector2(0, -100f), true);
		renderer = new Box2DDebugRenderer();
		// フィールド作成
		createWorld();

		// 背景(手前)テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/stage_near_test.png"));
		// コメントアウトしても動く。効果がいまいちわからない…
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// テクスチャ範囲
		TextureRegion region = new TextureRegion(texture, 0, 0, 2048, 2048);
		// 背景スプライトにセット
		stageSpr = new Sprite(region);
		// 中心
		stageSpr.setOrigin(stageSpr.getWidth() / 2, stageSpr.getHeight() / 2);
		// 0,0 だと画面の中央に背景画像の左下が設置されるため調整
		// 画面下の方が空白なので高さ位置はどう出したものかと…
		stageSpr.setPosition(-(Gdx.graphics.getWidth() / 2), -1024);

		// 背景（奥）テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/stage_far_test.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		region = new TextureRegion(texture, 0, 0, 1024, 1024);
		// 背景スプライトにセット
		bgSpr = new Sprite(region);
		bgSpr.setOrigin(bgSpr.getWidth() / 2, bgSpr.getHeight() / 2);
		bgSpr.setPosition(-(Gdx.graphics.getWidth() / 2), -660);

		// キャラクターテクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/chara.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		region = new TextureRegion(texture, 0, 0, 64, 64);
		// キャラクタースプライトにセット
		charaSpr = new Sprite(region);
		charaSpr.setOrigin(charaSpr.getWidth() / 2, charaSpr.getHeight() / 2);
		// キャラクター作成
		createChara();

		/*
		// オブジェクトテクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/chara.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		region = new TextureRegion(texture, 0, 0, 64, 64);
		// オブジェクトスプライトにセット
		sprObj = new Sprite(region);
		sprObj.setOrigin(sprObj.getWidth() / 2, sprObj.getHeight() / 2);
		// とりあえず真ん中に
		sprObj.setPosition(0, 0);
		// オブジェクト作成
		createObj();
		*/

		// アニメーション
		Texture dash = new Texture(Gdx.files.internal("data/dash_test.png"));
	}

	// フィールド（床）作成
	private void createWorld() {
		 // BodyEditorで作成したものを読み込む
		BodyEditorLoader loader =
				new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// Bodyのタイプを設定 Staticは動かない物体
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(-(Gdx.graphics.getWidth() / 2), -1024);

		// Bodyの設定を設定
		FixtureDef fd = new FixtureDef();
		fd.density = 1000;				// 密度
		fd.friction = 100f;				// 摩擦
		fd.restitution = 0;				// 反発係数

		// Bodyを作成
		groundBody = world.createBody(bd);

		// 各種設定を適用。引数は　Body、JSON中身のどのデータを使うか、FixtureDef、サイズ
		loader.attachFixture(groundBody, "bgTest", fd, 2048);

		/*
		// ボディタイプの設定　Staticは動かない物体
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		// 物体作成
		Body ground = world.createBody(def);

		// 物体の形を作成
		PolygonShape poly = new PolygonShape();
		// 物体のサイズ
		poly.setAsBox(400, 100);
		// 物体に設定？
		playerPhysicsFixture = ground.createFixture(poly, 1);
		// 解放しておく
		poly.dispose();

		groundBody = ground;
		// とりあえずの位置
		ground.setTransform(0, -200, 0);
		*/
	}

	// キャラクター作成
	private void createChara() {
		/*
		// BodyEditorで作成したものを読み込む
		BodyEditorLoader loader =
				new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// Bodyのタイプを設定
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		bd.type = BodyType.DynamicBody;

		// Bodyの設定を設定
		FixtureDef fd = new FixtureDef();
		fd.density = 50;				// 密度
		fd.friction = 100f;				// 摩擦
		fd.restitution = 0;				// 反発係数

		// Bodyを作成
		charaBody = world.createBody(bd);
		//charaBody.resetMassData();
		//charaBody.setLinearVelocity(new Vector2(0, 10));

		// 各種設定を適用。引数は　Body、JSON中身のどのデータを使うか、FixtureDef、サイズ
		loader.attachFixture(charaBody, "charaTest", fd, 64);
		// スプライト追随用にOriginを保存しておく
		charaOrigin = loader.getOrigin("charaTest", 64).cpy();
		*/

		// ボディタイプの設定　Dynamicは動く（動かされる）物体
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		charaBody = world.createBody(def);

		// 物体の形を作成
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(16, 24);
		// Bodyの設定を設定
		FixtureDef fd = new FixtureDef();
		fd.density = 50;				// 密度
		fd.friction = 100f;				// 摩擦
		fd.restitution = 0;				// 反発係数
		fd.shape = poly;
		// 設定を入れる
		charaBody.createFixture(fd);

		playerSensorFixture = charaBody.createFixture(poly, 0);
		poly.dispose();

		// これを設定すればContinuous Collision Detection(CCD)を行うので高速に動いても
		// すり抜けないけど計算時間増える、らしい。
		charaBody.setBullet(true);
		// 最初の出現で埋まることがあるので上の方にセットしておく
		charaBody.setTransform(0, 300, 0);
	}

	/*
	// オブジェクト作成
	private void createObj() {
		// Bodyのタイプを設定
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		bd.type = BodyType.StaticBody;

		// Bodyの設定を設定
		FixtureDef fd = new FixtureDef();
		fd.density = 50;					// 密度
		fd.friction = 100f;				// 摩擦
		fd.restitution = 0;				// 反発係数

		// Bodyを作成
		objBody = world.createBody(bd);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(32, 32);
		playerPhysicsFixture = objBody.createFixture(poly, 1);
		poly.dispose();
	}
	*/

	@Override
	public void dispose() {
		// 解放
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {
		// 回転するよ
		//tesSpr.setRotation(rotation);
		//rotation++;

		// 指定色で背景塗り潰し？
		//Gdx.gl.glClearColor(1, 1, 1, 1);
		// 背景塗り潰し？　バッファのクリア？
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// カメラ描画？
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// スプライト描画？
		// 奥に表示されるものから先に描画
		// シミュレーション世界より後にやらないとポリゴンの線が見えてしまうので注意
		bgSpr.draw(batch);
		stageSpr.draw(batch);
		//sprObj.draw(batch);
		charaSpr.draw(batch);
		batch.end();

		// シミュレーション世界を作成するよ？
		renderer.render(world, camera.combined);
		// 数字を大きくするとめりこみにくくなる、みたい？
		world.step(Gdx.graphics.getDeltaTime(), 20, 20);
		//SetAwakeしないと全てのオブジェクトが静止した場合に動かない、らしい
		charaBody.setAwake(true);
		//objBody.setAwake(false);
		groundBody.setAwake(true);

		// 現在位置
		charaPos = charaBody.getPosition();
		groundPos = groundBody.getPosition();

		// キャラクターの画像（スプライト）をcharaBodyの位置に描画する
		charaSpr.setPosition(charaPos.x - 32, charaPos.y - 32);
		//charaSpr.setOrigin(charaOrigin.x, charaOrigin.y);
		//charaSpr.setRotation(charaBody.getAngle() * MathUtils.radiansToDegrees);
		// 回転しないように固定
		charaBody.setTransform(charaPos, 0);

		// キャラクターに合わせて背景スクロール
		stageSpr.setPosition(groundPos.x, groundPos.y);
		// キャラクターの位置は端に居る時以外は真ん中
		move.x = 0;
		move.y = charaPos.y;
		if (!edge)
			charaBody.setTransform(move, 0);

		// キー入力
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			if (edge) {
				charaPos = charaBody.getPosition();
				charaPos.x -= 5;
				charaBody.setTransform(charaPos, 0);
			} else {
				groundPos = groundBody.getPosition();
				groundPos.x += 5;
				groundBody.setTransform(groundPos, 0);
				bgSpr.setPosition(bgSpr.getX() + 0.5f, bgSpr.getY());
			}
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if (edge) {
				charaPos = charaBody.getPosition();
				charaPos.x += 5;
				charaBody.setTransform(charaPos, 0);
			} else {
				groundPos = groundBody.getPosition();
				groundPos.x -= 5;
				groundBody.setTransform(groundPos, 0);
				bgSpr.setPosition(bgSpr.getX() - 0.5f, bgSpr.getY());
			}

		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (jump == 0) {
				jumpHeight = 0;
				jump = 1;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
		}

		// 現在位置再習得
		charaPos = charaBody.getPosition();
		groundPos = groundBody.getPosition();

		// 端にいるかどうか
		if (groundPos.x < -1647 || groundPos.x > -401)
			edge = true;
		else
			edge = false;

		// 端にいる時、キャラクターが画面半分以上反対側へ移動したらまた背景スクロール開始
		if ((groundPos.x < -1647 && charaPos.x < 0) ||
										(groundPos.x > -401 && charaPos.x > 0))
			edge = false;

		// ジャンプ中
		if (jump == 1) {
			// 上昇中
			charaPos = charaBody.getPosition();
			charaPos.y += 15 ;
			jumpHeight += 15;
			charaBody.setTransform(charaPos, 0);
			// 落下
			if (jumpHeight > 300)
				jump = 2;
		}

		// 重力が思ったようにきかないのでとりあえず…
		isGround = isPlayerGrounded();
		if (!isGround && jump == 2) {
			charaPos = charaBody.getPosition();
			charaPos.y -= 5;
			charaBody.setTransform(charaPos, 0);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	// プレイヤーが地面にいるかいないか
	private boolean isPlayerGrounded() {
		// リスト取得
		List<Contact> contactList = world.getContactList();
		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);
			// プレイヤーと接触しているかいないか
			if(contact.isTouching() && (contact.getFixtureA() == playerSensorFixture ||
				contact.getFixtureB() == playerSensorFixture)) {
				// 地面に接触したらジャンプはリセット
				if (jump != 0)
					jump = 0;
				return true;
			}
		}
		// ジャンプ（上昇）中でない、地面に接触していないのなら落下中
		if (jump != 1)
			jump = 2;
		return false;
	}
}
