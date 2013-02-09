/*
 * Copyright since 2013 Shigeru GOUGI (sgougi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;


import org.junit.*;
import static org.junit.Assert.*;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class BluePrintsUnderMultiThreadTest {
	
	static class Editor implements Runnable {
		private final int id;
		private Exchanger<Exception> exchanger;
		private final List<Integer> orders;
		private final OrientGraph graph;
		private final Object fvid;
		
		public Editor(OrientGraph graph, Object fvid, int id, Exchanger<Exception> exchanger, List<Integer> orders) {
			this.graph = graph;
			this.fvid = fvid; 
			this.id = id;
			this.exchanger = exchanger;
			this.orders = orders;
		}
		public void run() {
			Exception thrown = null;
			try {
				while(orders.size() > 0) {
					if(orders.get(0) == id) {
						System.out.println(String.format("id = %d : editing", id));
						edit();
						orders.remove(0);
					}
				}
				return;
			} catch ( Exception e ) {
				System.out.println(String.format("id = %d : catch = %s", id, e.getMessage()));
				thrown = e;
			} finally {
				try {
					exchanger.exchange(thrown);
					exchanger = null;
				} catch ( InterruptedException ignored ) {}
			}
		}
		
		private void edit() {
			Vertex f = graph.getVertex(fvid);
			Vertex v = graph.addVertex(null);
			@SuppressWarnings("unused")
			Edge e = graph.addEdge(null, f, v, "edge");
			graph.stopTransaction(Conclusion.SUCCESS);
		}
	}
	
	@Test
	public void test1() throws Exception {
		/*
		 *  ** This test fails. why? **
		 */
		OrientGraph graph0 = new OrientGraph("memory:temp");
		OrientGraph graph1 = new OrientGraph("memory:temp");
		process(graph0, graph1);
	}
	
	@Test
	public void test2() throws Exception {
		OrientGraph graph0 = new OrientGraph("memory:temp");
		process(graph0, graph0);
	}
	
	private void process(OrientGraph graph0, OrientGraph graph1) throws Exception {
		final Object fvid;
		{
			Vertex v = graph0.addVertex(null);
			graph0.stopTransaction(Conclusion.SUCCESS);
			fvid = v.getId();
			assertNotNull(fvid);
		}

		final Exchanger<Exception> exchanger = new Exchanger<Exception>();
		final Integer o[] = {0, 1, 0, 1, 0, 1};
		final List<Integer> orders = new ArrayList<Integer>(asList(o));
		  
		final Editor e0 = new Editor(graph0, fvid, 0, exchanger, orders);
		final Editor e1 = new Editor(graph1, fvid, 1, exchanger, orders);
		new Thread(e0).start();
		new Thread(e1).start();

	    final Exception exception = exchanger.exchange(null);
	    graph0.shutdown();
	    if(graph0 != graph1) graph1.shutdown();
		
	    if(exception != null)
	    	throw exception;		
	}
	
	private static < T > List< T > asList(T[] array) {
        final List< T > list = new ArrayList< T >();
        for (T obj : array)
            list.add(obj);
        return list;
    }	

}
