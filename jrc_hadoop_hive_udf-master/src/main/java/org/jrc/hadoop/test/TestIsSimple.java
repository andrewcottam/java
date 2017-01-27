package org.jrc.hadoop.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.BytesWritable;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;
import com.esri.hadoop.hive.LogUtils;

public class TestIsSimple {
	static final Log LOG = LogFactory.getLog(TestIsSimple.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("E:\\cottaan\\My Documents\\geo_json.txt");
		try {
			String json = FileUtils.readFileToString(file);
			try {
				OGCGeometry ogcGeom = OGCGeometry.fromGeoJson(json);
				BytesWritable bw = GeometryUtils.geometryToEsriShapeBytesWritable(ogcGeom);
				if (bw.getLength()==0){
					System.out.println("wibble");
				}
			} catch (Exception e) {
				LogUtils.Log_InvalidText(LOG, json);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
