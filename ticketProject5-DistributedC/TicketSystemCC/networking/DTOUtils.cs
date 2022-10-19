namespace networking.dto
{

	using ArtisticRepresentation = model.ArtisticRepresentation;
	using Seller = model.Seller;
	using Buyer = model.Buyer;
	using System;
	using System.Collections.Generic;

	public class DTOUtils
	{
		public static ArtisticRepresentation getFromDTO(ArtisticRepresentationDTO arDTO)
		{
			if (arDTO == null)
				return null;

			String id = arDTO.Id;
			String artistName = arDTO.ArtistName;
			String data = arDTO.Data;
			String location = arDTO.Location;
			String availableSeats = arDTO.AvailableSeats;
			String soldSeats = arDTO.SoldSeats;

			ArtisticRepresentation a = new ArtisticRepresentation(artistName, DateTime.Parse(data), location, long.Parse(availableSeats), long.Parse(soldSeats));
			a.Id = long.Parse(id);
			return a;
		}

		public static ArtisticRepresentationDTO getDTO(ArtisticRepresentation ar)
		{
			if (ar == null)
				return null;

			String id = ar.Id.ToString();
			String artistName = ar.ArtistName;
			String data = ar.Data.ToString();
			String location = ar.Location;
			String availableSeats = ar.AvailableSeats.ToString();
			String soldSeats = ar.SoldSeats.ToString();
			return new ArtisticRepresentationDTO(id, artistName, data, location, availableSeats, soldSeats);
		}

		public static Buyer getFromDTO(BuyerDTO buyerDTO)
		{
			if (buyerDTO == null)
				return null;

			String idAR = buyerDTO.IdAR;
			String firstName = buyerDTO.FirstName;
			String lastName = buyerDTO.LastName;
			String noTickets = buyerDTO.NoTickets;

			return new Buyer(long.Parse(idAR), firstName, lastName, long.Parse(noTickets));

		}
		public static BuyerDTO getDTO(Buyer buyer)
		{
			if (buyer == null)
				return null;

			String idAR = buyer.IdAR.ToString();
			String firstName = buyer.FirstName;
			String lastName = buyer.LastName;
			String noTickets = buyer.NoTickets.ToString();

			return new BuyerDTO(idAR, firstName, lastName, noTickets);
		}

		public static Seller getFromDTO(SellerDTO sellerDTO)
		{
			if (sellerDTO == null)
				return null;

			String id = sellerDTO.Id;
			String firstName = sellerDTO.FirstName;
			String lastName = sellerDTO.LastName;
			String username = sellerDTO.Username;
			String password = sellerDTO.Password;

			Seller s = new Seller(firstName, lastName, username, password);
			s.Id = long.Parse(id);

			return s;

		}
		public static SellerDTO getDTO(Seller seller)
		{
			if (seller == null)
				return null;

			String id = seller.Id.ToString();
			String firstName = seller.FirstName;
			String lastName = seller.LastName;
			String username = seller.Username;
			String password = seller.Password;

			return new SellerDTO(id, firstName, lastName, username, password);
		}

		public static IEnumerable<ArtisticRepresentationDTO> getDTO(IEnumerable<ArtisticRepresentation> ars)
		{
			List<ArtisticRepresentationDTO> arsDTO = new List<ArtisticRepresentationDTO>();
			foreach (ArtisticRepresentation a in ars)
			{
				arsDTO.Add(getDTO(a));
			}
			return arsDTO;
		}

		public static IEnumerable<ArtisticRepresentation> getFromDTO(IEnumerable<ArtisticRepresentationDTO> arsDTO)
		{
			List<ArtisticRepresentation> ars = new List<ArtisticRepresentation>();
			foreach (ArtisticRepresentationDTO a in arsDTO)
			{
				ars.Add(getFromDTO(a));
			}
			return ars;
		}

	}

}