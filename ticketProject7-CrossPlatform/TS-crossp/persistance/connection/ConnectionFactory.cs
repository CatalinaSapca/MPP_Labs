using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Reflection;
using System.Data.SQLite;


namespace ConnectionUtils
{
	public abstract class ConnectionFactory
	{
		protected ConnectionFactory()
		{
		}

		private static ConnectionFactory instance;

		public static ConnectionFactory getInstance()
		{
			if (instance == null)
			{

				Assembly assem = Assembly.GetExecutingAssembly();
				Type[] types = assem.GetTypes();
				foreach (var type in types)
				{
					if (type.IsSubclassOf(typeof(ConnectionFactory)))
						instance = (ConnectionFactory)Activator.CreateInstance(type);
				}
			}
			return instance;
		}

		public IDbConnection createConnection()
		{
			String connectionString = "DataSource=C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db;Version=3;";
			//String connectionString = ConfigurationManager.ConnectionStrings["databaseConfigure"].ConnectionString;
			Console.WriteLine("creare connection2");
			return new SQLiteConnection(connectionString);
		}
	}


}
