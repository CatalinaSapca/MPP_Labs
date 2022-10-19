using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SqlClient;
using Mono.Data.Sqlite;
using System.Data.SQLite;
using System.Configuration;

namespace ConnectionUtils
{
    public class SqliteConnectionFactory : ConnectionFactory
    {
        public IDbConnection createConnection()
        {

            //Mono Sqlite Connection
            //String connectionString = "URI=file:C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db,Version=3";
            String connectionString = "DataSource=C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db;Version=3;";
            //String connectionString = ConfigurationManager.ConnectionStrings["databaseConfigure"].ConnectionString;
            Console.WriteLine("creare connection");
            return new SQLiteConnection(connectionString);

            // Windows Sqlite Connection, fisierul .db ar trebuie sa fie in directorul debug/bin
            //String connectionString = "Data Source=tasks.db;Version=3";
            //return new SqliteConnection(connectionString);
        }
    }
}