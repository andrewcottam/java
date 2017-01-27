package org.jrc.hadoop.hive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.jrc.quadkeys.Quadkey;

import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;
import com.esri.hadoop.hive.LogUtils;
import com.esri.hadoop.hive.ST_Polygon;

@Description(name = "ST_GeomFromQuadKey", value = "_FUNC_(qk) - construct an ST_Polygon from a quadkey", extended = "Example:\n  SELECT _FUNC_('132320330230023') FROM src LIMIT 1;  -- constructs ST_Polygon\n")
public class ST_GeomFromQuadkey extends ST_Polygon {

	static final Log LOG = LogFactory
			.getLog(ST_GeomFromQuadkey.class.getName());

	public BytesWritable evaluate(Text qk) throws UDFArgumentException {
		Quadkey quadKey = new Quadkey(qk.toString(), true);
		String minx = String.valueOf(quadKey.minx);
		String maxx = String.valueOf(quadKey.maxx);
		String miny = String.valueOf(quadKey.miny);
		String maxy = String.valueOf(quadKey.maxy);
		String wkt = "polygon ((" + minx + " " + miny + "," + minx + " " + maxy
				+ "," + maxx + " " + maxy + "," + maxx + " " + miny + "))";
		try {
			OGCGeometry ogcObj = OGCGeometry.fromText(wkt);
			SpatialReference spatialReference = SpatialReference.create(3857);
			ogcObj.setSpatialReference(spatialReference);
			if (ogcObj.geometryType().equals("Polygon")) {
				return GeometryUtils.geometryToEsriShapeBytesWritable(ogcObj);
			} else {
				LogUtils.Log_InvalidType(LOG,
						GeometryUtils.OGCType.ST_LINESTRING,
						GeometryUtils.OGCType.UNKNOWN);
				return null;
			}

		} catch (Exception e) { // IllegalArgumentException, GeometryException
			LogUtils.Log_InvalidText(LOG, wkt);
			return null;
		}
	}
}
