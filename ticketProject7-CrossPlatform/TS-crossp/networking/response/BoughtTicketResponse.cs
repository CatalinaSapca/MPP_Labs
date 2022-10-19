using System;
namespace networking.protocol
{

	using ArtisticRepresentationDTO = networking.dto.ArtisticRepresentationDTO;

	[Serializable]
	public class BoughtTicketResponse : UpdateResponse
	{
		private ArtisticRepresentationDTO artisticRepresentationDTO;

		public BoughtTicketResponse(ArtisticRepresentationDTO ar)
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