package net.floodlightcontroller.pronghornmodule;


public class PronghornFlowTableEntry
{
    public final static int DEFAULT_PRIORITY = 32767;
    
    public String switch_id = null;
    public String entry_name = null;
    public boolean active = false;
    public String actions = null;
    public Integer priority = DEFAULT_PRIORITY;

    
    public Integer ingress_port = null;
    public String src_mac_address = null;
    public String dst_mac_address = null;
    public Integer vlan_id = null;
    public Integer vlan_priority = null;
    public Integer ether_type = null;
    public Integer tos_bits = null;
    public Integer protocol = null;

    public String ip_src = null;
    public String ip_dst = null;

    public Integer src_port = null;
    public Integer dst_port = null;

    
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