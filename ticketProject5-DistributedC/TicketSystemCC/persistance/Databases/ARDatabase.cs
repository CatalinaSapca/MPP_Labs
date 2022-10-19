using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using persistance.Interfaces;
using model;
using model.Validators;

namespace persistance.Database
{
    public class ARDatabase : IARRepository
    {
        //private static readonly ILog log = LogManager.GetLogger("ARDatabase");
        private Validator<ArtisticRepresentation> validator;

        public ARDatabase(Validator<ArtisticRepresentation> validator)
        {
            //log.Info("Creating ARDatabase");
            this.validator = validator;
        }

        public ArtisticRepresentation extractEntity(List<String> attributes)
        {
            if (attributes.Count < 6)
                throw new ValidationException("Insuficient datas!\n");
            ArtisticRepresentation ar = new ArtisticRepresentation(attributes[1], DateTime.Parse(attributes[2]), attributes[3], long.Parse(attributes[4]), long.Parse(attributes[5]))
            {
                Id = Int64.Parse(attributes[0])
            };
            return ar;
        }

        protected String createEntityAsString(ArtisticRepresentation entity)
        {
            return entity.Id + ";" + entity.ArtistName + ";" + entity.Data + ";" + entity.Location + ";" + entity.AvailableSeats + ";" + entity.SoldSeats;
        }

        public ArtisticRepresentation FindOne(long id)
        {
            IDbConnection con = DBUtils.getConnection();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, artistName, data, location, availableSeats, soldSeats from ARs where id=@id";
                    IDbDataParameter paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    comm.Parameters.Add(paramId);

                    using (var dataR = comm.ExecuteReader())
                    {
                        if (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            String artistName = dataR.GetString(1);
                            DateTime data = DateTime.Parse(dataR.GetString(2));
                            String location = dataR.GetString(3);
                            long availableSeats = dataR.GetInt64(4);
                            long soldSeats = dataR.GetInt64(5);

                            ArtisticRepresentation ar = new ArtisticRepresentation(artistName, data, location, availableSeats, soldSeats)
                            {
                                Id = idd
                            };

                            Console.WriteLine(ar);
                            return ar;
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

        public IEnumerable<ArtisticRepresentation> FindAll()
        {
            Console.WriteLine("sunt in ARrepo");
            IDbConnection con = DBUtils.getConnection();
            Console.WriteLine("sunt in ARrepo2");
            IList<ArtisticRepresentation> ars = new List<ArtisticRepresentation>();

            try
            {
                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "select id, artistName, data, location, availableSeats, soldSeats from ARs";

                    using (var dataR = comm.ExecuteReader())
                    {
                        while (dataR.Read())
                        {
                            long idd = dataR.GetInt32(0);
                            String artistName = dataR.GetString(1);
                            DateTime data = DateTime.Parse(dataR.GetString(2));
                            String location = dataR.GetString(3);
                            long availableSeats = dataR.GetInt64(4);
                            long soldSeats = dataR.GetInt64(5);

                            ArtisticRepresentation ar = new ArtisticRepresentation(artistName, data, location, availableSeats, soldSeats)
                            {
                                Id = idd
                            };

                            ars.Add(ar);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }

            return ars;
        }


        public ArtisticRepresentation Save(ArtisticRepresentation entity)
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
                    comm.CommandText = "insert into ARs (artistName, data, location, availableSeats, soldSeats)  values (@artistName, @data, @location, @availableSeats, @soldSeats)";

                    var paramArtistName = comm.CreateParameter();
                    paramArtistName.ParameterName = "@artistName";
                    paramArtistName.Value = entity.ArtistName;
                    comm.Parameters.Add(paramArtistName);

                    var paramData = comm.CreateParameter();
                    paramData.ParameterName = "@data";
                    paramData.Value = entity.Data;
                    comm.Parameters.Add(paramData);

                    var paramLocation = comm.CreateParameter();
                    paramLocation.ParameterName = "@location";
                    paramLocation.Value = entity.Location;
                    comm.Parameters.Add(paramLocation);

                    var paramAvailableSeats = comm.CreateParameter();
                    paramAvailableSeats.ParameterName = "@availableSeats";
                    paramAvailableSeats.Value = entity.AvailableSeats;
                    comm.Parameters.Add(paramAvailableSeats);

                    var paramSoldSeats = comm.CreateParameter();
                    paramSoldSeats.ParameterName = "@soldSeats";
                    paramSoldSeats.Value = entity.SoldSeats;
                    comm.Parameters.Add(paramSoldSeats);

                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        throw new Exception("Insert failed !");
                    //selse
                    //log.InfoFormat("Saved the AR {0}", entity);
                }
            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Mesajul erorii este {0}", ex.Message);
            }

            return null;
        }


        public ArtisticRepresentation Delete(long id)
        {
            if (id == null)
                throw new ArgumentException("Non existent user!");

            var con = DBUtils.getConnection();
            try
            {
                ArtisticRepresentation entity = FindOne(id);

                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "delete from ARs where id=@id";
                    IDbDataParameter paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = id;
                    comm.Parameters.Add(paramId);
                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        return null;
                    else
                    {
                        //log.InfoFormat("Deleted AR {0}", entity);
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

        public ArtisticRepresentation Update(ArtisticRepresentation entity)
        {
            Debug.WriteLine("2 " + entity.SoldSeats);
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
                    comm.CommandText = "update ARs set artistName=(@artistName), data=(@data), location=(@location), availableSeats=(@availableSeats), soldSeats=(@soldSeats) where id=(@id)";

                    var paramArtistName = comm.CreateParameter();
                    paramArtistName.ParameterName = "@artistName";
                    paramArtistName.Value = entity.ArtistName;
                    comm.Parameters.Add(paramArtistName);

                    var paramData = comm.CreateParameter();
                    paramData.ParameterName = "@data";
                    paramData.Value = entity.Data;
                    comm.Parameters.Add(paramData);

                    var paramLocation = comm.CreateParameter();
                    paramLocation.ParameterName = "@location";
                    paramLocation.Value = entity.Location;
                    comm.Parameters.Add(paramLocation);

                    var paramAvailableSeats = comm.CreateParameter();
                    paramAvailableSeats.ParameterName = "@availableSeats";
                    paramAvailableSeats.Value = entity.AvailableSeats;
                    comm.Parameters.Add(paramAvailableSeats);

                    var paramSoldSeats = comm.CreateParameter();
                    paramSoldSeats.ParameterName = "@soldSeats";
                    paramSoldSeats.Value = entity.SoldSeats;
                    comm.Parameters.Add(paramSoldSeats);
                    //Debug.WriteLine("3 " + paramSoldSeats.Value);

                    var paramId = comm.CreateParameter();
                    paramId.ParameterName = "@id";
                    paramId.Value = entity.Id;
                    comm.Parameters.Add(paramId);

                    var result = comm.ExecuteNonQuery();
                    if (result == 0)
                        return entity;
                    else
                    {
                        //log.InfoFormat("Updated AR {0}", entity);
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