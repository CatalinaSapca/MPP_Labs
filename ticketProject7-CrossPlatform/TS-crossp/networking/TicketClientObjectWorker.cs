using System;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using services;
using model;
using networking.dto;
using System.Collections.Generic;

namespace networking
{
	public class TicketClientWorker : ITicketSystemObserver //, Runnable
	{
		private ITicketSystemServices server;
		private TcpClient connection;

		private NetworkStream stream;
		private IFormatter formatter;
		private volatile bool connected;
		public TicketClientWorker(ITicketSystemServices server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{

				stream = connection.GetStream();
				formatter = new BinaryFormatter();
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
					object request = formatter.Deserialize(stream);
					Console.WriteLine("sunt in run");
					object response = handleRequest((Request)request);
					if (response != null)
					{
						sendResponse((Response)response);
					}
				}
				catch (Exception e)
				{
					Console.WriteLine(e.Message);
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


		public virtual void boughtTicket(ArtisticRepresentation artisticRepresentation)
		{
			ArtisticRepresentationDTO arDTO = DTOUtils.getDTO(artisticRepresentation);
			Console.WriteLine("Someone bought tickets " + artisticRepresentation);
			try
			{
				sendResponse(new BoughtTicketResponse(arDTO));
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
				throw new ServicesException("Sending error: " + e);
			}
		}

		private Response handleRequest(Request request)
		{
			Response response = null;
			if (request is LoginRequest)
			{
				Console.WriteLine("Login request ...");
				LoginRequest logReq = (LoginRequest)request;
				SellerDTO sellerDTO = logReq.Seller;
				Seller seller = DTOUtils.getFromDTO(sellerDTO);
				try
				{
					Seller seller1;
					lock (server)
					{
						seller1 = server.login(seller, this);
					}
					return new LoginResponse(DTOUtils.getDTO(seller1));
				}
				catch (ServicesException e)
				{
					connected = false;
					return new ErrorResponse(e.Message);
				}
			}
			if (request is LogoutRequest)
			{
				Console.WriteLine("Logout request");
				LogoutRequest logReq = (LogoutRequest)request;
				SellerDTO sellerDTO = logReq.Seller;
				Seller seller = DTOUtils.getFromDTO(sellerDTO);
				try
				{
					lock (server)
					{
						server.logout(seller, this);
					}

					connected = false;
					return new OkResponse();

				}
				catch (ServicesException e)
				{
					return new ErrorResponse(e.Message);
				}
			}
			if (request is BuyTicketRequest)
			{
				Console.WriteLine("BuyTicketRequest ...");
				BuyTicketRequest buyTicketRequestReq = (BuyTicketRequest)request;
				ArtisticRepresentationDTO arDTO = buyTicketRequestReq.ArtisticRepresentation;
				ArtisticRepresentation ar = DTOUtils.getFromDTO(arDTO);
				try
				{
					ArtisticRepresentation arr;
					lock (server)
					{
						arr = server.updateAR(ar, this);
					}
					return new BuyTicketResponse(DTOUtils.getDTO(arr));
				}
				catch (ServicesException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is AddBuyerRequest)
			{
				Console.WriteLine("AddBuyerRequest ...");
				AddBuyerRequest addBuyerRequest = (AddBuyerRequest)request;
				BuyerDTO buyerDTO = addBuyerRequest.Buyer;
				Buyer buyer = DTOUtils.getFromDTO(buyerDTO);
				try
				{
					Buyer buyer1;
					lock (server)
					{
						buyer1 = server.addBuyer(buyer);
					}
					return new AddBuyerResponse(DTOUtils.getDTO(buyer1));
				}
				catch (ServicesException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is GetAllARRequest)
			{
				Console.WriteLine("GetAllAR Request ...");
				GetAllARRequest getAllARRequest = (GetAllARRequest)request;
				try
				{
					IEnumerable<ArtisticRepresentation> ars;
					IEnumerable<ArtisticRepresentationDTO> arsDTO;
					lock (server)
					{
						Console.WriteLine("sunt aici");
						ars = server.getAllAR();
						Console.WriteLine("sunt aici2");
						arsDTO = DTOUtils.getDTO(ars);
					}
					return new GetAllARResponse(arsDTO);
				}
				catch (ServicesException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is GetAllARFromDateRequest)
			{
				Console.WriteLine("GetAllARFromDate Request ...");
				GetAllARFromDateRequest getReq = (GetAllARFromDateRequest)request;
				DateTime date = getReq.Date;
				try
				{
					IEnumerable<ArtisticRepresentation> ars;
					IEnumerable<ArtisticRepresentationDTO> arsDTO;
					lock (server)
					{
						ars = server.getAllARFromDate(date);
						arsDTO = DTOUtils.getDTO(ars);
					}
					return new GetAllARFromDateResponse(arsDTO);
				}
				catch (ServicesException e)
				{
					return new ErrorResponse(e.Message);
				}
			}
			return response;
		}


		private void sendResponse(Response response)
		{
			Console.WriteLine("sending response " + response);
			formatter.Serialize(stream, response);
			stream.Flush();

		}
	}

}