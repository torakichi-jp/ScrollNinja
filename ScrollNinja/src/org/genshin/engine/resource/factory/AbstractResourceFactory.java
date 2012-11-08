package org.genshin.engine.resource.factory;

public abstract class AbstractResourceFactory<Resource, ID> implements ResourceFactoryInterface<Resource, ID>
{
	@Override
	public final Resource get(ID id)
	{
		// TODO ここにリソースを使い回す仕組みを実装する。
		return create(id);
	}
	
	/**
	 * 新しいリソースを生成する。
	 * @param id	リソースの識別子
	 * @return		新しいリソース
	 */
	protected abstract Resource create(ID id);
}
