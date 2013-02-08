/*
 * Copyright since 2013 Shigeru GOUGI
 *                              e-mail:  sgougi@gmail.com
 *                              twitter: @igerugo
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

import com.orientechnologies.orient.core.id.ORID;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.annotations.AnnotationHandler;
import com.wingnest.play2.frames.OrientDB;
import com.wingnest.play2.frames.annotations.CustomId;

public class CustomIdAnnotationHandler implements AnnotationHandler<CustomId> {

	@Override
	public Class<CustomId> getAnnotationType() {
		return CustomId.class;
	}

	@Override
	public Object processVertex(final CustomId annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Vertex element) {
		final VertexFrame frame = (VertexFrame) framedGraph.frame(element, VertexFrame.class);
		return OrientDB.fromORIDToCustomId((ORID) frame.asVertex().getId());
	}

	@Override
	public Object processEdge(final CustomId annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Edge element, final Direction direction) {
		final EdgeFrame frame = (EdgeFrame) framedGraph.frame(element, direction, EdgeFrame.class);
		return OrientDB.fromORIDToCustomId((ORID) frame.asEdge().getId());
	}

}
