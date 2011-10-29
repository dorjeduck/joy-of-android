package uma.joyofandroid.gdx.utils.antialiasing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class AntiAliasedPolygonT2 extends AntiAliasedPolygon {
	
	protected static final int EXTRA_VERTICES_PER_EDGE = 1;

	public AntiAliasedPolygonT2(Vector2[] vertices, Color[] colors,
			float antiAliasingBorderWidth) {

		super(vertices, colors, antiAliasingBorderWidth);

	}
	
	protected int getNumberOfMeshVertices()
	{
		return polygonIndices.length * (1 + EXTRA_VERTICES_PER_EDGE);
	}

	protected void addOuterHullColors() {

		for (int i = 0; i < numberOfPolygonVertices; i++) {
			allColors[numberOfPolygonVertices + i] = getColorAlphaZero(polygonColors[i
					% numberOfPolygonColors]);
		}
	}

	protected void addOuterHullVertices() {

		for (int i = 0; i < numberOfPolygonVertices; i++) {

			// the vertices for drawing the outer anti-aliasing hull

			Vector2 outerVertex = getAntiAliasingHullPoint(
					polygonVertices.get((i - 1 + numberOfPolygonVertices)
							% numberOfPolygonVertices), polygonVertices.get(i),
					polygonVertices.get((i + 1) % numberOfPolygonVertices));

			meshVertices[(numberOfPolygonVertices + i) * 4] = outerVertex.x;
			meshVertices[(numberOfPolygonVertices + i) * 4 + 1] = outerVertex.y;
			meshVertices[(numberOfPolygonVertices + i) * 4 + 2] = 0;
			meshVertices[(numberOfPolygonVertices + i) * 4 + 3] = allColors[numberOfPolygonVertices
					+ i];

		}

	}

	protected void addOuterHullIndices() {
		
		int numberOfPolygonIndices = polygonIndices.length;

		for (int i = 0; i < numberOfPolygonVertices; i++) {
			meshIndices[numberOfPolygonIndices + 6 * i] = (short) (numberOfPolygonVertices + i);
			meshIndices[numberOfPolygonIndices + 6 * i + 1] = (short) (numberOfPolygonVertices + (i + 1)
					% numberOfPolygonVertices);
			meshIndices[numberOfPolygonIndices + 6 * i + 2] = (short) (i);

			meshIndices[numberOfPolygonIndices + 6 * i + 3] = (short) (i);
			meshIndices[numberOfPolygonIndices + 6 * i + 4] = (short) ((i + 1) % numberOfPolygonVertices);
			meshIndices[numberOfPolygonIndices + 6 * i + 5] = (short) (numberOfPolygonVertices + (i + 1)
					% numberOfPolygonVertices);
		}

	}

}
