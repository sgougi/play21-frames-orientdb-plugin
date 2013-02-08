package com.tinkerpop.blueprints.impls.orient;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Index;

import java.util.HashMap;
import java.util.Map;

/**
 * A Blueprints implementation of the graph database OrientDB (http://www.orientechnologies.com)
 *
 * @author Luca Garulli (http://www.orientechnologies.com)
 * @author Shigeru GOUGI (http://www.wingnest.com)
 */
class OrientGraphContext {
    public Map<String, OrientIndex<? extends OrientElement>> manualIndices = new HashMap<String, OrientIndex<? extends OrientElement>>();
    public OGraphDatabase rawGraph;

    @Override
	protected void finalize() throws Throwable {
		super.finalize();
		terminate();	
	}

	public void terminate() {
		if(manualIndices != null) {
			for (Index<? extends Element> idx : manualIndices.values())
				((OrientIndex<?>) idx).close();
	        manualIndices.clear();
	        manualIndices = null;			
		}
		if(rawGraph != null){
			rawGraph.commit();
			rawGraph.close();
			rawGraph = null;
		}
	}    
}