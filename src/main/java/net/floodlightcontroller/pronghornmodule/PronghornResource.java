package net.floodlightcontroller.pronghornmodule;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class PronghornResource extends ServerResource {
	@Post
	public boolean barrier() {
        IPronghornService prong = (IPronghornService)getContext().getAttributes().get(IPronghornService.class.getCanonicalName());
        String param = (String) getRequestAttributes().get("switch");
        return prong.sendBarrier(param);
	}
}
