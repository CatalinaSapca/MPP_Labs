package network.utils;

import network.rpcprotocol.TicketSystemClientRpcReflectionWorker;
import services.TicketSystemServices;

import java.net.Socket;


public class TicketSystemRpcConcurrentServer extends AbsConcurrentServer {
    private TicketSystemServices ticketSystemServer;
    public TicketSystemRpcConcurrentServer(int port, TicketSystemServices ticketSystemServer) {
        super(port);
        this.ticketSystemServer = ticketSystemServer;
        System.out.println("TicketSystem- TicketSystemRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        TicketSystemClientRpcReflectionWorker worker=new TicketSystemClientRpcReflectionWorker(ticketSystemServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
