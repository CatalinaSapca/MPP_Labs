using System;
using System.Threading;
using System.Net.Sockets;

using Chat.Protocol;
using Google.Protobuf;
using services;
using System.Collections.Generic;

namespace protobuf
{
	public class ProtoTSWorker : ITicketSystemObserver
	{
		private ITicketSystemServices server;
		private TcpClient connection;

		private NetworkStream stream;
		private volatile bool connected;
		public ProtoTSWorker(ITicketSystemServices server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{

				stream = connection.GetStream();
				connected = true;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}

		public virtual void run()
		{
			while (connected)
			{
				try
				{

					TSRequest request = TSRequest.Parser.ParseDelimitedFrom(stream);
					TSResponse response = handleRequest(request);
					if (response != null)
					{
						sendResponse(response);
					}
				}
				catch (Exception e)
				{
					Console.WriteLine(e.StackTrace);
				}

				try
				{
					Thread.Sleep(1000);
				}
				catch (Exception e)
				{
					Console.WriteLine(e.StackTrace);
				}
			}
			try
			{
				stream.Close();
				connection.Close();
			}
			catch (Exception e)
			{
				Console.WriteLine("Error " + e);
			}
		}

		public void boughtTicket(model.ArtisticRepresentation artisticRepresentation)
		{
			Console.WriteLine("Someone bought tickets " + artisticRepresentation);
			try
			{
				sendResponse(ProtoUtils.createBoughtTicketResponse(artisticRepresentation));
			}
			catch (Exception e)
			{
				throw new ServicesException("Sending error: " + e);
			}
		}

		private TSResponse handleRequest(TSRequest request)
		{
			TSResponse response = null;
			TSRequest.Types.Type reqType = request.Type;
			switch (reqType)
			{
				case TSRequest.Types.Type.Login:
					{
						Console.WriteLine("Login request ...");
						model.Seller seller = ProtoUtils.getSeller(request);
						try
						{
							
							lock (server)
							{
								model.Seller seller1;
								seller1 = server.login(seller, this);
								return ProtoUtils.createLoginResponse(seller1);
							}
							
						}
						catch (ServicesException e)
						{
							connected = false;
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case TSRequest.Types.Type.Logout:
					{
						Console.WriteLine("Logout request");
						model.Seller seller = ProtoUtils.getSeller(request);
						try
						{
							lock (server)
							{

								server.logout(seller, this);
							}
							connected = false;
							return ProtoUtils.createLogoutResponse();

						}
						catch (ServicesException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case TSRequest.Types.Type.AddBuyer:
					{
						Console.WriteLine("AddBuyer request");
						model.Buyer buyer = ProtoUtils.getBuyer(request);
						try
						{
							model.Buyer buyer1;
							lock (server)
							{
								buyer1 = server.addBuyer(buyer);
							}
							return ProtoUtils.createAddBuyerResponse(buyer1);

						}
						catch (ServicesException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case TSRequest.Types.Type.BuyTicket:
					{
						Console.WriteLine("BuyTicket request");
						model.ArtisticRepresentation artisticRepresentation = ProtoUtils.getArtisticRepresentation(request);
						try
						{
							model.ArtisticRepresentation ar1;
							lock (server)
							{
								ar1 = server.updateAR(artisticRepresentation, this);
							}
							return ProtoUtils.createBuyTicketResponse(ar1);

						}
						catch (ServicesException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case TSRequest.Types.Type.GetAllAr:
					{
						Console.WriteLine("GetAllAR request");
						try
						{
							IEnumerable<model.ArtisticRepresentation> ars;
							lock (server)
							{
								ars = server.getAllAR();
							}
							return ProtoUtils.createGetAllARResponse(ars);

						}
						catch (ServicesException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case TSRequest.Types.Type.GetAllArfromDate:
					{
						Console.WriteLine("GetAllARFromDate request");
						model.ArtisticRepresentation artisticRepresentation = ProtoUtils.getArtisticRepresentation(request);
						try
						{
							IEnumerable<model.ArtisticRepresentation> ars;
							lock (server)
							{
								ars = server.getAllARFromDate(artisticRepresentation.Data);
							}
							return ProtoUtils.createGetAllARFromDateResponse(ars);

						}
						catch (ServicesException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case TSRequest.Types.Type.FindSellerByUsername:
					{
						Console.WriteLine("FindSellerByUsername request");
						model.Seller seller = ProtoUtils.getSeller(request);
						try
						{
							model.Seller seller1;
							lock (server)
							{
								seller1 = server.findSellerByUsername(seller);
							}
							return ProtoUtils.createFindSellerByUsernameResponse(seller1);

						}
						catch (ServicesException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}



			}
			return response;
		}

		private void sendResponse(TSResponse response)
		{
			Console.WriteLine("sending response " + response);
			lock (stream)
			{
				response.WriteDelimitedTo(stream);
				stream.Flush();
			}

		}
	}



}
