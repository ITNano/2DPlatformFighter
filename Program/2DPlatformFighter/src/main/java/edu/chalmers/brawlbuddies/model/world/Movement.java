package edu.chalmers.brawlbuddies.model.world;

import edu.chalmers.brawlbuddies.Constants;

import edu.chalmers.brawlbuddies.model.Direction;
import edu.chalmers.brawlbuddies.model.Position;
import edu.chalmers.brawlbuddies.model.Velocity;

/**
 * This is a class for handling movement. It contains methods for handling both
 * controlled movement (moving), gravity and other outer forces/velocities.
 * @author Matz Larsson
 * @version 0.1
 *
 */

public class Movement {
	
	/* Parameters to set for different objects */
	private Velocity baseSpeed;
	private Velocity gravity;
	
	/* Inner variables used by this class */
	private Velocity innerSpeed;
	private Velocity outerSpeed;
	
	/**
	 * Creates a new Movement object with no base speed at all.
	 * This means it will not be movable by controls.
	 */
	public Movement(){
		this(new Velocity(0, 0));
	}
	
	/**
	 * Initiates this Movement with a base speed.
	 * @param baseSpeed The base speed for this Movement object
	 */
	public Movement(Velocity baseSpeed){
		this(baseSpeed, new Velocity(0, 1000));
	}

	/**
	 * Initiates this Movement with a base speed and a gravity
	 * @param baseSpeed The base speed to use for controlled movements
	 * @param gravity The gravity
	 */
	public Movement(Velocity baseSpeed, Velocity gravity){
		this.baseSpeed = baseSpeed;
		this.gravity = gravity;
		this.innerSpeed = new Velocity(0, 0);
		this.outerSpeed = new Velocity(0, 0);
	}
	
	/**
	 * Resets the speed in the alignment specified by the parameter align.
	 * @param align Alignment to reset in.
	 */
	public void resetSpeed(Alignment align){
		switch(align){
			case VERTICAL:		setInnerSpeed(this.innerSpeed.getX(), 0);
								setOuterSpeed(this.outerSpeed.getX(), 0);break;
			case HORIZONTAL:	setInnerSpeed(0, this.innerSpeed.getY());
								setOuterSpeed(0, this.outerSpeed.getY());break;
			case BOTH:			setInnerSpeed(0, 0);
								setOuterSpeed(0, 0);break;
			case NONE:			/* Do nothing */ break;
		}
	}
	
	/**
	 * Sets the base speed to the specified velocity
	 * @param baseSpeed The new base speed to use
	 */
	public void setBaseSpeed(Velocity baseSpeed){
		this.baseSpeed = baseSpeed;
	}
	/**
	 * Returns the current base speed
	 * @return The current base speed
	 */
	public Velocity getBaseSpeed(){
		return this.baseSpeed;
	}
	
	/**
	 * Sets the current gravity
	 * @param gravity The gravity
	 */
	public void setGravity(Velocity gravity){
		this.gravity = gravity;
	}
	/**
	 * Returns the current gravity
	 * @return The gravity
	 */
	public Velocity getGravity(){
		return this.gravity;
	}
	
	/**
	 * Sets up the speed for a movement depending on the direction.
	 * The base speed will be used.
	 * @param dir The direction to use.
	 */
	public void move(Direction dir){
		switch(dir){
			case RIGHT:	setInnerSpeed(this.baseSpeed.getX(), this.innerSpeed.getY());break;
			case LEFT:	setInnerSpeed(-this.baseSpeed.getX(), this.innerSpeed.getY());break;
			case UP:	setInnerSpeed(this.innerSpeed.getX(), -this.baseSpeed.getY());break;
			case DOWN:	setInnerSpeed(this.innerSpeed.getX(), this.baseSpeed.getY());break;
			case NONE:	setInnerSpeed(0, this.innerSpeed.getY()); break;
		}
	}	

	/**
	 * Adds another outer speed to the Movement
	 * @param outerSpeed The outer speed to add
	 */
	public void addOuterSpeed(Velocity outerSpeed){
		this.outerSpeed.increase(outerSpeed);
	}
	/**
	 * Removes an outer speed of the Movement
	 * @param outerSpeed The outer speed to remove
	 */
	public void removeOuterSpeed(Velocity outerSpeed){
		this.outerSpeed.decrease(outerSpeed);
	}
	/**
	 * Returns the speed that is used by outer objects like effects etc.
	 * NOTE: Only use this in subclasses to Movement.
	 * @return The outer speed of the Movement
	 */
	protected Velocity getOuterSpeed(){
		return this.outerSpeed;
	}
	/**
	 * Sets the outer speed to an absolute value.
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	protected void setOuterSpeed(float x, float y){
		this.outerSpeed.set(x, y);
	}
	
	/**
	 * Increases the inner speed by the Velocity with coordinates (x, y)
	 * @param x The x-coordinate of the Vector to add
	 * @param y The y-coordinate of the Vector to add
	 */
	protected void increaseInnerSpeed(float x, float y){
		this.innerSpeed.increase(x, y);
	}
	/**
	 * Increases the inner speed by the Velocity v
	 * @param v The Vector to add
	 */
	protected void increaseInnerSpeed(Velocity v){
		this.increaseInnerSpeed(v.getX(), v.getY());
	}
	/**
	 * Returns the inner speed that is used by controlled movements
	 * NOTE: Only use this in subclasses to Movement.
	 * @return The inner speed of the Movement
	 */
	protected Velocity getInnerSpeed(){
		return this.innerSpeed;
	}
	/**
	 * Sets the inner speed to an absolute value.
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	protected void setInnerSpeed(float x, float y){
		this.innerSpeed.set(x, y);
	}
	
	/**
	 * Returns the total Velocity of the Movement at this very moment.
	 * @return The current Velocity of the Movement
	 */
	public Velocity getTotalVelocity(){
		return this.innerSpeed.add(this.outerSpeed);
	}
	
	/**
	 * Increases the gravity and calculates the next position of the object.
	 * @param previous	The current position
	 * @param delta		The time elapsed since the last call to this method
	 * @return			The new calculated position
	 */
	public Position nextPosition(Position previous, int delta){
		//Add gravity to the inner speed (to operate more closely with the movement)
		Velocity tmpGrav = this.gravity.scale(((float)(delta))/Constants.MODIFIER);
		this.increaseInnerSpeed(tmpGrav);
		
		//Calculate next position
		Velocity totalSpeed = this.getTotalVelocity();
		Position diff = new Position((totalSpeed.getX()*((float)(delta))/Constants.MODIFIER),
									 (totalSpeed.getY()*((float)(delta))/Constants.MODIFIER));
		return previous.add(diff);
	}
	
	
	/**
	 * Simple enum for keeping the alignment of collisions.
	 * @author Matz Larsson
	 * @version 0.1
	 *
	 */
	public enum Alignment{
		HORIZONTAL, VERTICAL, BOTH, NONE
	}

}