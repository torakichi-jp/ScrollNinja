package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Character {
	private String name;
	private int hp;
	private int mp;
	private int lv;
	private int exp;
	private ArtsFormProto form;
	private WeaponProto primaryWeapon;
	private WeaponProto secondaryWeapon;
	private ArrayList<WeaponProto> weapons;
	private ArrayList<ItemProto> items;
	private ArrayList<ArtsFormProto> knownForms;
	private Vector2 position;
	private Boolean jump;

	private Body body;
	private Fixture sensor;

	// 定数
	private final static int DEFAULT_HP = 100;
	private final static int DEFAULT_MP = 10;

	// コンストラクタ
	public Character() {
		this.name = "";
		this.hp = DEFAULT_HP;
		this.mp = DEFAULT_MP;
		this.lv = 1;
		this.exp = 0;
		this.form = new ArtsFormProto();
		this.primaryWeapon = new WeaponProto();
		this.secondaryWeapon = this.primaryWeapon;
		this.weapons = new ArrayList<WeaponProto>();
		this.items = new ArrayList<ItemProto>();
		this.knownForms = new ArrayList<ArtsFormProto>();
		this.position = new Vector2(0, 0);
		this.jump = false;
	}

	// 当たり判定作成
	public void createBox(World world) {
		// Bodyタイプ
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		body = world.createBody(def);

		// 物体の形
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(16, 24);
		// Bodyの設定
		FixtureDef fd = new FixtureDef();
		fd.density = 50;				// 密度
		fd.friction = 100f;				// 摩擦
		fd.restitution = 0;				// 反発係数
		fd.shape = poly;

		// 設定を入れる
		body.createFixture(fd);
		// 当たり判定用
		sensor = body.createFixture(poly, 0);
		poly.dispose();

		body.setBullet(true);

		// 初期位置
		body.setTransform(0, 300, 0);
	}

	// キャラクター移動
	public void move() {
	}

	// 攻撃
	public void attack() {
	}

	// 武器変更
	public void changeWeapon() {
	}

	// アイテム使用
	public void useItem() {
	}

	// アニメーション
	public void animation() {
	}
}
