package net.floodlightcontroller.pronghornmodule;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import net.floodlightcontroller.restserver.RestletRoutable;

public class PronghornWebRoutable implements RestletRoutable {

	@Override
	public Restlet getRestlet(Context context) {
		Router router = new Router(context);
		router.attach("/switch/{switch}/barrier/json", PronghornResource.class);
		return router;
	}

	@Override
	public String basePath() {
		return "/wm/pronghorn";
	}

}
