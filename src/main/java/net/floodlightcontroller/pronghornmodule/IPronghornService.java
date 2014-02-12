package net.floodlightcontroller.pronghornmodule;

import net.floodlightcontroller.core.module.IFloodlightService;

public interface IPronghornService extends IFloodlightService {
    public String sendBarrier(String switchId);

    /**
       Returns unique id associated with 
     */
    public int add_entry (
        PronghornFlowTableEntry entry);
    public int remove_entry (
        PronghornFlowTableEntry entry);
    public void barrier (
        String switch_id,IPronghornBarrierCallback cb);
    
}
