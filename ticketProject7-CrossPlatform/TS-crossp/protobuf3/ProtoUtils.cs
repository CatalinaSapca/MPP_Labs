using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using services;
using model;
using Chat.Protocol;
using proto = Chat.Protocol;

namespace protobuf
{
    static class ProtoUtils
    {
        public static TSRequest createLoginRequest(model.Seller user)
        {
            proto.Seller userDTO = new proto.Seller{ Id = user.Id.ToString() , FirstName = user.FirstName, LastName = user.LastName, Username = user.Username, Password = user.Password };
            TSRequest request = new TSRequest { Type = TSRequest.Types.Type.Login, Seller = userDTO };
            return request;
        }

        public static TSRequest createLogoutRequest(model.Seller user)
        {
            proto.Seller userDTO = new proto.Seller { Id = user.Id.ToString(), FirstName = user.FirstName, LastName = user.LastName, Username = user.Username, Password = user.Password };
            TSRequest request = new TSRequest { Type = TSRequest.Types.Type.Logout, Seller = userDTO };
            return request;
        }

        public static TSRequest createAddBuyerRequest(model.Buyer buyer)
        {
            proto.Buyer buyerDTO = new proto.Buyer { Id = buyer.Id.ToString(), FirstName = buyer.FirstName, LastName = buyer.LastName, IdAR = buyer.IdAR.ToString(), NoTickets = buyer.NoTickets.ToString() }; 
            TSRequest request = new TSRequest { Type = TSRequest.Types.Type.AddBuyer, Buyer = buyerDTO };
            return request;
        }

        public static TSRequest createBuyTicketRequest(model.ArtisticRepresentation artisticRepresentation)
        {
            proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = artisticRepresentation.Data.ToString(), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() }; 
            TSRequest request = new TSRequest { Type = TSRequest.Types.Type.BuyTicket, ArtisticRepresentation = arDTO };
            return request;
        }

        public static TSRequest createFindSellerByUsernameRequest(model.Seller seller)
        {
            proto.Seller userDTO = new proto.Seller { Id = seller.Id.ToString(), FirstName = seller.FirstName, LastName = seller.LastName, Username = seller.Username, Password = seller.Password }; 
            TSRequest request = new TSRequest { Type = TSRequest.Types.Type.FindSellerByUsername, Seller = userDTO };
            return request;
        }

        public static TSRequest createGetAllARRequest()
        {
            proto.TSRequest request = new proto.TSRequest { Type = proto.TSRequest.Types.Type.GetAllAr };
            return request;
        }

