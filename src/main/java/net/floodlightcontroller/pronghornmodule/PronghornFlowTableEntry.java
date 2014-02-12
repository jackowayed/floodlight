package net.floodlightcontroller.pronghornmodule;


public class PronghornFlowTableEntry
{
    public String switch_id = null;
    public String entry_name = null;
    public boolean active = false;
    public String actions = null;

    public PronghornFlowTableEntry(
        String switch_id,String entry_name,boolean active,
        String actions)
    {
        this.switch_id = switch_id;
        this.entry_name = entry_name;
        this.active = active;
        this.actions = actions;
    }
            
}