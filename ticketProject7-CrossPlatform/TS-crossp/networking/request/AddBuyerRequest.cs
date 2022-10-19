using System;
namespace networking.protocol
{

	using BuyerDTO = networking.dto.BuyerDTO;

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

}