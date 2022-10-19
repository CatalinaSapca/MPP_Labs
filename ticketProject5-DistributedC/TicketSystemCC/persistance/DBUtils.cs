using System.Data;
using System;
using System.Reflection;
using ConnectionUtils;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using Mono.Data.Sqlite;
using System.Data.SQLite;


namespace persistance
{

    public static class DBUtils
    {
        private static IDbConnection instance = null;

        public static IDbConnection getConnection()
        {
            if (instance == null || instance.State == System.Data.ConnectionState.Closed)
            {
                instance = getNewConnection();
                instance.Open();
            }
            return instance;
        }

        private static IDbConnection getNewConnection()
        {

            return ConnectionFactory.getInstance().createConnection();
            //return ConnectionUtils.SqliteConnectionFactory.getInstance().createConnection();


        }
    }
}








//using System.Data;
//using System;
//using System.Reflection;
//using ConnectionUtils;

//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Threading.Tasks;
//using System.Data;
//using Mono.Data.Sqlite;
//using System.Data.SQLite;


//namespace persistence
//{

//    public static class DBUtils
//    {
//        private static IDbConnection instance = null;

//        public static IDbConnection getConnection()
//        {
//            if (instance == null || instance.State == System.Data.ConnectionState.Closed)
//            {
//                instance = getNewConnection();
//                instance.Open();
//            }
//            return instance;
//        }

//        private static IDbConnection getNewConnection()
//        {

//            return ConnectionFactory.getInstance().createConnection();
//            //return ConnectionUtils.SqliteConnectionFactory.getInstance().createConnection();


//        }
//    }

//    public abstract class ConnectionFactory
//    {
//        protected ConnectionFactory()
//        {
//        }

//        private static ConnectionFactory instance;

//        public static ConnectionFactory getInstance()
//        {
//            if (instance == null)
//            {

//                Assembly assem = Assembly.GetExecutingAssembly();
//                Type[] types = assem.GetTypes();
//                foreach (var type in types)
//                {
//                    if (type.IsSubclassOf(typeof(ConnectionFactory)))
//                        instance = (ConnectionFactory)Activator.CreateInstance(type);
//                }
//                Console.WriteLine("getinstance");
//            }
//            return instance;
//        }

//        public abstract IDbConnection createConnection();
//    }

//    public class SqliteConnectionFactory : ConnectionFactory
//    {
//        public override IDbConnection createConnection()
//        {
//            Console.WriteLine("creating ... sqlite connection");
//            //String connectionString = @"Data Source=C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db;Pooling=true;FailIfMissing=false;Version=3";
//            String connectionString = "Data Source=C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db;Version=3;";
//            //String connectionString = "URI = file:C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db,Version=3;";
//            //String connectionString = "DataSource=C:\\Users\\Admin\\Desktop\\databases\\MPP\\tickets.db;Version=3;";
//            return new SqliteConnection(connectionString);


//        }
//    }
//}