package net.floodlightcontroller.pronghornmodule;

import net.floodlightcontroller.core.module.IFloodlightService;

public interface IPronghornService extends IFloodlightService {
	public boolean sendBarrier(String switchId);
}
