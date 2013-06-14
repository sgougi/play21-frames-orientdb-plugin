import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class BlueprintsKeyIndexAndMultiThreadTest {
	
	private static final String ROOT_NODE_NAME = "rootNode";
	private static final String KEY_NAME = "name";

	@Test
	public void test_with_createKeyIndex() throws Exception {
		final OrientGraph graph = new OrientGraph("memory:temp");
		try {
			/* create key index */ 
			graph.createKeyIndex(KEY_NAME, Vertex.class);
			
			/* create the root vertex */ {
				final Vertex v = graph.addVertex(null);
				v.setProperty(KEY_NAME, ROOT_NODE_NAME); /* as key index */
				graph.commit();
				
				final Object rootVertexId = v.getId();
				assertNotNull(rootVertexId);
			}
			
			/* get rootNode */
			final List<Vertex> rootNodes = toArrayList(graph.getVertices(KEY_NAME, ROOT_NODE_NAME));
			assertEquals(1, rootNodes.size());
			
			final Worker worker0 = new Worker(graph);
			final Thread t0 = new Thread(worker0);
			t0.start();
			t0.join();
			
		} finally {
			graph.shutdown();
		}
	}

	private static class Worker implements Runnable {
		private final OrientGraph graph;
		
		public Worker(OrientGraph graph) {
			this.graph = graph;
		}
		
		public void run() {
			ODatabaseRecordThreadLocal.INSTANCE.set(graph.getRawGraph());						
			/* get rootNode */
			final List<Vertex> rootNodes = toArrayList(graph.getVertices(KEY_NAME, ROOT_NODE_NAME));
			assertEquals(1, rootNodes.size());
		}
	}
	
	/////
	
    public static <E> ArrayList<E> toArrayList(final Iterable<E> iterable) {
        if(iterable instanceof ArrayList) {
            return (ArrayList<E>) iterable;
        }
        final ArrayList<E> list = new ArrayList<E>();
        if(iterable != null) {
            for(E e: iterable) {
                list.add(e);
            }
        }
        return list;
    }	
}
