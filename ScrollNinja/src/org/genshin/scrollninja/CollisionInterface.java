package org.genshin.scrollninja;

public interface CollisionInterface {
	
	
	public void dispatch(Player obj);
	public void dispatch(Enemy obj);
	public void dispatch(Effect obj);
	public void dispatch(Item obj);
	public void dispatch(StageObject obj);
//	public void dispatch()

}
