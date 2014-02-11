package services;

import java.util.Date;

import models.formdata.*;
import models.incidences.HasComment;
import models.vertices.Comment;
import models.vertices.Log;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;
import com.wingnest.play2.frames.GraphDB;


public class CommentService {
	
	private static final CommentService INSTANCE = new CommentService();

	public static CommentService getInstance() {
		return INSTANCE;
	}

	// /

	public Comment create(CommentData commentData, Log log, boolean disupdateFlagLog) {
		final FramedGraph<Graph> fg = GraphDB.createFramedGraph();

		final Date nowDate = new Date();
		//Comment comment = fg.addVertex(null, Comment.class);
		Comment comment = fg.addVertex("class:Comment", Comment.class);

		/* comment */{
			comment.setClassName(Comment.class.getSimpleName());
			comment.setName(commentData.name);
			comment.setContent(commentData.content);
			comment.setCreatedDate(nowDate);
			comment.setUpdatedDate(nowDate);
		}
		/* log */{
			log.addComment(comment);
			//HasComment hasComment = fg.addEdge("class:HasComment", log.asVertex(), comment.asVertex(), "hasComment", HasComment.class);
			//log.asVertex().addEdge("hasComment", comment.asVertex());
			if ( !disupdateFlagLog )
				log.setUpdatedDate(nowDate);
		}
		return comment;
	}

	// /

	private CommentService() {
	}

}
