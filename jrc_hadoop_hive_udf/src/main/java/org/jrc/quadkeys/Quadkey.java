package org.jrc.quadkeys;

import java.awt.Point;

public class Quadkey {
	public static final double WGS84_SEMI_MAJOR_AXIS = 6378137;
	public static final double EARTH_CIRCUMFERANCE = (Math.PI
			* WGS84_SEMI_MAJOR_AXIS * 2);
	public static final double OFFSET = 20037508.3428;
	public int level;
	public double pixelSize;
	public String quadkey;
	public Point tmsPoint;
	public double minx;
	public double maxx;
	public double miny;
	public double maxy;

	public Quadkey(String key, boolean calculateRealWorldCoordinates) {
		this.level = key.length();
		this.quadkey = key;
		this.tmsPoint = quadKeyToTMS();
		this.pixelSize = EARTH_CIRCUMFERANCE / (Math.pow(2, this.level));
		if (calculateRealWorldCoordinates) {
			this.minx = tileToMeters(this.tmsPoint.x);
			this.maxx = tileToMeters(this.tmsPoint.x + 1);
			this.miny = tileToMeters(this.tmsPoint.y);
			this.maxy = tileToMeters(this.tmsPoint.y + 1);
		}
	}

	private Point quadKeyToTMS() {
		int tileX = 0;
		int tileY = 0;
		for (int i = this.level; i > 0; i--) {
			final int mask = 1 << (i - 1);
			switch (this.quadkey.charAt(this.level - i)) {
			case '0':
				break;
			case '1':
				tileX |= mask;
				break;

			case '2':
				tileY |= mask;
				break;

			case '3':
				tileX |= mask;
				tileY |= mask;
				break;

			default:
				throw new IllegalArgumentException(
						"Invalid QuadKey digit sequence.");
			}
		}
		tileY = (int) ((Math.pow(2, this.level)) - tileY - 1);
		return new Point(tileX, tileY);
	}

	private double tileToMeters(int tile) {
		return (tile * this.pixelSize) - OFFSET;
	}

}
