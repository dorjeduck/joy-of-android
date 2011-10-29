package uma.joyofandroid.gdx.utils.antialiasing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public abstract class AntiAliasedPolygon extends ColoredPolygon {

	
	float antiAliasingBorderWidth;

	
	public AntiAliasedPolygon(Vector2[] vertices, Color[] colors,
			float antiAliasingBorderWidth) {

		super(vertices,colors,false);
		

		this.antiAliasingBorderWidth = antiAliasingBorderWidth;

		
		// ------------------------ hull colors

		addOuterHullColors();

		// ------------------------- hull vertices

		
		addOuterHullVertices();

		mesh.setVertices(meshVertices);

		// --------------------------- indices

		addOuterHullIndices();

		mesh.setIndices(meshIndices);

	}

	abstract void addOuterHullColors();

	abstract void addOuterHullVertices();

	abstract void addOuterHullIndices();
	
	abstract protected int getNumberOfMeshVertices();
	
	
	protected int getNumberOfMeshIndices() {
		return polygonIndices.length + 3 * getNumberOfMeshVertices();
	}
	

	protected Vector2 getAntiAliasingHullPoint(Vector2 v1, Vector2 v2,
			Vector2 v3) {

		Vector2 lv1 = v1.cpy();
		Vector2 lv2 = v2.cpy();
		Vector2 lv3 = v3.cpy();

		lv1.sub(lv2);
		lv3.sub(lv2);

		float angle = lv1.angle() - lv3.angle();

		boolean sharp = ((angle > 0 && angle < 180) || angle < -180);

		float sign = (sharp && isCounterClockWise)
				|| !(sharp || isCounterClockWise) ? 1 : -1;

		Vector2 direction = lv1.add(lv3).mul(0.5f).nor().mul(sign);

		return lv2.sub(direction.mul(antiAliasingBorderWidth));

	}

	

	protected float getColorAlphaZero(Color c) {
		return Color.toFloatBits(c.r, c.g, c.b, 0);
		//return Color.toFloatBits(0,0,0,0);
	}

}
