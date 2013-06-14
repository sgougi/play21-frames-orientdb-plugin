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

import com.orientechnologies.orient.core.id.ORID;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.annotations.AnnotationHandler;
import com.wingnest.play2.frames.OrientDB;
import com.wingnest.play2.frames.annotations.CustomId;

public class CustomIdAnnotationHandler implements AnnotationHandler<CustomId> {

	@Override
	public Object processElement(final CustomId annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Element element, final Direction direction) {
		if( element instanceof Vertex ) {
			return _processVertex(annotation, method, arguments, framedGraph, (Vertex)element);
		} else {
			return _processEdge(annotation, method, arguments, framedGraph, (Edge)element, direction);
		}
	}
	
	@Override
	public Class<CustomId> getAnnotationType() {
		return CustomId.class;
	}

	public Object _processVertex(final CustomId annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Vertex element) {
		return OrientDB.fromORIDToCustomId((ORID) element.getId());
	}

	public Object _processEdge(final CustomId annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph, final Edge element, final Direction direction) {
		return OrientDB.fromORIDToCustomId((ORID) element.getId());
	}

}
