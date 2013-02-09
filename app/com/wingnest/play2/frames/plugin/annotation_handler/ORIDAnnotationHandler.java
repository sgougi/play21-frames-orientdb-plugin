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
package com.wingnest.play2.frames.plugin.annotation_handler;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.annotations.AnnotationHandler;
import com.wingnest.play2.frames.annotations.ORID;

public class ORIDAnnotationHandler implements AnnotationHandler<ORID> {

	@Override
	public Class<ORID> getAnnotationType() {
		return ORID.class;
	}

	@Override
	public Object processVertex(final ORID annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Vertex element) {
		final VertexFrame frame = (VertexFrame) framedGraph.frame(element, VertexFrame.class);
		return frame.asVertex().getId();
	}

	@Override
	public Object processEdge(final ORID annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Edge element, final Direction direction) {
		final EdgeFrame frame = (EdgeFrame) framedGraph.frame(element, direction, EdgeFrame.class);
		return frame.asEdge().getId();
	}

}
