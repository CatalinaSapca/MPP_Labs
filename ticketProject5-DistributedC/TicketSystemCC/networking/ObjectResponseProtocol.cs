using System;
using System.Collections.Generic;

namespace networking
{
	using ArtisticRepresentationDTO = networking.dto.ArtisticRepresentationDTO;
	using SellerDTO = networking.dto.SellerDTO;
	using BuyerDTO = networking.dto.BuyerDTO;

	public interface Response
	{
	}

	[Serializable]
	public class OkResponse : Response
	{

	}

	[Serializable]
	public class ErrorResponse : Response
	{
		private string message;

		public ErrorResponse(string message)
		{
			this.message = message;
		}

		public virtual string Message
		{
			get
			{
				return message;
			}
		}
	}

	[Serializable]
	public class LoginResponse : Response
	{
		private SellerDTO seller;

		public LoginResponse(SellerDTO seller)
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
	public class LogoutResponse : Response
	{
		private SellerDTO seller;

		public LogoutResponse(SellerDTO seller)
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
	public class AddBuyerResponse : Response
	{
		private BuyerDTO buyer;

		public AddBuyerResponse(BuyerDTO buyer)
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
	public class BoughtTicketResponse : UpdateResponse
	{
		private ArtisticRepresentationDTO artisticRepresentationDTO;

		public BoughtTicketResponse(ArtisticRepresentationDTO ar)
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
	public class BuyTicketResponse : Response
	{
		private ArtisticRepresentationDTO artisticRepresentationDTO;

		public BuyTicketResponse(ArtisticRepresentationDTO ar)
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
	public class GetAllARResponse : Response
	{
		private IEnumerable<ArtisticRepresentationDTO> arsDTO;

		public GetAllARResponse(IEnumerable<ArtisticRepresentationDTO> arsDTO)
		{
			this.arsDTO = arsDTO;
		}

		public virtual IEnumerable<ArtisticRepresentationDTO> getArs
		{
			get
			{
				return arsDTO;
			}
		}
	}

	[Serializable]
	public class GetAllARFromDateResponse : Response
	{
		private IEnumerable<ArtisticRepresentationDTO> arsDTO;

		public GetAllARFromDateResponse(IEnumerable<ArtisticRepresentationDTO> arsDTO)
		{
			this.arsDTO = arsDTO;
		}

		public virtual IEnumerable<ArtisticRepresentationDTO> getArs
		{
			get
			{
				return arsDTO;
			}
		}
	}
	public interface UpdateResponse : Response
	{
	}

}