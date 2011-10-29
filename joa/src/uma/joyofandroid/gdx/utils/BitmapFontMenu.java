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

package uma.joyofandroid.gdx.utils;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BitmapFontMenu {
	public static enum AnchorPosition {

		TOP_LEFT, CENTERED_LEFT, BOTTOM_LEFT, TOP_CENTERED, CENTERED, BOTTOM_CENTERED, TOP_RIGHT, CENTERED_RIGHT, BOTTOM_RIGHT
	}

	BitmapFont font;

	AnchorPosition anchorPosition;
	String menuString;
	Vector2 anchor;

	String[] menuStringLines;

	ArrayList<String> allMenuPoints;

	int numberOfLines;

	float menuHeight;
	float menuPointHeight;
	float lineHeight;
	float spaceWidth;
	float xheight;
	float ascent;
	float descent;
	float capHeight;

	float xTouchAreaAlign;

	final String spaceReplace = "_-_";

	ArrayList<Rectangle> allRectangles;

	public BitmapFontMenu(BitmapFont font, String menuString, Vector2 anchor,
			AnchorPosition orientation) {
		this(font, menuString, anchor, orientation, 0);
	}

	public BitmapFontMenu(BitmapFont font, String menuString, Vector2 anchor,
			AnchorPosition anchorPosition, float xTouchAreaAlign) {
		this.font = font;
		this.setMenuString(menuString);
		this.anchor = anchor;
		this.anchorPosition = anchorPosition;
		this.xTouchAreaAlign = xTouchAreaAlign;

		init();
	}

	private void init() {

		menuStringLines = getMenuString().split("\n");

		numberOfLines = menuStringLines.length;

		lineHeight = font.getLineHeight();
		spaceWidth = font.getSpaceWidth();
		ascent = font.getAscent();
		descent = font.getDescent();
		capHeight = font.getCapHeight();
		menuPointHeight = capHeight - descent;
		xheight = font.getXHeight();

		menuHeight = numberOfLines * lineHeight;

		float yStartPos = 0;

		if (anchorPosition == AnchorPosition.TOP_LEFT
				|| anchorPosition == AnchorPosition.TOP_CENTERED
				|| anchorPosition == AnchorPosition.TOP_RIGHT) {
			yStartPos = anchor.y;
		} else if (anchorPosition == AnchorPosition.CENTERED_LEFT
				|| anchorPosition == AnchorPosition.CENTERED
				|| anchorPosition == AnchorPosition.CENTERED_RIGHT) {
			yStartPos = anchor.y + (menuHeight - ascent) / 2;
		} else if (anchorPosition == AnchorPosition.BOTTOM_LEFT
				|| anchorPosition == AnchorPosition.BOTTOM_CENTERED
				|| anchorPosition == AnchorPosition.BOTTOM_RIGHT) {
			yStartPos = anchor.y + menuHeight - ascent / 2;
		}

		float[] xStartPos = new float[numberOfLines];

		allMenuPoints = new ArrayList<String>();
		allRectangles = new ArrayList<Rectangle>();

		for (int i = 0; i < numberOfLines; i++) {

			if (anchorPosition == AnchorPosition.CENTERED_LEFT
					|| anchorPosition == AnchorPosition.TOP_LEFT
					|| anchorPosition == AnchorPosition.BOTTOM_LEFT) {
				xStartPos[i] = anchor.x;
			} else if (anchorPosition == AnchorPosition.TOP_CENTERED
					|| anchorPosition == AnchorPosition.CENTERED
					|| anchorPosition == AnchorPosition.BOTTOM_CENTERED) {
				xStartPos[i] = anchor.x
						- font.getBounds(menuStringLines[i].replace("\"", "")).width
						/ 2;
			} else if (anchorPosition == AnchorPosition.BOTTOM_RIGHT
					|| anchorPosition == AnchorPosition.CENTERED_RIGHT
					|| anchorPosition == AnchorPosition.TOP_RIGHT) {
				xStartPos[i] = anchor.x
						- font.getBounds(menuStringLines[i].replace("\"", "")).width;
			}

			TextBounds tb;

			float shiftX = 0;

			String sola[] = menuStringLines[i].split("\"");

			if (sola.length > 1) {

				menuStringLines[i] = "";
				int k = 1;
				for (; k < sola.length; k += 2) {
					menuStringLines[i] += sola[k - 1];
					menuStringLines[i] += sola[k].replace(" ", spaceReplace);
				}
				if (k == sola.length) {
					menuStringLines[i] += sola[k - 1];
				}
			}
			String[] parts = menuStringLines[i].split(" ");

			for (int j = 0; j < parts.length; j++) {

				if (parts[j].length() > 0) {
					parts[j] = parts[j].replace(spaceReplace, " ");
					tb = font.getBounds(parts[j]);
					Rectangle r = new Rectangle();
					r.x = xStartPos[i] + shiftX - xTouchAreaAlign;
					r.y = yStartPos - i * lineHeight;
					r.width = tb.width + 1 + 2 * xTouchAreaAlign;
					r.height = menuPointHeight;

					allRectangles.add(r);

					allMenuPoints.add(parts[j]);

					shiftX += tb.width;
				}
				shiftX += spaceWidth;
			}

		}

	}

	public void drawMenus(SpriteBatch batcher) {
		int i = 0;
		for (Rectangle r : allRectangles) {

			font.draw(batcher, allMenuPoints.get(i), r.x + xTouchAreaAlign, r.y);
			i++;
		}

	}

	public void debugDrawRectangle(ShapeRenderer renderer) {

		renderer.begin(ShapeType.Line);
		renderer.setColor(0.3f, 0.3f, 0.3f, 1);

		for (Rectangle r : allRectangles) {

			renderer.line(r.x, r.y, r.x + r.width, r.y);
			renderer.line(r.x + r.width, r.y, r.x + r.width, r.y - r.height);
			renderer.line(r.x + r.width, r.y - r.height, r.x, r.y - r.height);
			renderer.line(r.x, r.y - r.height, r.x, r.y);

		}
		renderer.end();

	}

	public String checkTouchPoint(float x, float y) {
		int i = 0;
		for (Rectangle r : allRectangles) {
			if (contains(r, x, y)) {
				return allMenuPoints.get(i);
			}
			i++;
		}
		return null;
	}

	private boolean contains(Rectangle r, float x, float y) {

		return r.x <= x && r.x + r.width >= x && r.y >= y
				&& r.y - r.height <= y;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
		init();
	}

	public AnchorPosition getAnchorPosition() {
		return anchorPosition;
	}

	public void setAnchorPosition(AnchorPosition anchorPosition) {
		this.anchorPosition = anchorPosition;
		init();
	}

	public String getMenuString() {
		return menuString;
	}

	public void setMenuString(String menuString) {
		this.menuString = menuString;
		init();
	}

	public Vector2 getAnchor() {
		return anchor;
	}

	public void setAnchor(Vector2 anchor) {
		this.anchor = anchor;
		init();
	}

}
