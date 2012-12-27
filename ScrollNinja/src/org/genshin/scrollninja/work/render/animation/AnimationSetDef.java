package org.genshin.scrollninja.work.render.animation;

import com.badlogic.gdx.math.Vector2;

class AnimationSetDef
{
	public void test()
	{
		System.out.println("size = " + size);
		System.out.println("origin = " + origin);
		
		for(int i = 0;  i < animations.length;  ++i)
		{
			final AnimationDef animation = animations[i];
			System.out.println("animations[" + i + "]:");
			System.out.println("\ttexture = " + animation.texture);
			System.out.println("\tname = " + animation.name);
			System.out.println("\tcount = " + animation.count);
			System.out.println("\tstart = " + animation.start);
			System.out.println("\ttime = " + animation.time);
			System.out.println("\tlooping = " + animation.looping);
		}
	}
	
	public Vector2 size;
	public Vector2 origin;
	public AnimationDef[] animations;
}
