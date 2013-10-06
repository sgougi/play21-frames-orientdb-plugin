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
package com.wingnest.play2.frames.plugin.orientdb;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import play.Application;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.modules.MethodHandler;
import com.wingnest.play2.frames.GraphDB;
import com.wingnest.play2.frames.annotations.CustomEdge;
import com.wingnest.play2.frames.annotations.CustomVertex;
import com.wingnest.play2.frames.plugin.FramesLogger;
import com.wingnest.play2.frames.plugin.PluginBase;
import com.wingnest.play2.frames.plugin.framedgraph.DefaultFramedGraphDirector;
import com.wingnest.play2.frames.plugin.framedgraph.FramedGraphDirector;
import com.wingnest.play2.frames.plugin.method_handler.CustomIdMethodHandler;
import com.wingnest.play2.frames.plugin.method_handler.ORIDMethodHandler;
import com.wingnest.play2.frames.plugin.utils.TypeUtils;

public class FramesOrientDBPlugin extends PluginBase {
	
	public static final Map<String, Class<?>> graphEntityMap = new HashMap<String, Class<?>>();
		
	@Override
	protected void onEndStart() {
		registerGraphClasses();
	}

	public FramesOrientDBPlugin(final Application application) {
		super(application);	
	}

	@Override
	protected <T extends FramedGraph<? extends Graph>> FramedGraphDirector<T> createFramedGraphDirector() {
		return (FramedGraphDirector<T>)new DefaultFramedGraphDirector(new OrientDBGraphManager());
	}
	
	protected void onRegisterAnnotations(final Set<MethodHandler<? extends Annotation>> methodHandlers) {
		methodHandlers.add(new CustomIdMethodHandler());
		methodHandlers.add(new ORIDMethodHandler());
	}	

	private void registerGraphClasses() {
		FramesLogger.debug("registerGraphClasses");
		final OrientGraph graph = GraphDB.getGraph();
		final Set<Class<?>> customVertexClasses = TypeUtils.getTypesAnnotatedWith(application, "models", CustomVertex.class);
		maintainClasses(graph, customVertexClasses, true);
		final Set<Class<?>> customEdgeClasses = TypeUtils.getTypesAnnotatedWith(application, "models", CustomEdge.class);
		maintainClasses(graph, customEdgeClasses, false);
	}
	
	private void maintainClasses(OrientGraph graph, Set<Class<?>> javaClasses, boolean bForVertex) {
		final OSchema schema = graph.getRawGraph().getMetadata().getSchema();
		for ( final Class<?> javaClass : javaClasses ) {
			final String entityName = javaClass.getSimpleName();
			{
				if ( !schema.existsClass(entityName) ) {
					if(bForVertex) {
						FramesLogger.debug("Custom Vertex: %s", entityName);
						graph.createVertexType(entityName);
					} else {
						FramesLogger.debug("Custom Edge: %s", entityName);
						graph.createEdgeType(entityName);
					}					
				}
				graphEntityMap.put(entityName, javaClass);
			}
		}
		for ( final Class<?> javaClass : javaClasses ) {
			final String entityName = javaClass.getSimpleName();
			final OClass oClass = schema.getClass(entityName);
			OClass baseClass = bForVertex ? schema.getClass("V"): schema.getClass("E");
			oClass.setSuperClass(baseClass);			
			final Set<Class<?>> parentClasses = new HashSet<Class<?>>();
			parentClasses.add(javaClass.getSuperclass());
			parentClasses.addAll(Arrays.asList(javaClass.getInterfaces()));												
			for ( final Class<?> sclass : parentClasses ) {
				if ( javaClasses.contains(sclass) ) {
					final OClass sClass = schema.getClass(sclass.getSimpleName());
					oClass.setSuperClass(sClass);
					break;
				}
			}
			maintainProperties(oClass, javaClass);										
		}
	}

	
	private void maintainProperties(OClass oClass, Class<?> javaClass) {
		// TODO 		
	}

	
}
