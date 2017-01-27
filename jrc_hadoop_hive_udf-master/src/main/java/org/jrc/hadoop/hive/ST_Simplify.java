package org.jrc.hadoop.hive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;
import com.esri.hadoop.hive.LogUtils;
import com.esri.hadoop.hive.ST_GeometryProcessing;

@Description(name = "ST_Simplify", value = "_FUNC_(ST_Geometry) - Simplifies the geometry", extended = "Example:\n" + " Cant be bothered right now\n")
public class ST_Simplify extends ST_GeometryProcessing {

	static final Log LOG = LogFactory.getLog(ST_Simplify.class.getName());

	public BytesWritable evaluate(BytesWritable geom) {
		LOG.info("Processing geometry");
		if (geom == null || geom.getLength() == 0) {
			return null;
		}

		OGCGeometry ogcGeometry = GeometryUtils.geometryFromEsriShape(geom);
		Geometry geometry = ogcGeometry.getEsriGeometry();
		if (geometry == null) {
			LogUtils.Log_ArgumentsNull(LOG);
			return null;
		}
		Geometry simplifiedGeometry = GeometryEngine.simplify(geometry, null);

		// TODO persist type information (polygon vs multipolygon)
		return GeometryUtils.geometryToEsriShapeBytesWritable(OGCGeometry.createFromEsriGeometry(simplifiedGeometry, null));
	}
}