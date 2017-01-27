package org.jrc.hadoop.hive;

import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.jrc.EsriCellCalculator;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;
import com.esri.hadoop.hive.ST_GeometryProcessing;

@Description(name = "ST_CellIntersection", value = "Gets the intersection of the cell and the geometry", extended = "Example:\n" + " Cant be bothered right now\n")
public class ST_CellIntersection extends ST_GeometryProcessing {
	static final Log LOG = LogFactory.getLog(ST_CellIntersection.class.getName());

	public BytesWritable evaluate(double cellSize, long cell, BytesWritable geom) {
		LOG.info("Processing ST_CellIntersection for cell: " + cell);
		if (geom == null || geom.getLength() == 0) {
			return null;
		}

		OGCGeometry ogcGeometry = OGCGeometry.fromBinary(ByteBuffer.wrap(geom.getBytes()));
		// get the envelope of the cell
		EsriCellCalculator cellCalculator = new EsriCellCalculator();
		cellCalculator.setCellSize(cellSize);
		Envelope envelope = cellCalculator.getCellEnvelope(cell);
		OGCGeometry ogcEnvelope = OGCGeometry.createFromEsriGeometry(envelope, null);
		OGCGeometry intersection = ogcGeometry.intersection(ogcEnvelope);

		return GeometryUtils.geometryToEsriShapeBytesWritable(OGCGeometry.createFromEsriGeometry(intersection.getEsriGeometry(), null));
	}
}