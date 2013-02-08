package models.incidences;

import models.vertices.Comment;
import models.vertices.Log;

import com.tinkerpop.frames.Domain;
import com.tinkerpop.frames.Range;

public interface HasComment extends EdgeBase {

	@Domain
	public Log getDomain();

	@Range
	public Comment getComment();

}
