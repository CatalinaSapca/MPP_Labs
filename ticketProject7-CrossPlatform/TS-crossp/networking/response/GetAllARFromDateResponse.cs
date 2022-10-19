using System;
using System.Collections.Generic;

namespace networking.protocol
{

	using ArtisticRepresentationDTO = networking.dto.ArtisticRepresentationDTO;

	[Serializable]
	public class GetAllARFromDateResponse : Response
	{
		private IEnumerable<ArtisticRepresentationDTO> arsDTO;

		public GetAllARFromDateResponse(IEnumerable<ArtisticRepresentationDTO> arsDTO)
		{
			this.arsDTO = arsDTO;
		}

		public virtual IEnumerable<ArtisticRepresentationDTO> getArs
		{
			get
			{
				return arsDTO;
			}
		}
	}

}