package org.genshin.engine.resource.factory;

/**
 * リソースを生成・管理するインタフェース。<br>
 * リソースを取得する際、使い回せるものがあればそれを使い回す。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <Resource>	リソースの型
 * @param <ID>			リソースを判別するための識別子
 */
public interface ResourceFactoryInterface<Resource, ID>
{
	/**
	 * リソースを取得する。
	 * @param id	リソースの識別子
	 * @return		リソース
	 */
	public Resource get(ID id);
}
