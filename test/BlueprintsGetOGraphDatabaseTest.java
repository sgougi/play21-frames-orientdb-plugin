import static org.junit.Assert.*;

import org.junit.Test;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class BlueprintsGetOGraphDatabaseTest {
	
	@Test
	public void test_get_OGraphDatabase() throws Exception {
		final OrientGraph graph = new OrientGraph("memory:temp");
		try {
			ODatabaseDocumentTx ddtx = graph.getRawGraph();
			assertNotNull(ddtx);
			
			assertTrue(ddtx instanceof OGraphDatabase); // ###### java.lang.AssertionError
			
			OGraphDatabase ogd = (OGraphDatabase)graph.getRawGraph();
			
			OClass v = ogd.getVertexBaseClass();
			assertNotNull(v);
			
			OClass e = ogd.getEdgeBaseClass();
			assertNotNull(e);

			ogd.setUseCustomTypes(false);
			assertFalse(ogd.isUseCustomTypes());
			
			ogd.setUseCustomTypes(true);
			assertTrue(ogd.isUseCustomTypes());			
			
		} finally {
			graph.shutdown();
		}
	}
	
}
