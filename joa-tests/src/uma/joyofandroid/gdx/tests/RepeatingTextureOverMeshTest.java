/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package uma.joyofandroid.gdx.tests;

import uma.joyofandroid.gdx.tests.utils.JoaTest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RepeatingTextureOverMeshTest extends JoaTest {

	private Mesh mesh;

	private Texture texture;

	BitmapFont font;

	SpriteBatch batch;

	private OrthographicCamera camera;

	@Override
	public void create() {
		FileHandle imageFileHandle = Gdx.files
				.internal("data/texture/seamless.jpg");
		texture = new Texture(imageFileHandle, Format.RGB565, false);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		texture.setFilter(Texture.TextureFilter.Linear,
				Texture.TextureFilter.Linear);

		batch = new SpriteBatch();
		font = new BitmapFont();
	}
	
	@Override
	public void resize(int width, int height) {
		float aspectRatio = (float) width / (float) height;
		camera = new OrthographicCamera(2f * aspectRatio, 2f);
		if (mesh == null) {

			mesh = new Mesh(true, 9, 9, new VertexAttribute(Usage.Position, 3,
					"a_position"), new VertexAttribute(
					Usage.TextureCoordinates, 2, "a_texCoords"));
		}
		mesh.setVertices(verticesForUnscaledTexture(texture,
				camera.viewportWidth, camera.viewportHeight, new float[] {
						-0.6f, 0.2f, -0.3f, 0.5f, -0.3f, -0.5f, 0f, 0.5f, 0f,
						-0.5f, 0.3f, 0.0f, 0.2f, -1f, 0.4f, 0.9f, 0.6f, 0f }));
		mesh.setIndices(new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });

	}

	protected float[] verticesForUnscaledTexture(Texture texture,
			float viewportWidth, float viewportHeight, float[] vertices) {

		float[] result = new float[5 * vertices.length / 2];

		float u = ((float) Gdx.graphics.getWidth()) / texture.getWidth()/ viewportWidth;
		float v = ((float) Gdx.graphics.getHeight()) / texture.getHeight()/ viewportHeight;

		for (int i = 0; i < vertices.length / 2; i++) {

			result[5 * i] = vertices[2 * i];
			result[5 * i + 1] = vertices[2 * i + 1];
			result[5 * i + 2] = 0;
			result[5 * i + 3] = u * vertices[2 * i] ;
			result[5 * i + 4] = -v * vertices[2 * i + 1] ;

		}
		return result;

	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {

		camera.update();
		camera.apply(Gdx.gl10);

		renderBackground();
	}

	public void renderBackground() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);

		texture.bind();
		mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 9);

	}

	

	@Override
	public void resume() {
	}

}
