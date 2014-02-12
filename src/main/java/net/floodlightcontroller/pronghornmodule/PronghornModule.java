package net.floodlightcontroller.pronghornmodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.openflow.util.HexString;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.restserver.IRestApiService;

public class PronghornModule
    implements IFloodlightModule, IOFMessageListener, IPronghornService
{
        
    protected IFloodlightProviderService floodlightProvider;
    protected IRestApiService restApi;
    protected ConcurrentHashMap<IOFSwitch, BlockingQueue<OFMessage>> queues;

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices()
    {
        Collection<Class<? extends IFloodlightService>> l =
            new ArrayList<Class<? extends IFloodlightService>>();
        l.add(IPronghornService.class);
        return l;
    }       

    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls()
    {
        Map<Class<? extends IFloodlightService>, IFloodlightService> m =
            new HashMap<Class<? extends IFloodlightService>, IFloodlightService>();
        m.put(IPronghornService.class, this);
        return m;
    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleDependencies()
    {
        Collection<Class<? extends IFloodlightService>> l =
            new ArrayList<Class<? extends IFloodlightService>>();
        l.add(IFloodlightProviderService.class);
        l.add(IRestApiService.class);
        return l;
    }

    @Override
    public void init(FloodlightModuleContext context)
        throws FloodlightModuleException {
        floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
        restApi = context.getServiceImpl(IRestApiService.class);
        queues = new ConcurrentHashMap<IOFSwitch, BlockingQueue<OFMessage>>();
    }

    @Override
    public void startUp(FloodlightModuleContext context) {
        floodlightProvider.addOFMessageListener(OFType.BARRIER_REPLY, this);
        restApi.addRestletRoutable(new PronghornWebRoutable());
    }
    
    @Override
    public String getName() {
        return "PronghornModule";
    }

    @Override
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* Create queue for sw if does not exist */
    private void ensureQueueExists(IOFSwitch sw) {
        if (!queues.contains(sw)) {
            queues.put(sw, new LinkedBlockingQueue<OFMessage>());
        }
    }

    @Override
    public net.floodlightcontroller.core.IListener.Command receive(
        IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        // TODO do stuff
        //System.out.println(sw + "-->" + msg);
        if (msg.getType() == OFType.BARRIER_REPLY && queues.get(sw) != null) {
            try {
                queues.get(sw).put(msg);;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Command.CONTINUE;
    }

    
    @Override
    public String sendBarrier(String switch_id)
    {
        if (send_barrier(switch_id))
            return "true";
        return "false";
    }


    
    @Override
    public int add_entry (
        PronghornFlowTableEntry entry,IPronghornFlowtableCallback cb)
    {
        // FIXME: Must fill in
        return -1;
    }
    @Override
    public int remove_entry (
        PronghornFlowTableEntry entry,IPronghornFlowtableCallback cb)
    {
        // FIXME: Must fill in
        return -1;
    }
    @Override
    public void barrier (
        String switch_id,IPronghornBarrierCallback cb)
    {
        // FIXME: Must fill in
    }
    
    
    // TODO actually handle the failures better.
    private boolean send_barrier(String switchId)
    {
        long id = HexString.toLong(switchId);
        // send barrier request
        IOFSwitch sw = floodlightProvider.getSwitch(id);
        OFMessage barrierReq =
            floodlightProvider.getOFMessageFactory().getMessage(OFType.BARRIER_REQUEST);
        
        ensureQueueExists(sw);
        int xid = sw.getNextTransactionId();
        barrierReq.setXid(xid);
        try {
            sw.write(barrierReq, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        // block until barrier reply
        OFMessage barrierResp;
        try {
            barrierResp = queues.get(sw).poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        if (barrierResp == null) {
            System.out.println("no response (timed out)");
            return false;
        }
        return true;
    }
}
