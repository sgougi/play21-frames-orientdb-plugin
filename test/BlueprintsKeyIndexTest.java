import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class BlueprintsKeyIndexTest {
	
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
			assertEquals(1, rootNodes.size()); // ########## java.lang.AssertionError: expected:<1> but was:<0>		
		} finally {
			graph.shutdown();
		}
	}
		
	@Test
	public void test_without_createKeyIndex() throws Exception {
		final OrientGraph graph = new OrientGraph("memory:temp");
		try {
			/* create key index */ 
			//graph.createKeyIndex("name", Vertex.class);
			
			/* create the root vertex */ {
				final Vertex v = graph.addVertex(null);
				v.setProperty(KEY_NAME, ROOT_NODE_NAME); /* as key index */
				graph.commit();
				
				final Object rootVertexId = v.getId();
				assertNotNull(rootVertexId);
			}
	
			/* get rootNode */		
			final List<Vertex> rootNodes = toArrayList(graph.getVertices(KEY_NAME, ROOT_NODE_NAME));
			assertEquals(1, rootNodes.size()); // ########## no problem		
		} finally {
			graph.shutdown();
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
