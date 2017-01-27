package org.jrc.hadoop.test;

import org.jrc.hadoop.hive.ST_GeomFromQuadkey;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;

public class TestST_GeomFromQuadkey {
	public static void main(String[] args){
		ST_GeomFromQuadkey udf = new ST_GeomFromQuadkey();
		try {
			BytesWritable bytes = udf.evaluate(new Text("132233213330233"));
			OGCGeometry geom = GeometryUtils.geometryFromEsriShape(bytes);
			System.out.println(geom.asText());
		} catch (UDFArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
