package org.genshin.scrollninja.object.character.ninja;

import java.util.logging.Logger;

import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.Background;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.controller.DefaultPlayerNinjaController;
import org.genshin.scrollninja.object.character.ninja.controller.NinjaControllerInterface;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

// 制作メモ
// 10/2 	制作開始
// 10/3 	変数と空の関数を実装
// 			ジャンプと移動だけ先に明日実装！
// 10/4 	ジャンプと移動は実装完了だけど実行してない
//			明日アニメーション関連進行。今週までに表示までいきたい
// 10/8		移動だけ動作確認。段差のところで空中移行になってるのを直そう
//			重力弱いから要調整。ジャンプできてねー＾ｑ＾
// 10/19 	・エフェクトとのアニメーションを一致させました
//			・上半身を下半身の前に描画するようにしました
//			・攻撃後すぐ戻ると不自然だったので上半身がそのまましばらく残るようにしました
//			・落下速度の調整

// *メモ*
// 攻撃はダッシュしながら攻撃可能（足は止まらない）
// 右クリック押しっぱなしで伸び続ける
// 壁とかに付いた後も押しっぱでそっちに移動
// 壁とかに付いた状態で離すとブラーン
// もう一回右クリックで離す

//TODO	currentState = ◯◯のコメントアウト
//		↑の部分は現在立ちアニメーションがないのでコメントアウトしてモーション遷移確認のため歩きで代用しています
// 		コメントアウト部分消すと完成した時どこ直せばいいのか探すのめんどいので消さないようにお願いします

// FIXME そのうちがっつりリファクタリングしてやるから待ってろ！

