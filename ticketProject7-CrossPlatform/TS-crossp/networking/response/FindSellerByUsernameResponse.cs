using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

	[Serializable]
	public class FindSellerByUsernameResponse : Response
	{
		private SellerDTO seller;

		public FindSellerByUsernameResponse(SellerDTO seller)
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