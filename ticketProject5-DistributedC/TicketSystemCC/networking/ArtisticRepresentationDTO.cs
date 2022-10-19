using System;

namespace networking.dto
{

	[Serializable]
	public class ArtisticRepresentationDTO
	{
		private String id;
		private String artistName;
		private String data;
		private String location;
		private String availableSeats;
		private String soldSeats;


		public ArtisticRepresentationDTO(String id, String artistName, String data, String location, String availableSeats, String soldSeats)
		{
			this.id = id;
			this.artistName = artistName;
			this.data = data;
			this.location = location;
			this.availableSeats = availableSeats;
			this.soldSeats = soldSeats;
		}

		public virtual string Id
		{
			get
			{
				return id;
			}
			set
			{
				this.id = value;
			}
		}

		public virtual string ArtistName
		{
			get
			{
				return artistName;
			}
			set
			{
				this.artistName = value;
			}
		}

		public virtual string Data
		{
			get
			{
				return data;
			}
			set
			{
				this.data = value;
			}
		}

		public virtual string Location
		{
			get
			{
				return location;
			}
			set
			{
				this.location = value;
			}
		}

		public virtual string AvailableSeats
		{
			get
			{
				return availableSeats;
			}
			set
			{
				this.availableSeats = value;
			}
		}

		public virtual string SoldSeats
		{
			get
			{
				return soldSeats;
			}
			set
			{
				this.soldSeats = value;
			}
		}

	}


}