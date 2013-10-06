package models.incidences;

import com.wingnest.play2.frames.EdgeFrameWithId;
import com.wingnest.play2.frames.annotations.*;

public interface EdgeBase extends EdgeFrameWithId {

	@CustomId
	public Object getCustomId();
	
}
