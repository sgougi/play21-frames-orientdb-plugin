import play.Application;
import play.GlobalSettings;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.wingnest.play2.frames.GraphDB;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		GraphDB.createKeyIndex("className", Vertex.class);

		maintainType("Log", true);
		maintainType("Comment", true);
		maintainType("HasComment", false);

		/*
		 * default GraphDB.setIdHandler(new IdHandler(){
		 * 
		 * @Override public String encode(ORID arg0) { return
		 * Crypto.encryptAES(arg0.toString()); }
		 * 
		 * @Override public String decode(String arg0) { return
		 * Crypto.decryptAES(arg0); }
		 * 
		 * });
		 */
	}

	private void maintainType(String type, boolean isVertex) {
		OrientGraph graph = GraphDB.getGraph();
		OGraphDatabase raw = graph.getRawGraph();
		if ( isVertex ) {
			OClass v = raw.getVertexBaseClass();
			if ( raw.getVertexType(type) == null )
				raw.createVertexType(type, v);
		} else {
			OClass e = raw.getEdgeBaseClass();
			if ( raw.getEdgeType(type) == null )
				raw.createEdgeType(type, e);
		}
	}

}
