using System;


namespace networking
{
	using ArtisticRepresentationDTO = networking.dto.ArtisticRepresentationDTO;
	using SellerDTO = networking.dto.SellerDTO;
	using BuyerDTO = networking.dto.BuyerDTO;


	public interface Request
	{
	}


	[Serializable]
	public class LoginRequest : Request
	{
		private SellerDTO seller;

		public LoginRequest(SellerDTO seller)
		{
			this.seller = seller;
		}

		public virtual SellerDTO Seller
		{
			get
			{
				return seller;
			}
		}
	}

	[Serializable]
	public class LogoutRequest : Request
	{
		private SellerDTO seller;

		public LogoutRequest(SellerDTO seller)
		{
			this.seller = seller;
		}

		public virtual SellerDTO Seller
		{
			get
			{
				return seller;
			}
		}
	}

	[Serializable]
	public class AddBuyerRequest : Request
	{
		private BuyerDTO buyer;

		public AddBuyerRequest(BuyerDTO buyer)
		{
			this.buyer = buyer;
		}

		public virtual BuyerDTO Buyer
		{
			get
			{
				return buyer;
			}
		}
	}

	[Serializable]
	public class BuyTicketRequest : Request
	{
		private ArtisticRepresentationDTO artisticRepresentationDTO;

		public BuyTicketRequest(ArtisticRepresentationDTO ar)
		{
			this.artisticRepresentationDTO = ar;
		}

		public virtual ArtisticRepresentationDTO ArtisticRepresentation
		{
			get
			{
				return artisticRepresentationDTO;
			}
		}
	}

	[Serializable]
	public class GetAllARRequest : Request
	{
		public GetAllARRequest() { }
	}

	[Serializable]
	public class GetAllARFromDateRequest : Request
	{
		private DateTime date;

		public GetAllARFromDateRequest(DateTime date)
		{
			this.date = date;
		}

		public virtual DateTime Date
		{
			get
			{
				return date;
			}
		}
	}


}