/**
 * プレイヤーが操作する忍者
 * @author	インターン生
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class PlayerNinja extends AbstractCharacter {
	/**
	 * コンストラクタ
	 * @param world		所属するWorldオブジェクト
	 * @param position	初期座標
	 * @param cursor	カーソルオブジェクト（鉤縄の向き計算用）
	 */
	public PlayerNinja(World world, Vector2 position, Cursor cursor) {
		super(world, position);
		
		// 初期座標設定
		getBody().setTransform(position, 0.0f);
		
		// フィールドの初期化
		state = new NinjaStateOnGround(this);
		controller = new DefaultPlayerNinjaController(this, cursor);
		kaginawa = new Kaginawa(world, getBody());
		restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
	}

	/**
	 * 更新処理
	 */
	public void update()
	{
		// 操作状態を更新
		controller.update();

		// 状態に合わせた更新処理
		state = state.update();
		
		// 鉤縄を更新
		kaginawa.update();
		
		// 地面との接触フラグは落としておく
		grounded = false;
		
		// 前方を表すベクトルもデフォルトにしておく
		frontDirection.set(Vector2.X);
	}
	
	@Override
	public void render()
	{
		//---- 描画時の左右反転フラグを設定する。
		final Vector2 direction = controller.getDirection();
		final float upAngle = getBody().getAngle();
		final Vector2 upDirection = new Vector2(-(float)Math.sin(upAngle), (float)Math.cos(upAngle));
		flip(upDirection.crs(direction) < 0.0f, false);
		
		//---- 鉤縄を描画する。
		kaginawa.render();

		//---- 自身を描画する。
		super.render();
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}
	
	@Override
	public void notifyCollision(Background obj, Contact contact) {
		// 衝突したのが足でなければそのままでOK
		Fixture footFixture = getFixture(FOOT);
		if(contact.getFixtureA()!=footFixture && contact.getFixtureB()!=footFixture)
			return;

		// 足が地面に着いたらフラグを立てる
		grounded = true;
		restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
		Vector2 normal = contact.getWorldManifold().getNormal();
		frontDirection.set(normal.y, -normal.x);

		// 壁走り
		nearRotate( (float)Math.toRadians(contact.getWorldManifold().getNormal().angle()-90) );
	}
	
	@Override
	protected void initializeSprite()
	{
		RenderObjectInterface bodyRenderObject = RenderObjectFactory.getInstance().get("NinjaBody");
		RenderObjectInterface footRenderObject = RenderObjectFactory.getInstance().get("NinjaFoot");

		final String animName = "Stay";
		bodyRenderObject.setAnimation(animName);
		footRenderObject.setAnimation(animName);

		addRenderObject(footRenderObject);
		addRenderObject(bodyRenderObject);
	}

	@Override
	protected BodyDef createBodyDef()
	{
		BodyDef bd = super.createBodyDef();
		bd.fixedRotation = true;		// 回転しない
		return bd;
	}

	@Override
	protected void initializeFixture()
	{
		// 下半身
		FixtureDef ffd = NinjaParam.INSTANCE.FOOT_FIXTURE_DEF_LOADER.createFixtureDef();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(1.5f);
		ffd.shape = circleShape;
		
		createFixture(ffd);
		circleShape.dispose();
		
		// 上半身
		FixtureDef bfd = NinjaParam.INSTANCE.BODY_FIXTURE_DEF_LOADER.createFixtureDef();
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(1.6f, 1.6f, new Vector2(0.0f, 1.6f), 0.0f);
		bfd.shape = polygonShape;

		createFixture(bfd);
		polygonShape.dispose();
	}
	
	void move()
	{
		// TODO 壁走り、天井走りを継続させるため、地面に足が着いている時は地面への吸着力を発生させる？
		Body body = getBody();
		
		// 移動処理
		// FIXME 斜面でずり落ちないようにする。摩擦いじる？
		float moveLevel = controller.getMoveLevel();

		if(moveLevel == 0.0f)
		{
			// TODO 摩擦的な処理。box2dのfrictionに任せるべき？
			if( IsGrounded() )
			{
				Vector2 velocity = body.getLinearVelocity();
				velocity.mul(0.8f);
				if(velocity.len2() < (0.5f*0.5f))
				{
					velocity.set(Vector2.Zero);
				}
				body.setLinearVelocity(velocity);
			}
			setAnimation("Stay");
		}
		else
		{
			if(controller.isDash())
			{
				dash(moveLevel);
				setAnimation("Dash");
			}
			else
			{
				run(moveLevel);
				setAnimation("Run");
			}
		}

		// 速度制限
		// FIXME 現状だと水平方向にしか制限が働かないため、なんとかする。
		final float maxVelocity = controller.isDash() ? NinjaParam.INSTANCE.DASH_MAX_VELOCITY : NinjaParam.INSTANCE.RUN_MAX_VELOCITY; 
		Vector2 vel = body.getLinearVelocity();
		if( Math.abs(vel.x) > maxVelocity ) {
			body.setLinearVelocity(Math.signum(vel.x)*maxVelocity, vel.y);
		}
	}
	
	void jump()
	{
		boolean isJump = false;
		
		if( IsGrounded() )
		{
			isJump = controller.isJump();
		}
		else 
		{
			if( controller.isAerialJump() && restAerialJumpCount > 0 )
			{
				isJump = true;
				restAerialJumpCount--;
			}
		}
		
		if( isJump )
		{
			Body body = getBody();
			body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
			body.applyLinearImpulse(0.0f, NinjaParam.INSTANCE.JUMP_POWER, position.x, position.y);
		}
	}
	
	void kaginawa()
	{
		if( controller.isKaginawaSlack() )
		{
			kaginawa.slack(controller.getDirection());
		}
		if( controller.isKaginawaShrink() )
		{
			kaginawa.shrink();
		}
		if( controller.isKaginawaHang() )
		{
			kaginawa.hang();
		}
		if( controller.isKaginawaRelease() )
		{
			kaginawa.release();
		}
	}

	/**
	 * 180度以下の範囲で回転する。
	 * @param radian		回転後の角度
	 */
	void nearRotate(float radian)
	{
		Body body = getBody();
		float stateTime = 10;		// 引数から指定する仕様に変更するかも知れない

		radian -= body.getAngle();
		radian = (float)( Math.abs(radian) % (Math.PI*2.0f) * Math.signum(radian) );
		if(Math.abs(radian) > Math.PI)
			radian = (float)( radian - Math.signum(radian) * Math.PI );

		body.setAngularVelocity(radian*stateTime);
	}

	/**
	 * 上半身のアニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	void setBodyAnimation(String name)
	{
		getBodyRenderObject().setAnimation(name);
	}

	/**
	 * 下半身のアニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	void setFootAnimation(String name)
	{
		getFootRenderObject().setAnimation(name);
	}
	
	/**
	 * アニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	void setAnimation(String name)
	{
		setBodyAnimation(name);
		setFootAnimation(name);
	}
	
	/**
	 * 上半身の描画オブジェクトを取得する。
	 * @return		上半身の描画オブジェクト
	 */
	RenderObjectInterface getBodyRenderObject()
	{
		return getRenderObject(1);
	}
	
	/**
	 * 下半身の描画オブジェクトを取得する。
	 * @return		下半身の描画オブジェクト
	 */
	RenderObjectInterface getFootRenderObject()
	{
		return getRenderObject(0);
	}
	
	/**
	 * 忍者の操作を管理するオブジェクトを取得する。
	 * @return		忍者の操作を管理するオブジェクト
	 */
	NinjaControllerInterface getNinjaController()
	{
		return controller;
	}
	
	/**
	 * 地面との接触フラグを取得する。
	 * @return		地面と接触している場合はtrue
	 */
	boolean IsGrounded()
	{
		return grounded;
	}
	
	private void run(float moveLevel)
	{
		moveMethod(moveLevel, NinjaParam.INSTANCE.RUN_ACCEL, NinjaParam.INSTANCE.RUN_MAX_VELOCITY);
	}
	
	private void dash(float moveLevel)
	{
		moveMethod(moveLevel, NinjaParam.INSTANCE.DASH_ACCEL, NinjaParam.INSTANCE.DASH_MAX_VELOCITY);
	}
	
	private void moveMethod(float moveLevel, float accel, float maxVelocity)
	{
		Body body = getBody();
		
		// 加速度を加える
		Vector2 position = body.getPosition();
		
		body.applyLinearImpulse(accel*moveLevel*frontDirection.x, accel*moveLevel*frontDirection.y, position.x, position.y);
	}
	
	/** 忍者の状態を管理するオブジェクト */
	private AbstractNinjaState		state;
	
	/** 忍者の操作を管理するオブジェクト */
	private NinjaControllerInterface	controller;
	
	/** 鉤縄オブジェクト */
	private Kaginawa	kaginawa;
	
	/** 空中でジャンプできる残り回数 */
	private int	restAerialJumpCount;
	
	/** 地面との接触フラグ。接触時にはtrueを代入する。 */
	private boolean grounded;
	
	/** 正面方向を表すベクトル */
	private final Vector2 frontDirection = new Vector2();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
