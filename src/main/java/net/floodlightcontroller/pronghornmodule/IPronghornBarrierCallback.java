package net.floodlightcontroller.pronghornmodule;

public interface IPronghornBarrierCallback
{
    public void command_failure(int id);
    public void barrier_success();
    public void barrier_failure();
}