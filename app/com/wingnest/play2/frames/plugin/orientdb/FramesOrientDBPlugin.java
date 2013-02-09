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
import java.util.Set;

import play.Application;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.annotations.AnnotationHandler;
import com.wingnest.play2.frames.plugin.PluginBase;
import com.wingnest.play2.frames.plugin.annotation_handler.CustomIdAnnotationHandler;
import com.wingnest.play2.frames.plugin.annotation_handler.ORIDAnnotationHandler;
import com.wingnest.play2.frames.plugin.framedgraph.DefaultFramedGraphDirector;
import com.wingnest.play2.frames.plugin.framedgraph.FramedGraphDirector;

public class FramesOrientDBPlugin extends PluginBase {
	
	public FramesOrientDBPlugin(final Application application) {
		super(application);	
	}

	@Override
	protected <T extends FramedGraph<? extends Graph>> FramedGraphDirector<T> createFramedGraphDirector() {
		return (FramedGraphDirector<T>)new DefaultFramedGraphDirector(new OrientDBGraphManager());
	}
	
	protected void onRegisterAnnotations(final Set<AnnotationHandler<? extends Annotation>> annotationHandlers) {
		annotationHandlers.add(new CustomIdAnnotationHandler());
		annotationHandlers.add(new ORIDAnnotationHandler());
	}	
		
}
