package edu.chalmers.brawlbuddies.util;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * A type of wrapper to cover the holes in Slick with some functionality it lacks.
 * @author Patrik Haar
 * @version 1.0
 */
public class SlickUtil {
	
	/**
	 * Creates a deep copy of the given shape.
	 * @param shape The Shape to make a copy of.
	 * @return The copied Shape.
	 */
	public static Shape copy(Shape shape) {
		Shape copy;
		if(shape.getClass()==Rectangle.class){
			copy = new Rectangle(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight());
		} else if (shape.getClass()==Circle.class){
			copy = new Circle(shape.getCenterX(),shape.getCenterY(),((Circle)shape).getRadius());
		} else if (shape.getClass()==Ellipse.class){
			copy = new Ellipse(shape.getCenterX(),shape.getCenterY(),((Ellipse)shape).getRadius1(),((Ellipse)shape).getRadius2());
		} else {
			throw new IllegalArgumentException("The Shape: " + shape.getClass() + " is not supported for copying.");
		}
		return copy;
	}
	
	/**
	 * Converts a shape to a polygon
	 * @param shape The shape to convert
	 * @return A polygon representing the given shape
	 */
	public static Polygon shapeToPolygon(Shape shape){
		return new Polygon(shape.getPoints());
	}
	
}
