using System;

namespace networking.dto
{

	[Serializable]
	public class BuyerDTO
	{
		private String idAR;
		private String firstName;
		private String lastName;
		private String noTickets;


		public BuyerDTO(String idAR, String firstName, String lastName, String noTickets)
		{
			this.idAR = idAR;
			this.firstName = firstName;
			this.lastName = lastName;
			this.noTickets = noTickets;
		}

		public virtual string IdAR
		{
			get
			{
				return idAR;
			}
			set
			{
				this.idAR = value;
			}
		}

		public virtual string FirstName
		{
			get
			{
				return firstName;
			}
			set
			{
				this.firstName = value;
			}
		}

		public virtual string LastName
		{
			get
			{
				return lastName;
			}
			set
			{
				this.lastName = value;
			}
		}

		public virtual string NoTickets
		{
			get
			{
				return noTickets;
			}
			set
			{
				this.noTickets = value;
			}
		}


	}


}