/**
 * 
 */
package org.genshin.scrollninja.object.player;

import com.badlogic.gdx.Gdx;

/**
 * プレイヤーコントローラーの基本クラス。<br>
 * プレイヤーと入力状態とを仲介する。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public abstract class BasePlayerController implements PlayerController
{
	/** 鉤縄の状態 */
	protected enum State
	{
		LEFT,
		RIGHT,
		DASH,
		JUMP,
		ATTACK,
		KAGINAWA,
	}
	
	/** 入力補助オブジェクト */
	private IInputHelper inputHelpers[];
	
	/** 鉤縄を離す入力の判定用 */
	private final float KAGINAWA_RELEASE_TIME = 0.1f;
	
	/** 鉤縄を離す入力の判定用タイマー */
	private float kaginawaReleaseTimer;
	
	/**
	 * コンストラクタ
	 */
	public BasePlayerController()
	{
		inputHelpers = new IInputHelper[State.values().length];
		initialize();
	}

	@Override
	public final void update()
	{
		/** 入力情報を更新する。 */
		for(int i = 0;  i < inputHelpers.length;  ++i)
		{
			assert inputHelpers[i]!=null : "プレイヤーコントローラは正しく初期化されていません。(ACTION=" + State.values()[i].toString() + ")";
			inputHelpers[i].update();
		}
		
		/** 鉤縄を離す入力の判定用タイマー */
		final float delta = 1.0f/60.0f;
		if( inputHelpers[State.KAGINAWA.ordinal()].trg() )
			kaginawaReleaseTimer = 0.0f;
		else
			kaginawaReleaseTimer += delta;
	}

	@Override
	public final float moveLevel()
	{
		float result = 0.0f;
		if(inputHelpers[State.LEFT.ordinal()].prs())	result -= 1.0f;
		if(inputHelpers[State.RIGHT.ordinal()].prs())	result += 1.0f;
		return result;
	}

	@Override
	public final boolean dash()
	{
		return inputHelpers[State.DASH.ordinal()].prs();
	}

	@Override
	public final boolean jump()
	{
		return inputHelpers[State.JUMP.ordinal()].prs();
	}

	@Override
	public final boolean aerialJump()
	{
		return inputHelpers[State.JUMP.ordinal()].trg();
	}

	@Override
	public final boolean attack()
	{
		return inputHelpers[State.ATTACK.ordinal()].prs();
	}

	@Override
	public final boolean kaginawaThrow()
	{
		return inputHelpers[State.KAGINAWA.ordinal()].trg();
	}
	
	@Override
	public final boolean kaginawaShrink()
	{
		return !inputHelpers[State.KAGINAWA.ordinal()].prs();
	}

	@Override
	public final boolean kaginawaHang()
	{
		// FIXME 鉤縄実験用の一時的な処理。
		return false;//inputHelpers[State.KAGINAWA.ordinal()].prs() && kaginawaReleaseTimer>=KAGINAWA_RELEASE_TIME;
	}

	@Override
	public final boolean kaginawaRelease()
	{
		return inputHelpers[State.KAGINAWA.ordinal()].rls() && kaginawaReleaseTimer<KAGINAWA_RELEASE_TIME;
	}

	/**
	 * インスタンスを初期化する。
	 */
	protected abstract void initialize();
	
	/**
	 * キーボード入力を登録する。
	 * @param action		行動の種類
	 * @param key			キーコード(com.badlogic.gdx.Input.Keys)
	 * @see com.badlogic.gdx.Input.Keys
	 */
	protected final void registKey(State action, int key)
	{
		inputHelpers[action.ordinal()] = new KeyInputHelper(key);
	}
	
	/**
	 * マウス入力を登録する。
	 * @param action		行動の種類
	 * @param mouse		マウスコード(com.badlogic.gdx.Input.Buttons)
	 * @see com.badlogic.gdx.Input.Buttons
	 */
	protected final void registMouse(State action, int mouse)
	{
		inputHelpers[action.ordinal()] = new MouseInputHelper(mouse);
	}
	
	
	
	/** 
	 * 入力補助インタフェース
	 */
	private interface IInputHelper
	{
		/**
		 * 入力状態を更新する。
		 */
		void update();
		
		/**
		 * プレス入力を取得する。
		 * @return	プレス入力
		 */
		boolean prs();

		/**
		 * トリガ入力を取得する。
		 * @return	トリガ入力
		 */
		boolean trg();

		/**
		 * リリース入力を取得する。
		 * @return	リリース入力
		 */
		boolean rls();
	}
	
	/** 
	 * 入力補助基本クラス
	 */
	private abstract class InputHelperBase implements IInputHelper
	{
		/** 入力状態 */
		protected boolean prev, now;
		
		/**
		 * 入力状態を更新する。
		 * @param newInput		新しい入力状態
		 */
		protected final void update(boolean newInput)
		{
			prev = now;
			now = newInput;
		}

		@Override
		public final boolean prs()
		{
			return now;
		}

		@Override
		public final boolean trg()
		{
			return !prev && now;
		}

		@Override
		public final boolean rls()
		{
			return prev && !now;
		}
	}
	
	/** 
	 * キーボード入力補助クラス
	 */
	private final class KeyInputHelper extends InputHelperBase
	{
		/** キーコード */
		private final int key;
		
		/**
		 * コンストラクタ
		 * @param key		キーコード
		 */
		public KeyInputHelper(int key)
		{
			this.key = key;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isKeyPressed(key));
		}
	}
	
	/** 
	 * マウス入力補助クラス
	 */
	private final class MouseInputHelper extends InputHelperBase
	{
		/** ボタンコード */
		private final int button;
		
		/**
		 * コンストラクタ
		 * @param button		ボタンコード
		 */
		public MouseInputHelper(int button)
		{
			this.button = button;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isButtonPressed(button));
		}
	}
}
