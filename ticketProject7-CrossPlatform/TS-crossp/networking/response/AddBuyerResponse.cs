using System;
namespace networking.protocol
{

	using BuyerDTO = networking.dto.BuyerDTO;

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

}