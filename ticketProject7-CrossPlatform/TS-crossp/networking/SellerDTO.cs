using System;

namespace networking.dto
{

	[Serializable]
	public class SellerDTO
	{
		private String id;
		private String firstName;
		private String lastName;
		private String username;
		private String password;


		public SellerDTO(String id, String firstName, String lastName, String username, String password)
		{
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.username = username;
			this.password = password;
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

		public virtual string Username
		{
			get
			{
				return username;
			}
			set
			{
				this.username = value;
			}
		}

		public virtual string Password
		{
			get
			{
				return password;
			}
			set
			{
				this.password = value;
			}
		}


	}


}