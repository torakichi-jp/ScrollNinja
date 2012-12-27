package org.genshin.engine.system;

/**
 * 破棄処理を持つインタフェース
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public interface Disposable
{
	/**
	 * 破棄する。
	 */
	public void dispose();
	
	/**
	 * 既に破棄されているか調べる。
	 * @return	既に破棄されている場合はtrue
	 */
	public boolean isDisposed();
}
