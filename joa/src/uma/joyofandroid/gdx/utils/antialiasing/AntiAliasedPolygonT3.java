package uma.joyofandroid.gdx.utils.antialiasing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class AntiAliasedPolygonT3 extends AntiAliasedPolygon {

	protected static final int EXTRA_VERTICES_PER_EDGE = 2;

	public AntiAliasedPolygonT3(Vector2[] vertices, Color[] colors,
			float antiAliasingBorderWidth) {

		super(vertices, colors, antiAliasingBorderWidth);
	}

	protected int getNumberOfMeshVertices() {
		return numberOfPolygonVertices * (1 + EXTRA_VERTICES_PER_EDGE);
	}

	protected void addOuterHullColors() {

		for (int i = 0; i < numberOfPolygonVertices; i++) {

			allColors[numberOfPolygonVertices + 2 * i] = getColorAlphaZero(polygonColors[i
					% numberOfPolygonColors]);

			allColors[numberOfPolygonVertices + 2 * i + 1] = getColorAlphaZero(mixColors(
					polygonColors[i % numberOfPolygonColors],
					polygonColors[(i + 1) % numberOfPolygonVertices
							% numberOfPolygonColors]));
		}
	}

	protected void addOuterHullVertices() {

		Vector2[] outerVertices = new Vector2[2 * numberOfPolygonVertices];

		for (int i = 0; i < numberOfPolygonVertices; i++) {

			Vector2 outerVertex = getAntiAliasingHullPoint(
					polygonVertices.get((i - 1 + numberOfPolygonVertices)
							% numberOfPolygonVertices), polygonVertices.get(i),
					polygonVertices.get((i + 1) % numberOfPolygonVertices));

			outerVertices[i] = outerVertex;

		}

		for (int i = 0; i < numberOfPolygonVertices; i++) {

			// the vertices for drawing the outer anti-aliasing ring

			meshVertices[(numberOfPolygonVertices + 2 * i) * 4] = outerVertices[i].x;
			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 1] = outerVertices[i].y;
			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 2] = 0;
			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 3] = allColors[numberOfPolygonVertices
					+ 2 * i];

			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 4] = (outerVertices[i].x + outerVertices[(i + 1)
					% numberOfPolygonVertices].x) / 2;
			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 5] = (outerVertices[i].y + outerVertices[(i + 1)
					% numberOfPolygonVertices].y) / 2;
			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 6] = 0;
			meshVertices[(numberOfPolygonVertices + 2 * i) * 4 + 7] = allColors[numberOfPolygonVertices
					+ 2 * i + 1];

		}

	}

	protected void addOuterHullIndices() {
		int numberOfPolygonIndices = polygonIndices.length;
		for (int i = 0; i < numberOfPolygonVertices; i++) {
			meshIndices[numberOfPolygonIndices + 9 * i] = (short) (numberOfPolygonVertices + 2 * i);
			meshIndices[numberOfPolygonIndices + 9 * i + 1] = (short) (numberOfPolygonVertices + (2 * i + 1));
			meshIndices[numberOfPolygonIndices + 9 * i + 2] = (short) i;

			meshIndices[numberOfPolygonIndices + 9 * i + 3] = (short) i;
			meshIndices[numberOfPolygonIndices + 9 * i + 4] = (short) ((i + 1) % numberOfPolygonVertices);
			meshIndices[numberOfPolygonIndices + 9 * i + 5] = (short) (numberOfPolygonVertices + (2 * i + 1));

			meshIndices[numberOfPolygonIndices + 9 * i + 6] = (short) (numberOfPolygonVertices + (2 * i + 1));
			meshIndices[numberOfPolygonIndices + 9 * i + 7] = (short) (numberOfPolygonVertices + (2 * i + 2)
					% (2 * numberOfPolygonVertices));
			meshIndices[numberOfPolygonIndices + 9 * i + 8] = (short) ((i + 1) % numberOfPolygonVertices);

		}
	}

	protected Color mixColors(Color c1, Color c2) {
		return new Color((c1.r + c2.r) / 2, (c1.g + c2.g) / 2,
				(c1.b + c2.b) / 2, (c1.a + c2.a) / 2);
	}

}
