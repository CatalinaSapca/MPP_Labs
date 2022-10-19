using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using persistance.Interfaces;
using model;
using model.Validators;

namespace persistance.Database
{
    public class BuyerDatabase : IBuyerRepository
    {
        //private static readonly ILog log = LogManager.GetLogger("BuyerDatabase");
        private Validator<Buyer> validator;

        public BuyerDatabase(Validator<Buyer> validator)
        {
            //log.Info("Creating BuyerDatabase");
            this.validator = validator;
        }

        public Buyer extractEntity(List<String> attributes)
        {
            if (attributes.Count < 5)
                throw new ValidationException("Insuficient datas!\n");
            Buyer buyer = new Buyer(long.Parse(attributes[1]), attributes[2], attributes[3], long.Parse(attributes[4]))
            {
                Id = Int64.Parse(attributes[0])
            };
            return buyer;
        }

        protected String createEntityAsString(Buyer entity)
        {
            return entity.Id + ";" + entity.IdAR + ";" + entity.FirstName + ";" + entity.LastName + ";" + entity.NoTickets;
        }

        public Buyer FindOne(long id)
        {
            IDbConnection con = DBUtils.getConnection();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, idAR, firstName, lastName, noTickets from Buyers where id=@id";
                    IDbDataParameter paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    comm.Parameters.Add(paramId);

                    using (var dataR = comm.ExecuteReader())
                    {
                        if (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            long idAR = dataR.GetInt64(1);
                            String firstName = dataR.GetString(2);
                            String lastName = dataR.GetString(3);
                            long noTickets = dataR.GetInt64(4);

                            Buyer buyer = new Buyer(idAR, firstName, lastName, noTickets)
                            {
                                Id = idd
                            };

                            return buyer;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }
            return null;
        }

        public IEnumerable<Buyer> FindAll()
        {
            IDbConnection con = DBUtils.getConnection();
            IList<Buyer> buyers = new List<Buyer>();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, idAR, firstName, lastName, noTickets from Buyers";

                    using (var dataR = comm.ExecuteReader())
                    {
                        while (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            long idAR = dataR.GetInt32(1);
                            String firstName = dataR.GetString(2);
                            String lastName = dataR.GetString(3);
                            long noTickets = dataR.GetInt64(4);

                            Buyer buyer = new Buyer(idAR, firstName, lastName, noTickets)
                            {
                                Id = idd
                            };

                            buyers.Add(buyer);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }

            return buyers;
        }


        public Buyer Save(Buyer entity)
        {
            if (entity == null)
                throw new ArgumentException("entity must be not null");

            validator.Validate(entity);
            //if (FindOne(entity.Id) != null)
            //{
            //    return entity;
            //}

            var con = DBUtils.getConnection();
            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "insert into Sellers  values (@idAR, @firstName, @lastName, @noTickets)";

                    var paramIdAR = comm.CreateParameter();
                    paramIdAR.ParameterName = "@idAR";
                    paramIdAR.Value = entity.IdAR;
                    comm.Parameters.Add(paramIdAR);

                    var paramFName = comm.CreateParameter();
                    paramFName.ParameterName = "@firstName";
                    paramFName.Value = entity.FirstName;
                    comm.Parameters.Add(paramFName);

                    var paramLName = comm.CreateParameter();
                    paramLName.ParameterName = "@lastName";
                    paramLName.Value = entity.LastName;
                    comm.Parameters.Add(paramLName);

                    var paramNoTickets = comm.CreateParameter();
                    paramNoTickets.ParameterName = "@noTickets";
                    paramNoTickets.Value = entity.NoTickets;
                    comm.Parameters.Add(paramNoTickets);

                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        throw new Exception("Insert failed !");
                    //else
                    //log.InfoFormat("Saved the Buyer {0}", entity);
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }

            return null;
        }


        public Buyer Delete(long id)
        {
            if (id == null)
                throw new ArgumentException("Non existent user!");

            var con = DBUtils.getConnection();
            try
            {
                Buyer entity = FindOne(id);

                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "delete from Buyers where id=@id";
                    IDbDataParameter paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    comm.Parameters.Add(paramId);
                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        return null;
                    else
                    {
                        //log.InfoFormat("Deleted Buyer {0}", entity);
                        return entity;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }
            return null;
        }

        public Buyer Update(Buyer entity)
        {
            if (entity == null)
                throw new ArgumentException("entity must be not null!");
            validator.Validate(entity);

            if (FindOne(entity.Id) == null)
                return entity;

            var con = DBUtils.getConnection();
            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "update Buyers set idAR=(@idAR), firstName=(@firstName), lastName=(@lastName), noTickets=(@noTickets) where id=(@id)";

                    var paramIdAR = comm.CreateParameter();
                    paramIdAR.ParameterName = "@idAR";
                    paramIdAR.Value = entity.IdAR;
                    comm.Parameters.Add(paramIdAR);

                    var paramFName = comm.CreateParameter();
                    paramFName.ParameterName = "@firstName";
                    paramFName.Value = entity.FirstName;
                    comm.Parameters.Add(paramFName);

                    var paramLName = comm.CreateParameter();
                    paramLName.ParameterName = "@lastName";
                    paramLName.Value = entity.LastName;
                    comm.Parameters.Add(paramLName);

                    var paramNoTickets = comm.CreateParameter();
                    paramNoTickets.ParameterName = "@noTickets";
                    paramNoTickets.Value = entity.NoTickets;
                    comm.Parameters.Add(paramNoTickets);

                    var paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = entity.Id;
                    comm.Parameters.Add(paramId);

                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        return entity;
                    else
                    {
                        //log.InfoFormat("Updated Buyer {0}", entity);
                        return null;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }
            return entity;
        }

    }
}