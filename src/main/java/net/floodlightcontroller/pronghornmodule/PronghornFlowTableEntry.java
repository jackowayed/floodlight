package net.floodlightcontroller.pronghornmodule;

import java.util.ArrayList;
import java.util.List;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.OFType;

import org.slf4j.Logger;

import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.staticflowentry.IStaticFlowEntryPusherService;

public class PronghornFlowTableEntry
{
    public final static short DEFAULT_PRIORITY = 32767;
    
    public String switch_id = null;
    public String entry_name = null;
    public boolean active = false;
    public String actions = null;
    public Short priority = DEFAULT_PRIORITY;

    
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


    public OFFlowMod produce_flow_mod_msg(
        int xid,
        IFloodlightProviderService floodlight_provider,
        IStaticFlowEntryPusherService flow_entry_pusher,
        Logger log, boolean add)  throws IllegalArgumentException 
    {
        OFFlowMod flow_mod_msg =
            (OFFlowMod)floodlight_provider.getOFMessageFactory().getMessage(OFType.FLOW_MOD);
        flow_mod_msg.setXid(xid);

        flow_entry_pusher.initDefaultFlowMod(flow_mod_msg, entry_name);

        // this message is not a response to any packet in.
        flow_mod_msg.setBufferId(-1);

        if (add)
            flow_mod_msg.setCommand(OFFlowMod.OFPFC_ADD);
        else
            flow_mod_msg.setCommand(OFFlowMod.OFPFC_DELETE_STRICT);

        // I don't know that this is valid: essentially, I don't want
        // to be informed when this field is removed.
        flow_mod_msg.setFlags((short)0);

        // entries will never disappear on their own from flow table.
        flow_mod_msg.setHardTimeout((short)0);
        flow_mod_msg.setIdleTimeout((short)0);

        // set match
        flow_mod_msg.setMatch(construct_match());

        // set output port: applies deletes to all output ports
        flow_mod_msg.setOutPort(OFPort.OFPP_NONE);

        // set priority
        flow_mod_msg.setPriority(priority);

        // set actions
        flow_entry_pusher.parseActionString(
            flow_mod_msg, actions, log);

        return flow_mod_msg;
    }


    private OFMatch construct_match() throws IllegalArgumentException 
    {
        // generate a list key-value pairs.  Then, creates a
        // comma-separated list out of these key-value pairs.
        List<String> match_string_as_list = new ArrayList<String>();
        if (ingress_port != null)
        {
            match_string_as_list.add(
                OFMatch.STR_IN_PORT + "=" + ingress_port);
        }
        
        if (src_mac_address != null)
        {
            match_string_as_list.add(
                OFMatch.STR_DL_SRC + "=" + src_mac_address);
        }

        if (dst_mac_address != null)
        {
            match_string_as_list.add(
                OFMatch.STR_DL_DST + "=" + dst_mac_address);
        }

        if (vlan_id != null)
        {
            match_string_as_list.add(
                OFMatch.STR_DL_VLAN + "=" + vlan_id);
        }

        if (vlan_priority != null)
        {
            match_string_as_list.add(
                OFMatch.STR_DL_VLAN_PCP + "=" + vlan_priority);
        }

        if (ether_type != null)
        {
            match_string_as_list.add(
                OFMatch.STR_DL_TYPE + "=" + ether_type);
        }

        if (tos_bits != null)
        {
            match_string_as_list.add(
                OFMatch.STR_NW_TOS + "=" + tos_bits);
        }

        if (protocol != null)
        {
            match_string_as_list.add(
                OFMatch.STR_NW_PROTO + "=" + protocol);
        }

        if (ip_src != null)
        {
            match_string_as_list.add(
                OFMatch.STR_NW_SRC + "=" + ip_src);
        }

        if (ip_dst != null)
        {
            match_string_as_list.add(
                OFMatch.STR_NW_DST + "=" + ip_dst);
        }
        
        if (src_port != null)
        {
            match_string_as_list.add(
                OFMatch.STR_TP_SRC + "=" + src_port);
        }

        if (dst_port != null)
        {
            match_string_as_list.add(
                OFMatch.STR_TP_DST + "=" + dst_port);
        }

        StringBuffer match_string = new StringBuffer();
        for (int i = 0; i < match_string_as_list.size(); ++i)
        {
            match_string.append(match_string_as_list.get(i));
            if (i != (match_string_as_list.size() - 1))
                match_string.append(",");
        }

        OFMatch match = new OFMatch();
        match.fromString(match_string.toString());
        return match;
    }
}