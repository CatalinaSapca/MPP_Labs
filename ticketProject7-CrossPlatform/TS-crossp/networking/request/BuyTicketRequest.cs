using System;
namespace networking.protocol
{

	using ArtisticRepresentationDTO = networking.dto.ArtisticRepresentationDTO;

	[Serializable]
	public class BuyTicketRequest : Request
	{
		private ArtisticRepresentationDTO ar;

		public BuyTicketRequest(ArtisticRepresentationDTO ar)
		{
			this.ar = ar;
		}

		public virtual ArtisticRepresentationDTO ArtisticRepresentation
		{
			get
			{
				return ar;
			}
		}
	}

}