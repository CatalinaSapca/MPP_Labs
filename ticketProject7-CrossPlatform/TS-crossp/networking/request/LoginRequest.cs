using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

	[Serializable]
	public class LoginRequest : Request
	{
		private SellerDTO user;

		public LoginRequest(SellerDTO user)
		{
			this.user = user;
		}

		public virtual SellerDTO Seller
		{
			get
			{
				return user;
			}
		}
	}

}