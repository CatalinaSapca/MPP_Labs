using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

	[Serializable]
	public class LogoutRequest : Request
	{
		private SellerDTO user;

		public LogoutRequest(SellerDTO user)
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