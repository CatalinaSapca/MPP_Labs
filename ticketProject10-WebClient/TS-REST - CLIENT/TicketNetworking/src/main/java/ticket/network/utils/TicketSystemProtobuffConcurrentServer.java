package ticket.network.utils;

import services.TicketSystemServices;
import ticket.network.protobuffprotocol.ProtoTSWorker;

import java.net.Socket;


public class TicketSystemProtobuffConcurrentServer extends AbsConcurrentServer {
    private TicketSystemServices server;
    public TicketSystemProtobuffConcurrentServer(int port, TicketSystemServices server) {
        super(port);
        this.server = server;
        System.out.println("TicketSystem- TicketSystemProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoTSWorker worker = new ProtoTSWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}