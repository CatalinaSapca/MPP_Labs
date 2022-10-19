package network.utils;


import network.objectprotocol.TicketSystemClientObjectWorker;
import services.TicketSystemServices;

import java.net.Socket;


public class TicketSystemObjectConcurrentServer extends AbsConcurrentServer {
    private TicketSystemServices ticketSystemServer;
    public TicketSystemObjectConcurrentServer(int port, TicketSystemServices ticketSystemServer) {
        super(port);
        this.ticketSystemServer = ticketSystemServer;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TicketSystemClientObjectWorker worker=new TicketSystemClientObjectWorker(ticketSystemServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
