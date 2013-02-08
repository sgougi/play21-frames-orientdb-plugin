import com.tinkerpop.blueprints.Vertex;
import com.wingnest.play2.frames.GraphDB;

import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {

	public void onStart(Application app) {

		try {
			GraphDB.createKeyIndex("className", Vertex.class);
		} catch (java.lang.UnsupportedOperationException e){
			Logger.info(e.getMessage());
		}

	}
	
}
