package edu.chalmers.brawlbuddies.model;

/**
 * Enum for holding and calculating simple movement directions.
 * 
 * @author Matz Larsson
 * @revised Patrik Haar
 * @version 1.0
 */

public enum Direction {
	//All values are self explaining.
	LEFT(-1, 0) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.UP) {
				return Direction.NORTHWEST;
			} else if (dir == Direction.DOWN) {
				return Direction.SOUTHWEST;
			} else if (dir == Direction.RIGHT) {
				return Direction.NONE;
			} else if (dir == Direction.SOUTHWEST || dir == Direction.NORTHWEST) {
				return dir;
			}
			return this;
		}
	},
	RIGHT(1, 0) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.UP) {
				return Direction.NORTHEAST;
			} else if (dir == Direction.DOWN) {
				return Direction.SOUTHEAST;
			} else if (dir == Direction.LEFT) {
				return Direction.NONE;
			} else if ( dir == Direction.SOUTHEAST || dir == Direction.NORTHEAST) {
				return dir;
			}
			return this;
		}
	},
	UP(0, -1) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.LEFT) {
				return Direction.NORTHWEST;
			} else if (dir == Direction.RIGHT) {
				return Direction.NORTHEAST;
			} else if (dir == Direction.DOWN) {
				return Direction.NONE;
			} else if( dir == Direction.NORTHEAST || dir == Direction.NORTHWEST) {
				return dir;
			}
			return this;
		}
	},
	DOWN(0, 1) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.RIGHT) {
				return Direction.SOUTHEAST;
			} else if (dir == Direction.LEFT) {
				return Direction.SOUTHWEST;
			} else if (dir == Direction.UP) {
				return Direction.NONE;
			} else if ( dir == Direction.SOUTHEAST || dir == Direction.SOUTHWEST){
				return dir;
			}
			return this;
		}
	},
	NORTHWEST(-1, -1) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.RIGHT) {
				return Direction.NORTHEAST;
			} else if (dir == Direction.DOWN) {
				return Direction.SOUTHWEST;
			} else if (dir == Direction.SOUTHEAST) {
				return Direction.NONE;
			} else if (dir == Direction.SOUTHWEST){
				return Direction.LEFT;
			} else if ( dir == Direction.NORTHEAST){
				return Direction.UP;
			}
			return this;
		}
	},
	NORTHEAST(1, -1) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.LEFT) {
				return Direction.NORTHWEST;
			} else if (dir == Direction.DOWN) {
				return Direction.SOUTHEAST;
			} else if (dir == Direction.SOUTHWEST) {
				return Direction.NONE;
			} else if ( dir == Direction.SOUTHEAST){
				return Direction.RIGHT;
			} else if ( dir == Direction.NORTHWEST){
				return Direction.UP;
			}
			return this;
		}
	},
	SOUTHWEST(-1, 1) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.UP) {
				return Direction.NORTHWEST;
			} else if (dir == Direction.RIGHT) {
				return Direction.SOUTHEAST;
			} else if (dir == Direction.NORTHEAST) {
				return Direction.NONE;
			} else if (dir == Direction.NORTHWEST){
				return Direction.LEFT;
			} else if ( dir == Direction.SOUTHEAST){
				return Direction.DOWN;
			}
			return this;
		}
	},
	SOUTHEAST(1, 1) {
		@Override
		public Direction add(Direction dir) {
			if (dir == Direction.UP) {
				return Direction.NORTHEAST;
			} else if (dir == Direction.LEFT) {
				return Direction.SOUTHWEST;
			} else if (dir == Direction.NORTHWEST) {
				return Direction.NONE;
			} else if (dir == Direction.NORTHEAST){
				return Direction.RIGHT;
			} else if ( dir == Direction.SOUTHWEST){
				return Direction.DOWN;
			}
			return this;
		}
	},
	NONE(0, 0) {
		@Override
		public Direction add(Direction dir) {
			return dir;
		}
	};
	
	private int x;
	private int y;
	
	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the Directions x-value.
	 * @return The x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the Directions y-value.
	 * @return The y
	 */
	public int getY() {
		return y;
	}
	
	// This method is overridden by the enum constants.
	/**
	 * Used to add two directions to create a direction that is the same, none, or turned 45 degrees
	 * depending on the given direction. With the exception of Direction.NONE which will be replaced
	 * by the given Direction.
	 * @param dir The direction to add.
	 * @return A new Direction that is the same, none, or turned 45 degrees depending on the given direction.
	 */
	public Direction add(Direction dir) {
		return this;
	}
	
	/**
	 * Get this Directions projection on the x-axis.
	 * @return The resulting LEFT, RIGHT or NONE Direction.
	 */
	public Direction getXDirection() {
		return getX()<0 ? Direction.LEFT : (getX()>0 ? Direction.RIGHT : Direction.NONE);
	}
	
	/**
	 * Get this Directions projection on the y-axis.
	 * @return The resulting UP, DOWN or NONE Direction.
	 */
	public Direction getYDirection() {
		return getY()<0 ? Direction.UP : (getY()>0 ? Direction.DOWN : Direction.NONE);
	}
	
	/**
	 * Returns a direction based on the given coordinates where 0,0 will return the Direction NONE.
	 * @param x A value -infinity < x < infinity.
	 * @param y A value -infinity < x < infinity.
	 * @return The Direction created.
	 */
	public static Direction getDirection(float x, float y) {
		return Direction.NONE.add(x<0?Direction.LEFT:x>0?Direction.RIGHT:Direction.NONE)
				.add(y<0?Direction.UP:y>0?Direction.DOWN:Direction.NONE);
	}
	/**
	 * Return a direction based on a angle
	 * @param angle to be converted to a direction
	 * @return a direction to match the angel
	 */
	public static Direction getDirection(double angle){
		Direction dir = Direction.NONE;
		if(angle<90 || angle>270){
			dir = dir.add(Direction.RIGHT);
		}
		if(angle>90 && angle<270){
			dir = dir.add(Direction.LEFT);
		}
		if(angle>180 && angle<360){
			dir = dir.add(Direction.UP);
		}
		if(angle<180 && angle>0){
			dir = dir.add(Direction.DOWN);
		}
		return dir;
	}
}
