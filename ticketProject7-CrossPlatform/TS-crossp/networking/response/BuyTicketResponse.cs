using System;
namespace networking.protocol
{

	using ArtisticRepresentationDTO = networking.dto.ArtisticRepresentationDTO;

	[Serializable]
	public class BuyTicketResponse : Response
	{
		private ArtisticRepresentationDTO artisticRepresentationDTO;

		public BuyTicketResponse(ArtisticRepresentationDTO ar)
		{
			this.artisticRepresentationDTO = ar;
		}

		public virtual ArtisticRepresentationDTO ArtisticRepresentation
		{
			get
			{
				return artisticRepresentationDTO;
			}
		}
	}

}