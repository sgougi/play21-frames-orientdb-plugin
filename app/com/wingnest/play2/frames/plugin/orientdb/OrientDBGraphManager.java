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

import static com.wingnest.play2.frames.plugin.orientdb.ConfigConsts.CONF_ORIENT_DB_CONFIG_FILE;
import static com.wingnest.play2.frames.plugin.orientdb.ConfigConsts.CONF_ORIENT_DB_PASSWORD;
import static com.wingnest.play2.frames.plugin.orientdb.ConfigConsts.CONF_ORIENT_DB_URL;
import static com.wingnest.play2.frames.plugin.orientdb.ConfigConsts.CONF_ORIENT_DB_USER;
import static com.wingnest.play2.frames.plugin.orientdb.ConfigConsts.CONF_ORIENT_DB_WWW_PATH;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import play.Play;

import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.wingnest.play2.frames.plugin.FramesLogger;
import com.wingnest.play2.frames.plugin.exceptions.FramesUnexpectedException;
import com.wingnest.play2.frames.plugin.graphManager.AbstractGraphManager;

public class OrientDBGraphManager extends AbstractGraphManager {

	private static final String ORIENTDB_WWW_PATH = "orientdb.www.path";

	private OServer server;
	final private OrientGraph graph;

	public OrientDBGraphManager() {
		OGlobalConfiguration.CACHE_LEVEL1_ENABLED.setValue(false);				
		graph = createGraph();
	}

	private OrientGraph createGraph() {
		final String url = getConfigString(CONF_ORIENT_DB_URL, "memory:temp");
		if ( !url.startsWith("remote") )
			runEmbeddedOrientDB();
		return new OrientGraph(url);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Graph> T getGraph() {
		return (T) graph;
	}

	@Override
	public void startTransaction() {
		ODatabaseRecordThreadLocal.INSTANCE.set( ((OrientGraph)graph).getRawGraph() );
	}
	
	@Override
	public void stopTransaction(Conclusion conclusion) {
		graph.stopTransaction(conclusion);
	}	
	
	@Override
	public void commit() {
		graph.commit();
	}

	@Override
	public void rollback() {
		graph.rollback();
	}		

	private void runEmbeddedOrientDB() {
		if ( server != null ) return;
		FileInputStream fis = null;
		try {
			/* orient server */{
				final String cfile = getConfigString(CONF_ORIENT_DB_CONFIG_FILE, null);
				final InputStream is;
				if ( cfile != null ) {
					final File aFile = new File(cfile);
					FramesLogger.info("db.config in application was used : " + aFile.getAbsolutePath());
					fis = new FileInputStream(new File(cfile));
					is = fis;
				} else {
					FramesLogger.info("default db.config in Frames Plugin was used");
					is = FramesOrientDBPlugin.class.getClassLoader().getResourceAsStream("db.config");
				}
				server = OServerMain.create();
				server.startup(is);
				FramesLogger.info("OrientDB of embedded mode was started");
			}
			/* web server */{
				final String orientDBWwwPath = getConfigString(CONF_ORIENT_DB_WWW_PATH, null);
				if ( System.getProperty(ORIENTDB_WWW_PATH) == null && orientDBWwwPath != null ) {
					final File wwwPath = new File(orientDBWwwPath);
					if ( !wwwPath.exists() || !wwwPath.isDirectory() ) {
						final String mes = String.format("www directory not found : %s", wwwPath.getAbsolutePath());
						FramesLogger.error(mes);
						throw new IllegalStateException(mes);
					}
					System.setProperty(ORIENTDB_WWW_PATH, wwwPath.getCanonicalPath());

				}
				if ( System.getProperty(ORIENTDB_WWW_PATH) != null ) {
					server.activate();
					FramesLogger.info("WWW Server was just activated : %s", System.getProperty(ORIENTDB_WWW_PATH));
				} else {
					FramesLogger.info("WWW Server is not activated : application.conf's %s property was not provided", CONF_ORIENT_DB_WWW_PATH);
				}
			}
		} catch ( Exception e ) {
			throw new FramesUnexpectedException(e);
		} finally {
			if ( fis != null )
				try {
					fis.close();
				} catch ( Exception dummy ) {
				}
		}
	}

	@Override
	public void onShutdown() {
		serverShutdown();
		if(graph != null)
			graph.shutdown();
		final Orient orient = Orient.instance();
		if(orient != null)
			 orient.shutdown();
	}
	
	private void serverShutdown() {
		if ( server != null ) {
			FramesLogger.info("WWW Server was just shutdowned");
			server.shutdown();
			server = null;
		}
	}

	private static String getConfigString(final String propName, final String defaultVal) {
		final String val = Play.application().configuration().getString(propName);
		return val == null ? defaultVal : val;
	}
}
