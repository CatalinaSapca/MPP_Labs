using System;
namespace networking.protocol
{

	using SellerDTO = networking.dto.SellerDTO;

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