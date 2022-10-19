using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

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

}