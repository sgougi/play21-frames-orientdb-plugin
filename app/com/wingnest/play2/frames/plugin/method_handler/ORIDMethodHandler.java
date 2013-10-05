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
package com.wingnest.play2.frames.plugin.method_handler;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.MethodHandler;
import com.wingnest.play2.frames.annotations.ORID;

public class ORIDMethodHandler implements MethodHandler<ORID> {

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final ORID annotation, final FramedGraph<?> framedGraph, final Element element) {
		if( element instanceof Vertex) {
			return _processVertex(annotation, method, arguments, framedGraph, (Vertex)element);
		} else {
			return _processEdge(annotation, method, arguments, framedGraph, (Edge)element);
		}	
	}
	
	@Override
	public Class<ORID> getAnnotationType() {
		return ORID.class;
	}

	private Object _processVertex(final ORID annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Vertex element) {
		final VertexFrame frame = (VertexFrame) framedGraph.frame(element, VertexFrame.class);
		return frame.asVertex().getId();
	}

	private Object _processEdge(final ORID annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Edge element) {
		final EdgeFrame frame = (EdgeFrame) framedGraph.frame(element, EdgeFrame.class);
		return frame.asEdge().getId();
	}

}
