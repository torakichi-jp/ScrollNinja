package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

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
