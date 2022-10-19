using System;
using System.Collections.Generic;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using services;
using networking.dto;
using model;

namespace networking
{

	public class TicketServerProxy : ITicketSystemServices
	{
		private string host;
		private int port;

		private ITicketSystemObserver client;

		private NetworkStream stream;

		private IFormatter formatter;
		private TcpClient connection;

		private Queue<Response> responses;
		private volatile bool finished;
		private EventWaitHandle _waitHandle;
		public TicketServerProxy(string host, int port)
		{
			this.host = host;
			this.port = port;
			responses = new Queue<Response>();
		}

		public Seller login(Seller seller, ITicketSystemObserver client)
		{
			initializeConnection();
			Console.WriteLine("proxy login");
			SellerDTO sellerDTO = DTOUtils.getDTO(seller);
			sendRequest(new LoginRequest(sellerDTO));
			Response response = readResponse();
			if (response is LoginResponse)
			{
				this.client = client;
				LoginResponse resp = (LoginResponse)response;
				SellerDTO seller1 = resp.Seller;
				Seller seller2 = DTOUtils.getFromDTO(seller1);
				return seller2;
			}
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				closeConnection();
				throw new ServicesException(err.Message);
			}

			return seller;
		}

		public ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, ITicketSystemObserver client)
		{
			Console.WriteLine("proxy update");
			ArtisticRepresentationDTO arDTO = DTOUtils.getDTO(artisticRepresentation);
			sendRequest(new BuyTicketRequest(arDTO));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new ServicesException(err.Message);
			}
			if (response is BuyTicketResponse)
			{
				this.client = client;
				BuyTicketResponse resp = (BuyTicketResponse)response;
				ArtisticRepresentationDTO ar = resp.ArtisticRepresentation;
				ArtisticRepresentation ars = DTOUtils.getFromDTO(ar);
				return ars;
			}
			Console.WriteLine("proxy final");
			return artisticRepresentation;
		}

		public void logout(Seller seller, ITicketSystemObserver client)
		{
			Console.WriteLine("proxy logout");
			SellerDTO sellerDTO = DTOUtils.getDTO(seller);
			sendRequest(new LogoutRequest(sellerDTO));
			Response response = readResponse();
			closeConnection();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new ServicesException(err.Message);
			}
		}


		public Seller findSellerByUsername(Seller seller)
		{
			return null;
		}

		public Buyer addBuyer(Buyer buyer)
		{
			BuyerDTO buyerDTO = DTOUtils.getDTO(buyer);
			sendRequest(new AddBuyerRequest(buyerDTO));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				closeConnection();
				throw new ServicesException(err.Message);
			}
			if (response is AddBuyerResponse)
			{
				AddBuyerResponse resp = (AddBuyerResponse)response;
				BuyerDTO buyerDTO1 = resp.Buyer;
				Buyer buyer1 = DTOUtils.getFromDTO(buyerDTO1);
				return buyer1;
			}

			return buyer;
		}

		public IEnumerable<ArtisticRepresentation> getAllAR()
		{
			Console.WriteLine("proxy getallAR");
			sendRequest(new GetAllARRequest());
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new ServicesException(err.Message);
			}
			GetAllARResponse resp = (GetAllARResponse)response;
			IEnumerable<ArtisticRepresentationDTO> arsDTO = resp.getArs;
			IEnumerable<ArtisticRepresentation> ars = DTOUtils.getFromDTO(arsDTO);
			return ars;
		}

		public IEnumerable<ArtisticRepresentation> getAllARFromDate(DateTime date)
		{
			sendRequest(new GetAllARFromDateRequest(date));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new ServicesException(err.Message);
			}
			GetAllARFromDateResponse resp = (GetAllARFromDateResponse)response;
			IEnumerable<ArtisticRepresentationDTO> arsDTO = resp.getArs;
			IEnumerable<ArtisticRepresentation> ars = DTOUtils.getFromDTO(arsDTO);
			return ars;
		}

		private void closeConnection()
		{
			finished = true;
			try
			{
				stream.Close();
				//output.close();
				connection.Close();
				_waitHandle.Close();
				client = null;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}

		}

		private void sendRequest(Request request)
		{
			try
			{
				formatter.Serialize(stream, request);
				stream.Flush();
			}
			catch (Exception e)
			{
				throw new ServicesException("Error sending object " + e);
			}

		}

		private Response readResponse()
		{
			Response response = null;
			try
			{
				_waitHandle.WaitOne();
				lock (responses)
				{
					//Monitor.Wait(responses); 
					response = responses.Dequeue();

				}


			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
			return response;
		}
		private void initializeConnection()
		{
			try
			{
				connection = new TcpClient(host, port);
				stream = connection.GetStream();
				formatter = new BinaryFormatter();
				finished = false;
				_waitHandle = new AutoResetEvent(false);
				startReader();
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}
		private void startReader()
		{
			Thread tw = new Thread(run);
			tw.Start();
		}


		private void handleUpdate(UpdateResponse update)
		{
			if (update is BoughtTicketResponse)
			{
				BoughtTicketResponse bUpd = (BoughtTicketResponse)update;
				ArtisticRepresentation ar = DTOUtils.getFromDTO(bUpd.ArtisticRepresentation);
				Console.WriteLine("Someone bought tickets to " + ar);
				try
				{
					client.boughtTicket(ar);
				}
				catch (ServicesException e)
				{
					Console.WriteLine(e.StackTrace);
				}
			}
		}
		public virtual void run()
		{
			while (!finished)
			{
				try
				{
					object response = formatter.Deserialize(stream);
					Console.WriteLine("response received " + response);
					if (response is UpdateResponse)
					{
						handleUpdate((UpdateResponse)response);
					}
					else if(response is BoughtTicketResponse)
                    {
						handleUpdate((UpdateResponse)response);
                    }
					else
					{

						lock (responses)
						{


							responses.Enqueue((Response)response);

						}
						_waitHandle.Set();
					}
				}
				catch (Exception e)
				{
					Console.WriteLine("Reading error " + e);
				}

			}
		}
		//}
	}

}