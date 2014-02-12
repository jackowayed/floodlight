package net.floodlightcontroller.pronghornmodule;

import net.floodlightcontroller.core.module.IFloodlightService;

public interface IPronghornService extends IFloodlightService {
    public String sendBarrier(String switchId);
}
