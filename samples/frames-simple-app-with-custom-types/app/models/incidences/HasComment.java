package models.incidences;

import java.util.Date;

import models.vertices.Comment;
import models.vertices.Log;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.wingnest.play2.frames.annotations.DateProperty;

public interface HasComment extends EdgeBase {

	@InVertex
	public Log getDomain();

	@OutVertex
	public Comment getComment();

	@DateProperty("createdDate")
	public void setCreatedDate(Date createdDate);

	@DateProperty("createdDate")
	public Date getCreatedDate();
	
}
