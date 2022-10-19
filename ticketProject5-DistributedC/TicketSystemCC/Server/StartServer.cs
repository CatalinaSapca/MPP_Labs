using System;
using System.Net.Sockets;

using System.Threading;
using model.Validators;
using persistance.Database;
using persistance.Interfaces;
using networking;
using services;
using ServerTemplate;

namespace Server
{
    //using server;
    class StartServer
    {
        static void Main(string[] args)
        {
            IARRepository arRepo = new ARDatabase(new ARValidator());
            ISellerRepository sellerRepo = new SellerDatabase(new SellerValidator());
            IBuyerRepository buyerRepo = new BuyerDatabase(new BuyerValidator());

            ITicketSystemServices serviceImpl = new TicketServerImpl(arRepo, buyerRepo, sellerRepo);

            SerialChatServer server = new SerialChatServer("127.0.0.1", 55555, serviceImpl);
            server.Start();
            Console.WriteLine("Server started ...");
            Console.WriteLine("Press <enter> to exit...");
            Console.ReadLine();

        }
    }

    public class SerialChatServer : ConcurrentServer
    {
        private ITicketSystemServices server;
        private TicketClientWorker worker;
        public SerialChatServer(string host, int port, ITicketSystemServices server) : base(host, port)
        {
            this.server = server;
            Console.WriteLine("TicketSystemServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new TicketClientWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }

}
