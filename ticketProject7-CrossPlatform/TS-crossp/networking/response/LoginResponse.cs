using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

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

}