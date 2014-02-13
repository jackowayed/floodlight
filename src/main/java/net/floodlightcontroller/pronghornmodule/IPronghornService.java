package net.floodlightcontroller.pronghornmodule;

import java.io.IOException;

import net.floodlightcontroller.core.module.IFloodlightService;


public interface IPronghornService extends IFloodlightService {
    public String sendBarrier(String switchId);

    /**
       Returns unique id associated with 
     */
    public int add_entry (
        PronghornFlowTableEntry entry) throws IOException;
    public int remove_entry (
        PronghornFlowTableEntry entry) throws IOException;
    public void barrier (
        String switch_id,IPronghornBarrierCallback cb) throws IOException;
    
}