        public static TSRequest createFGetAllARFRomDateRequest(DateTime data)
        {
            proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = "1", ArtistName = "sdd", Data = data.ToString(), Location = "sazdf", AvailableSeats = "1", SoldSeats = "1" };
            proto.TSRequest request = new proto.TSRequest { Type = proto.TSRequest.Types.Type.GetAllArfromDate, ArtisticRepresentation = arDTO };
            return request;
        }

        //-------------------------
        public static TSResponse createOkResponse()
        {
            TSResponse response = new TSResponse { Type = TSResponse.Types.Type.Ok };
            return response;
        }


        public static TSResponse createErrorResponse(String text)
        {
            TSResponse response = new TSResponse
            {
                Type = TSResponse.Types.Type.Error,
                Error = text
            };
            return response;
        }

        public static TSResponse createLoginResponse(model.Seller seller)
        {
            proto.Seller sellerDTO = new proto.Seller { Id = seller.Id.ToString(), FirstName = seller.FirstName, LastName = seller.LastName, Username = seller.Username, Password = seller.Password };
            proto.TSResponse response = new proto.TSResponse { Type = TSResponse.Types.Type.Login, Seller = sellerDTO };
            return response;
        }

        public static TSResponse createLogoutResponse()
        {
           TSResponse response = new TSResponse { Type = TSResponse.Types.Type.Logout };
            return response;
        }

        public static TSResponse createBoughtTicketResponse(model.ArtisticRepresentation artisticRepresentation)
        {
            TSResponse response = new TSResponse { Type = TSResponse.Types.Type.BoughtTicket };

            proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = artisticRepresentation.Data.ToString(), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() };
            response.ArtisticRepresentation.Add(arDTO);

            return response;
        }

        public static TSResponse createAddBuyerResponse(model.Buyer buyer)
        {
            if (buyer == null)
            {
                TSResponse response = new TSResponse { Type = TSResponse.Types.Type.Ok };
                return response;
            }
            else
            {
                proto.Buyer buyerDTO = new proto.Buyer { Id = buyer.Id.ToString(), FirstName = buyer.FirstName, LastName = buyer.LastName, IdAR = buyer.IdAR.ToString(), NoTickets = buyer.NoTickets.ToString() };
                TSResponse response = new TSResponse { Type = TSResponse.Types.Type.AddBuyer, Buyer = buyerDTO };
                return response;
            }
        }

        public static TSResponse createBuyTicketResponse(model.ArtisticRepresentation artisticRepresentation)
        {
            if (artisticRepresentation == null)
            {
                proto.ArtisticRepresentation arDTO = null;
                TSResponse response = new TSResponse { Type = TSResponse.Types.Type.Ok };
                return response;
            }
            else
            {
                proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = artisticRepresentation.Data.ToString(), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() };

                //TSResponse response = new TSResponse { Type = TSResponse.Types.Type.BuyTicket, ArtisticRepresentation = arDTO };
                //return response;
                TSResponse response = new TSResponse { Type = TSResponse.Types.Type.BuyTicket};
                response.ArtisticRepresentation.Add(arDTO);
                return response;
            }
        }

        public static TSResponse createGetAllARResponse(IEnumerable<model.ArtisticRepresentation> ars)
        {
            TSResponse response = new TSResponse { Type = TSResponse.Types.Type.GetAllAr };

            foreach (model.ArtisticRepresentation artisticRepresentation in ars)
            {
                proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = artisticRepresentation.Data.ToString(), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() };
                //proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = getStringFromDateTime(artisticRepresentation.Data), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() };
                response.ArtisticRepresentation.Add(arDTO);
            }

            return response;
        }

        public static String getStringFromDateTime(DateTime data)
        {
            String result = "";
            result = result + data.Year + "-" + data.Month + "-" + data.Day + "T" + data.Hour + ":" + data.Minute + ":" + data.Second;
            return result;
        }

        public static TSResponse createGetAllARFromDateResponse(IEnumerable<model.ArtisticRepresentation> ars)
        {
            TSResponse response = new TSResponse { Type = TSResponse.Types.Type.GetAllArfromDate };

            foreach (model.ArtisticRepresentation artisticRepresentation in ars)
            {
                proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = artisticRepresentation.Data.ToString(), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() };
                //proto.ArtisticRepresentation arDTO = new proto.ArtisticRepresentation { Id = artisticRepresentation.Id.ToString(), ArtistName = artisticRepresentation.ArtistName, Data = getStringFromDateTime(artisticRepresentation.Data), Location = artisticRepresentation.Location, AvailableSeats = artisticRepresentation.AvailableSeats.ToString(), SoldSeats = artisticRepresentation.SoldSeats.ToString() };
                response.ArtisticRepresentation.Add(arDTO);
            }

            return response;
        }

        public static TSResponse createFindSellerByUsernameResponse(model.Seller seller)
        {
            proto.Seller sellerDTO = new proto.Seller{ Id = seller.Id.ToString(), FirstName = seller.FirstName, LastName = seller.LastName, Username = seller.Username, Password = seller.Password };
            TSResponse response = new TSResponse { Type = TSResponse.Types.Type.FindSellerByUsername, Seller = sellerDTO };
            return response;
        }
        //----------------------------

        public static String getError(TSResponse response)
        {
            String errorMessage = response.Error;
            return errorMessage;
        }

        public static model.Seller getSeller(TSResponse response)
        {
            model.Seller seller = new model.Seller()
            {
                Id = long.Parse(response.Seller.Id),
                FirstName = response.Seller.FirstName,
                LastName = response.Seller.LastName,
                Username = response.Seller.Username,
                Password = response.Seller.Password
            };
            return seller;
        }

        public static IEnumerable<model.ArtisticRepresentation> getArtisticRepresentations(TSResponse response)
        {
            List<model.ArtisticRepresentation> ars = new List<model.ArtisticRepresentation>();
            for (int i = 0; i < response.ArtisticRepresentation.Count; i++)
            {
                model.ArtisticRepresentation ar = new model.ArtisticRepresentation("1L", DateTime.Now, "sdc", 2L, 2L);
                ar.Id = long.Parse(response.ArtisticRepresentation[i].Id);
                ar.ArtistName = response.ArtisticRepresentation[i].ArtistName;
                ar.Data = DateTime.Parse(response.ArtisticRepresentation[i].Data);
                ar.Location = response.ArtisticRepresentation[i].Location;
                ar.AvailableSeats = long.Parse(response.ArtisticRepresentation[i].AvailableSeats);
                ar.SoldSeats = long.Parse(response.ArtisticRepresentation[i].SoldSeats);

                ars.Add(ar);
            }
            return ars;
        }

        public static model.Seller getSeller(TSRequest request)
        {
            model.Seller seller = new model.Seller();
            seller.Id = long.Parse(request.Seller.Id);
            seller.FirstName = request.Seller.FirstName;
            seller.LastName = request.Seller.LastName;
            seller.Username = request.Seller.Username;
            seller.Password = request.Seller.Password;
            return seller;
        }

        public static model.Buyer getBuyer(TSRequest request)
        {
            model.Buyer buyer = new model.Buyer(1L, "scd", "sdc", 2L);
            buyer.Id = long.Parse(request.Buyer.Id);
            buyer.IdAR = long.Parse(request.Buyer.IdAR);
            buyer.FirstName = request.Buyer.FirstName;
            buyer.LastName = request.Buyer.LastName;
            buyer.NoTickets = long.Parse(request.Buyer.NoTickets);
            return buyer;
        }

        public static model.ArtisticRepresentation getArtisticRepresentation(TSRequest request)
        {
            model.ArtisticRepresentation ar = new model.ArtisticRepresentation("1", DateTime.Now, "sdc", 2L, 2L);
            ar.Id = long.Parse(request.ArtisticRepresentation.Id);
            ar.ArtistName = request.ArtisticRepresentation.ArtistName;
            ar.Data = DateTime.Parse(request.ArtisticRepresentation.Data);
            ar.Location = request.ArtisticRepresentation.Location;
            ar.AvailableSeats = long.Parse(request.ArtisticRepresentation.AvailableSeats);
            ar.SoldSeats = long.Parse(request.ArtisticRepresentation.SoldSeats);
            return ar;
        }

    }
}