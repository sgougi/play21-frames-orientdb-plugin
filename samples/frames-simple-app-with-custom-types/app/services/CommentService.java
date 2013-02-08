package services;

import java.util.Date;

import models.formdata.*;
import models.incidences.HasComment;
import models.vertices.Comment;
import models.vertices.Log;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.FramedGraph;
import com.wingnest.play2.frames.GraphDB;


public class CommentService {

	public static CommentService getInstance() {
		return CommentServiceHolder.INSTANCE;
	}

	//

	public Comment create(CommentData commentData, Log log, boolean disupdateFlagLog) {
		OrientGraph graph = GraphDB.getGraph();
		graph.getRawGraph().setUseCustomTypes(true);
		final FramedGraph<Graph> fg = GraphDB.createFramedGraph();
		final Date nowDate = new Date();
		Comment comment = fg.addVertex("class:Comment", Comment.class);

		/* comment */{
			comment.setClassName(Comment.class.getSimpleName());
			comment.setName(commentData.name);
			comment.setContent(commentData.content);
			comment.setCreatedDate(nowDate);
			comment.setUpdatedDate(nowDate);
		}
		/* log */{
			@SuppressWarnings("unused")
			HasComment hasComment = fg.addEdge("class:HasComment", log.asVertex(), comment.asVertex(), "hasComment", Direction.OUT, HasComment.class);
			if ( !disupdateFlagLog )
				log.setUpdatedDate(nowDate);
		}
		return comment;
	}

	// /

	private CommentService() {
	}

	private static class CommentServiceHolder {
		private static final CommentService INSTANCE = new CommentService();
	}

}
