package uma.joyofandroid.gdx.utils.antialiasing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class ColoredPolygon {

	protected Mesh mesh;

	protected ArrayList<Vector2> polygonVertices;
	
	protected short[] polygonIndices;
	
	protected Color[] polygonColors;
	
	protected int numberOfPolygonVertices;
	
	protected int numberOfPolygonColors;
	
	protected Vector2 centeroid;
	
	protected boolean isCounterClockWise;

	float[] meshVertices;
	float[] allColors;
	short[] meshIndices;

	
	public ColoredPolygon(Vector2[] vertices, Color[] colors) {
		
		this(vertices,colors,true);
	}
		
		
	public ColoredPolygon(Vector2[] vertices, Color[] colors,boolean setMesh) {

		polygonVertices = new ArrayList<Vector2>();

		for (int i = 0; i < vertices.length; i++) {
			polygonVertices.add(i, vertices[i]);
		}

		polygonColors = colors;
		
		numberOfPolygonVertices = vertices.length;
		numberOfPolygonColors = colors.length;
		
		centeroid = calculateCenteroid();
		
		EarClippingTriangulator ect = new EarClippingTriangulator();

		polygonIndices = getIndices(ect.computeTriangles(polygonVertices),
				polygonVertices);

		
		//colors
		
		allColors = new float[getNumberOfMeshVertices()];

		for (int i = 0; i < numberOfPolygonVertices; i++) {
			allColors[i] = polygonColors[i % numberOfPolygonColors].toFloatBits();
		}
		
		
		// --- vertices
		
		mesh = new Mesh(true, getNumberOfMeshVertices(),getNumberOfMeshIndices(), new VertexAttribute(Usage.Position, 3,
						"a_position"), new VertexAttribute(Usage.ColorPacked,
						4, "a_color"));

		meshVertices = new float[4 * getNumberOfMeshVertices()];

		for (int i = 0; i < numberOfPolygonVertices; i++) {

			Vector2 vi = polygonVertices.get(i);

			meshVertices[4 * i] = vi.x;
			meshVertices[4 * i + 1] = vi.y;
			meshVertices[4 * i + 2] = 0;
			meshVertices[4 * i + 3] = allColors[i];
		}
		
		mesh.setVertices(meshVertices);
		
		// ---- indices
		
		meshIndices = new short[getNumberOfMeshIndices()];

		for (int i = 0; i < polygonIndices.length; i++) {
			meshIndices[i] = polygonIndices[i];
		}
		
		if (setMesh) {
			mesh.setVertices(meshVertices);
			mesh.setIndices(meshIndices);
		}
		

	}
	
	public void drawYourself() {

		mesh.render(GL10.GL_TRIANGLES);
	

	}
	
	public void drawSkeleton() {
		mesh.render(GL10.GL_LINE_STRIP);
	}

	public void drawYourself(int start, int offSet) {
		mesh.render(GL10.GL_TRIANGLES, start, offSet);
	}

	public void transform(Vector2 translate, float scaleX, float scaleY,
			float angle) {

		Matrix3 mat = new Matrix3();

		mat.translate(centeroid.x + translate.x, centeroid.y + translate.y)
				.rotate(angle).scale(scaleX, scaleY)
				.translate(-centeroid.x, -centeroid.y);

		centeroid.mul(mat);

		for (int i = 0; i < meshVertices.length / 4; i++) {

			Vector2 aha = new Vector2(meshVertices[4 * i],
					meshVertices[4 * i + 1]);

			aha.mul(mat);

			meshVertices[4 * i] = aha.x;
			meshVertices[4 * i + 1] = aha.y;

		}

		mesh.setVertices(meshVertices);
	}


	protected int getNumberOfMeshVertices() {
		return numberOfPolygonVertices ;
	}
	
	protected int getNumberOfMeshIndices() {
		return polygonIndices.length ;
	}
	
	protected short[] getIndices(List<Vector2> triangles,
			ArrayList<Vector2> polygon) {

		HashMap<String, Short> map = new HashMap<String, Short>();

		Short i = 0;
		for (Vector2 p : polygon) {
			map.put(p.x + "-" + p.y, i++);
		}

		short[] result = new short[triangles.size()];
		i = 0;
		for (Vector2 p : triangles) {
			result[i++] = map.get(p.x + "-" + p.y);
		}

		return result;
	}
	
	protected Vector2 calculateCenteroid() {
		float x = 0;
		float y = 0;
		Vector2 v1, v2;
		for (int i = 0; i < numberOfPolygonVertices; i++) {
			v1 = polygonVertices.get(i);
			v2 = polygonVertices.get((i + 1) % numberOfPolygonVertices);

			x += (v1.x + v2.x) * (v1.x * v2.y - v2.x * v1.y);
			y += (v1.y + v2.y) * (v1.x * v2.y - v2.x * v1.y);
		}

		float area = calculateSignedArea();

		return new Vector2(x / (6 * area), y / (6 * area));

	}

	protected float calculateSignedArea() {
		float area = 0;
		Vector2 v1, v2;
		for (int i = 0; i < numberOfPolygonVertices; i++) {
			v1 = polygonVertices.get(i);
			v2 = polygonVertices.get((i + 1) % numberOfPolygonVertices);

			area += v1.x * v2.y - v2.x * v1.y;
		}
		System.out.println(area);
		area *= 0.5f;

		isCounterClockWise = area > 0;

		return area;
	}
}
