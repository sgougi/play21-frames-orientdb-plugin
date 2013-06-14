import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class BlueprintsUnderMultiThreadTest {
	
	@Test
	public void test() throws Exception {
		final OrientGraph graph = new OrientGraph("memory:temp");
		final Object rootVertexId;
		OGlobalConfiguration.CACHE_LEVEL1_ENABLED.setValue(false);
		
		/* create the root vertex */ {
			ODatabaseRecordThreadLocal.INSTANCE.set(graph.getRawGraph());			
			Vertex v = graph.addVertex(null);
			graph.commit();
			rootVertexId = v.getId();
			assertNotNull(rootVertexId);
		}
		  
		final Worker worker0 = new Worker(graph, rootVertexId, 0);
		final Worker worker1 = new Worker(graph, rootVertexId, 1);
		final Thread t0 = new Thread(worker0);
		final Thread t1 = new Thread(worker1);
		
		t0.start();
		t1.start();
		
		/////////////////////////// 
		
		worker0.yourTurn();
		worker1.yourTurn();
		worker0.yourTurn();
		worker1.yourTurn();

		/////////////////////////// 
		
		/* terminate */ {
			worker0.exit();
			worker1.exit();
			t0.join();
			t1.join();
			
			if( worker0.exception != null) 
				worker0.exception.printStackTrace();
			if( worker1.exception != null) 
				worker1.exception.printStackTrace();
		}
	}
	
	private static class Worker implements Runnable {
		private final int editorId;
		private final OrientGraph graph;
		private final Object rootVertexId;
		
		private volatile boolean yourTurn = false;
		private volatile boolean bExit = false;		
		private Exception exception = null;
		
		public Worker(OrientGraph graph, Object fvid, int id) {
			this.graph = graph;
			this.rootVertexId = fvid; 
			this.editorId = id;
		}
		
		public void yourTurn() {
			if(bExit) return;			
			yourTurn = true;
			while(yourTurn && !bExit);			
		}
		
		public void exit() {
			bExit = true;
			System.out.println(String.format("editorId = %d : exit", editorId));			
		}
		
		public void run() {
			ODatabaseRecordThreadLocal.INSTANCE.set(graph.getRawGraph());						
			try {
				while(!bExit) {
					if(yourTurn) {
						System.out.println(String.format("turn : editorId = %d", editorId));
						edit();
						yourTurn = false;
					}
				}
				return;
			} catch ( Exception e ) {
				System.out.println(String.format("[Caught exception] : editorId = %d : %s", editorId, e.getMessage()));
				exception = e;				
				exit();
			}
		}
		
		private void edit() {
			Vertex f = graph.getVertex(rootVertexId);
			Vertex v = graph.addVertex(null);
			@SuppressWarnings("unused")
			Edge e = graph.addEdge(null, f, v, "edge");
			graph.commit();
		}
	}

}
