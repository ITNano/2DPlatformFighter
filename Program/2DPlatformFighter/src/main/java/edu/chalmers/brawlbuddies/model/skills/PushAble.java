package edu.chalmers.brawlbuddies.model.skills;

import edu.chalmers.brawlbuddies.model.Velocity;
/**
 * A interface to describe a object that can be pushed
 * @author David Gustafsson
 * @version 1.0
 *
 */
public interface PushAble {
	/**
	 * Move a object with a velocity 
	 * @param v - the velocity the object will be pushed with
	 */
	public void push(Velocity v);
}
