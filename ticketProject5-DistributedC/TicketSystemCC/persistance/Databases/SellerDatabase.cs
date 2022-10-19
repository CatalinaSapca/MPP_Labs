using System;
using System.Collections.Generic;
using System.Data;
using model;
using model.Validators;
using persistance.Interfaces;

namespace persistance.Database
{

    public class SellerDatabase : ISellerRepository
    {

        //private static readonly ILog log = LogManager.GetLogger("SellerDatabase");
        private Validator<Seller> validator;

        public SellerDatabase(Validator<Seller> validator)
        {
            //log.Info("Creating SellerDatabase");
            this.validator = validator;
        }

        public Seller extractEntity(List<String> attributes)
        {
            if (attributes.Count < 5)
                throw new ValidationException("Insuficient datas!\n");
            Seller seller = new Seller(attributes[1], attributes[2], attributes[3], attributes[4])
            {
                Id = Int64.Parse(attributes[0])
            };
            return seller;
        }

        protected String createEntityAsString(Seller entity)
        {
            return entity.Id + ";" + entity.FirstName + ";" + entity.LastName + ";" + entity.Username + ";" + entity.Password;
        }

        public Seller FindOne(long id)
        {
            IDbConnection con = DBUtils.getConnection();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, firstName, lastName, username, password from Sellers where id=@id";
                    IDbDataParameter paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    comm.Parameters.Add(paramId);

                    using (var dataR = comm.ExecuteReader())
                    {
                        if (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            String firstName = dataR.GetString(1);
                            String lastName = dataR.GetString(2);
                            String username = dataR.GetString(3);
                            String password = dataR.GetString(4);

                            Seller seller = new Seller(firstName, lastName, username, password)
                            {
                                Id = idd
                            };

                            return seller;
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

        public Seller FindOneByUsername(string username)
        {
            IDbConnection con = DBUtils.getConnection();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, firstName, lastName, username, password from Sellers where username=@username";
                    IDbDataParameter paramUsername = comm.CreateParameter();
                    paramUsername.ParameterName = "@username";
                    paramUsername.Value = username;
                    comm.Parameters.Add(paramUsername);

                    using (var dataR = comm.ExecuteReader())
                    {
                        if (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            String firstName = dataR.GetString(1);
                            String lastName = dataR.GetString(2);
                            String username1 = dataR.GetString(3);
                            String password = dataR.GetString(4);

                            Seller seller = new Seller(firstName, lastName, username1, password)
                            {
                                Id = idd
                            };

                            return seller;
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

        public IEnumerable<Seller> FindAll()
        {
            IDbConnection con = DBUtils.getConnection();
            IList<Seller> sellers = new List<Seller>();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, firstName, lastName, username, password from Sellers";

                    using (var dataR = comm.ExecuteReader())
                    {
                        while (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            String firstName = dataR.GetString(1);
                            String lastName = dataR.GetString(2);
                            String username = dataR.GetString(3);
                            String password = dataR.GetString(4);

                            Seller seller = new Seller(firstName, lastName, username, password)
                            {
                                Id = idd
                            };

                            sellers.Add(seller);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }

            return sellers;
        }


        public Seller Save(Seller entity)
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
                    comm.CommandText = "insert into Sellers  values (@firstName, @lastName, @username, @password)";

                    var paramFName = comm.CreateParameter();
                    paramFName.ParameterName = "@firstName";
                    paramFName.Value = entity.FirstName;
                    comm.Parameters.Add(paramFName);

                    var paramLName = comm.CreateParameter();
                    paramLName.ParameterName = "@lastName";
                    paramLName.Value = entity.LastName;
                    comm.Parameters.Add(paramLName);

                    var paramUsername = comm.CreateParameter();
                    paramUsername.ParameterName = "@username";
                    paramUsername.Value = entity.Username;
                    comm.Parameters.Add(paramUsername);

                    var paramPassword = comm.CreateParameter();
                    paramLName.ParameterName = "@password";
                    paramPassword.Value = entity.Password;
                    comm.Parameters.Add(paramPassword);

                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        throw new Exception("Insert failed !");
                    //else
                    //log.InfoFormat("Saved the Seller {0}", entity);
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }

            return null;
        }


        public Seller Delete(long id)
        {
            if (id == null)
                throw new ArgumentException("Non existent user!");

            var con = DBUtils.getConnection();
            try
            {
                Seller entity = FindOne(id);

                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "delete from Sellers where id=@id";
                    IDbDataParameter paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    comm.Parameters.Add(paramId);
                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        return null;
                    else
                    {
                        //log.InfoFormat("Deleted Seller {0}", entity);
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

        public Seller Update(Seller entity)
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
                    comm.CommandText = "update Sellers set firstName=(@firstName), lastName=(@lastName), username=(@username), password=(@password) where id=(@id)";

                    var paramFName = comm.CreateParameter();
                    paramFName.ParameterName = "@firstName";
                    paramFName.Value = entity.FirstName;
                    comm.Parameters.Add(paramFName);

                    var paramLName = comm.CreateParameter();
                    paramLName.ParameterName = "@lastName";
                    paramLName.Value = entity.LastName;
                    comm.Parameters.Add(paramLName);

                    var paramUsername = comm.CreateParameter();
                    paramUsername.ParameterName = "@username";
                    paramUsername.Value = entity.Username;
                    comm.Parameters.Add(paramUsername);

                    var paramPassword = comm.CreateParameter();
                    paramPassword.ParameterName = "@password";
                    paramPassword.Value = entity.Password;
                    comm.Parameters.Add(paramPassword);

                    var paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = entity.Id;
                    comm.Parameters.Add(paramId);

                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        return entity;
                    else
                    {
                        //log.InfoFormat("Updated Seller {0}", entity);
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