// XXX ここから下は未確認。徐々に本実装に置き換える予定。


//	@Override
//	public void notifyCollision(Background obj, Contact contact) {
//		// 衝突したのが足でなければそのままでOK
//		Fixture footFixture = getFixture(FOOT);
//		if(contact.getFixtureA()!=footFixture && contact.getFixtureB()!=footFixture)
//			return;
//
//		// 足が地面に着いたらジャンプできるよ！
//		jump = false;
//		restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
//
//		if( currentState != ATTACK ) {
//			//currentState = STAND;
//			currentState = RUN;
//		}
//
//		groundJudge = true;
//
//		// 壁走り
//		nearRotate( (float)Math.toRadians(contact.getWorldManifold().getNormal().angle()-90) );
//	}
//
//	@Override
//	public void notifyCollision(Item obj, Contact contact){
//		switch(obj.GetType()) {
//		case Item.ONIGIRI:
//			hp += 50;
//			if( hp > MAX_HP ) {
//				hp = MAX_HP;
//			}
//			Interface.calculateHP = true;
//			break;
//		case Item.OKANE:
//			break;
//		}
//	}
//	
//	@Override
//	public void notifyCollision(AbstractWeapon obj, Contact contact){
//		if (obj.GetOwner() != this) {
//			if( invincibleTime == 0 ) {
//				invincibleTime = 120;		// 無敵時間付与
//				hp -= obj.GetAttackNum();
//				Interface.calculateHP = true;
//			}
//			if( hp <= 0 ) {
//				// TODO ゲームオーバー処理へ
//				hp = 100;
//			}
//		}
//	}
//	
//	@Override
//	public void notifyCollision(Effect obj, Contact contact){
//		if (obj.GetOwner() != this) {
//			if( invincibleTime == 0 ) {
//				invincibleTime = 120;		// 無敵時間付与
//				hp -= obj.GetAttackNum();
//				Interface.calculateHP = true;
//			}
//			if( hp <= 0 ) {
//				// TODO ゲームオーバー処理へ
//				hp = 100;
//			}
//		}
//	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public int GetDirection(){ return direction; }
	public int GetChakra(){ return chakra; }
	public int GetMaxChakra(){ return maxChakra; }
	public Sprite GetSprite(String type) {
		if (type.equals("BODY"))
			return sprites.get(BODY);
		else
			return sprites.get(FOOT);
	}
	public int GetMaxHP() { return MAX_HP;}
	public float GetHP() { return hp; }
	public AbstractWeapon GetWeapon(){ return weapon; }

	/**
	 *
	 */
	private void updateStand() {

		Vector2 velocity = getBody().getLinearVelocity();
		if( velocity.x == 0 ) {
			currentState = STAND;
		}
	}
	
	
	/**
	 * ジャンプ処理
	 * Ｗでジャンプ
	 */
	private void updateJump() {
		Body body = getBody();
		
		// 地面に接触しているならジャンプ可能
		if( groundJudge ) {
			// 上押したらジャンプ！
			if (controller.isJump()) {
				jump = true;
				currentState = JUMP;
				body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
				body.applyLinearImpulse(0.0f, NinjaParam.INSTANCE.JUMP_POWER, position.x, position.y);
			}
		}
		else
		{
			if(controller.isAerialJump() && restAerialJumpCount > 0)
			{
				restAerialJumpCount--;
				currentState = JUMP;
				body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
				body.applyLinearImpulse(0.0f, NinjaParam.INSTANCE.JUMP_POWER, position.x, position.y);
			}
			nearRotate(0);
		}
	}

	/**
	 * 移動処理
	 * 左右で移動
	 */
	private void updateMove()
	{
		if(controller.isDash())
			move(NinjaParam.INSTANCE.DASH_ACCEL, NinjaParam.INSTANCE.DASH_MAX_VELOCITY, DASH);
		else
			move(NinjaParam.INSTANCE.RUN_ACCEL, NinjaParam.INSTANCE.RUN_MAX_VELOCITY, RUN);
	}

	private void move(float accel, float maxVel, int state)
	{
		// TODO 壁走り、天井走りを継続させるため、地面に足が着いている時は地面への吸着力を発生させる？
		Body body = getBody();

		// 速度制限
		// FIXME 現状だと水平方向にしか制限が働かないため、なんとかする。
		Vector2 vel = body.getLinearVelocity();
		if( Math.abs(vel.x) > maxVel ) {
			body.setLinearVelocity(Math.signum(vel.x)*maxVel, vel.y);
		}

		// 移動処理
		// FIXME 斜面でずり落ちないようにする。摩擦いじる？
		float moveLevel = controller.getMoveLevel();

		if(moveLevel == 0.0f)
		{
			if( groundJudge ) {
				Vector2 velocity = body.getLinearVelocity();
				velocity.x *= 0.8;
				if( velocity.x < 0.5 && velocity.x > -0.5 ) {
					velocity.x = 0;
				}
				body.setLinearVelocity(velocity);
			}
		}
		else
		{
			direction = moveLevel>0.0f ? RIGHT : LEFT;
			float sin = (float) Math.sin(body.getAngle());
			float cos = (float) Math.cos(body.getAngle());
			body.applyLinearImpulse(accel*direction*cos, accel*direction*sin, position.x, position.y);

			if( groundJudge ) {
				currentState = state;
			}
		}
	}

	//************************************************************
	// Attack
	// 攻撃処理。左クリックで攻撃
	//************************************************************
	private void updateAttack() {
		if( weapon.GetUseFlag() ) {
			currentState = ATTACK;
		}
		else if( currentState != RUN/*STAND*/ ){
			currentState = JUMP;
		}

		if(controller.isAttack() && currentState != ATTACK ) {
			count = 30 + 18;
			currentState = ATTACK;
			weapon.SetUseFlag(true);
		}
	}
	
	/**
	 * 鉤縄の更新処理を実行する。
	 */
	private void updateKaginawa()
	{
		if( controller.isKaginawaSlack() )
		{
			kaginawa.slack(Vector2.Zero);
		}
		if( controller.isKaginawaShrink() )
		{
			kaginawa.shrink();
		}
		if( controller.isKaginawaHang() )
		{
			kaginawa.hang();
		}
		if( controller.isKaginawaRelease() )
		{
			kaginawa.release();
		}
		kaginawa.update();
	}

	//************************************************************
	// animation
	// 現在の状態を参照して画像を更新
	//************************************************************
	private void animation() {
//		currentBodyAnimation.update();
//		currentFootAnimation.update();
	}

	// 武器変更
	private void changeWeapon()
	{
	}

	/**
	 * 点滅処理
	 */
	public void flashing() {
		// 高速点滅
		if( invincibleTime != 0 ) {
			if( invincibleTime % 10 > 5 ) {
				for(int i = 0; i < sprites.size(); i ++) {
					sprites.get(i).setColor( 0, 0, 0, 0);
				}
			}
			else {
				for(int i = 0; i < sprites.size(); i ++) {
					sprites.get(i).setColor(1, 1, 1, 1);
				}
			}
		}
	}
	
	
	// 定数宣言
	private static final int FOOT	= 0;
	private static final int BODY	= 1;

	private static final int RIGHT			=  1;
	private static final int LEFT			= -1;
	private static final int STAND			=  0;
	private static final int RUN				=  1;
	private static final int DASH			=  2;
	private static final int JUMP			=  3;
	private static final int ATTACK			=  4;

	// 変数宣言
	private int				invincibleTime;				// 無敵時間
	private boolean			groundJudge;				// 地面と当たってますよフラグ
	private AbstractWeapon		weapon;						// 武器
	
	
	// なんだかよく分からない変数
	private int			number;				// プレイヤー番号
	private int			currentState;			// 現在の状態
	private int			count;					// カウント用変数

	// おそらく別のクラスに吐き出す変数
	private int			charge;				// チャージゲージ
	private int			maxChakra;			// チャクラ最大値
	private int			chakra;				// チャクラ
	private int			money;				// お金
	
	// おそらく使わなくなる変数
	private boolean				jump;					// ジャンプフラグ
}
