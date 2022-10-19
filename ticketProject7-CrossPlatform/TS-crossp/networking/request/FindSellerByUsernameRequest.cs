using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

	[Serializable]
	public class FindSellerByUsernameRequest : Request
	{
		private SellerDTO seller;

		public FindSellerByUsernameRequest(SellerDTO seller)
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