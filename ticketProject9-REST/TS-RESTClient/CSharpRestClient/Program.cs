using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace TSRESTClient
{
	class MainClass
	{
		static HttpClient client = new HttpClient();

		static string path= "http://localhost:8080/TS/ArtisticRepresentations";

		public static void Main(string[] args)
		{
			Console.WriteLine("Hello World!");
			RunAsync().Wait();
		}


		static async Task RunAsync()
		{
			client.BaseAddress = new Uri("http://localhost:8080/TS/ArtisticRepresentations");
			runTests();
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

			Console.ReadLine();
		}

		static async Task<IEnumerable<ArtisticRepresentation>> GetARAsync()
		{
			IEnumerable<ArtisticRepresentation> product = null;
			HttpResponseMessage response = await client.GetAsync(path);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsAsync<IEnumerable<ArtisticRepresentation>>();
			}
			


			return product;
		}

		static async Task<IEnumerable<ArtisticRepresentation>> GetAllARWithArtistNameAsync(String artistName)
		{
			IEnumerable<ArtisticRepresentation> product = null;
			HttpResponseMessage response = await client.GetAsync(path + "/getAllARWith-artistName?artistName="+artistName);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsAsync< IEnumerable < ArtisticRepresentation >> ();
			}
			return product;
		}

		static async Task<IEnumerable<ArtisticRepresentation>> GetAllARFromDateAsync(String data)
		{
			string urlParameters = "/getAllARFromDate?data=" + data;
			IEnumerable<ArtisticRepresentation> product = null;
			HttpResponseMessage response = await client.GetAsync(path+urlParameters);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsAsync<IEnumerable<ArtisticRepresentation>>();
			}
			else
				throw new Exception("Couldn't get all ARs from date " + data);
			return product;
		}

		static async Task<ArtisticRepresentation> FindOneARByIdAsync(String id)
		{
			string urlParameters = id;
			ArtisticRepresentation product = null;
			HttpResponseMessage response = await client.GetAsync(path+"/"+id);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsAsync<ArtisticRepresentation>();
			}
			return product;
		}

		static async Task<ArtisticRepresentation> SaveARAsync(ArtisticRepresentation ar)
		{
			ArtisticRepresentation product = null;
			HttpContent content = new StringContent(JsonConvert.SerializeObject(ar,new IsoDateTimeConverter() { DateTimeFormat = "yyyy-MM-dd HH:mm:ss" }), UTF8Encoding.UTF8, "application/json");
			HttpResponseMessage response = await client.PostAsync(path, content);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsAsync<ArtisticRepresentation>();
			}
			else
				throw new Exception("Couldn't save ar " + ar);
			return product;
		}

		static async Task<ArtisticRepresentation> UpdateARAsync(ArtisticRepresentation ar)
		{
			ArtisticRepresentation product = null;
			HttpContent content = new StringContent(JsonConvert.SerializeObject(ar, new IsoDateTimeConverter() { DateTimeFormat = "yyyy-MM-dd HH:mm:ss" }), UTF8Encoding.UTF8, "application/json");
			//HttpResponseMessage response = await client.PutAsync(String.Format("%s/%s", path, ar.id.ToString()), content);
			HttpResponseMessage response = await client.PutAsync(path + "/" + ar.id.ToString(), content);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsAsync<ArtisticRepresentation>();
			}
			else
				throw new Exception("Couldn't update ar " + ar);
			return product;
		}

		static async void DeleteARAsync(String id)
		{
			//HttpResponseMessage response = await client.DeleteAsync(String.Format("%s/%s", path, id));
			HttpResponseMessage response = await client.DeleteAsync(path+"/"+id);
			if (response.IsSuccessStatusCode)
			{
				
			}
			else
				throw new Exception("Couldn't delete ar with id " + id);
		}


		//------------------------
		static void runTests()
		{
			runTestAdd();
			Thread.Sleep(2000);

			runTestFindOneById();
			Thread.Sleep(2000);

			runTestFindAllWithArtistName();
			Thread.Sleep(2000);

			runTestUpdate();
			Thread.Sleep(2000);

			runTestDelete();
			Thread.Sleep(2000);

			runTestGetAllFromDate();
			Thread.Sleep(2000);
		}

		static async void runTestAdd()
		{
			//getAllAR
			List<ArtisticRepresentation> all = (List<ArtisticRepresentation>)await GetARAsync();
			int beforeAdd = all.Count;
			Console.WriteLine(beforeAdd);

			//add 1
			ArtisticRepresentation ar1 = new ArtisticRepresentation(1, "aaa", DateTime.Now, "aaa", 78, 90);
            Console.WriteLine("Saving ar {0}", ar1);
            ArtisticRepresentation ar1S = await SaveARAsync(ar1);
            Console.WriteLine("Recieved {0}", ar1S);

			//add 2
			ArtisticRepresentation ar2 = new ArtisticRepresentation(1, "bbb", DateTime.Now, "bbb", 78, 90);
			Console.WriteLine("Saving ar {0}", ar2);
			ArtisticRepresentation ar2S = await SaveARAsync(ar2);
			Console.WriteLine("Recieved {0}", ar2S);

			//getAllAR
			List<ArtisticRepresentation> all2 = (List<ArtisticRepresentation>)await GetARAsync();
			int afterAdd = all2.Count;
			Console.WriteLine(afterAdd);

			//test
			Debug.Assert(beforeAdd + 2 == afterAdd);

			Console.WriteLine("ADD TESTS SUCCESSFULLY PASSED!");
            Console.WriteLine("\n");
        }

		static async void runTestFindOneById()
		{
			//getAllAR

			List<ArtisticRepresentation> all = (List<ArtisticRepresentation>)await GetARAsync();
			foreach (ArtisticRepresentation ar in all)
			{
				ArtisticRepresentation foundAR = await FindOneARByIdAsync(ar.id.ToString());
				Debug.Assert(foundAR != null);
			}

			Console.WriteLine("findOneById TESTS SUCCESSFULLY PASSED!");
			Console.WriteLine("\n");
		}

		static async void runTestFindAllWithArtistName()
		{
			//getAllAR
			List<ArtisticRepresentation> all = (List<ArtisticRepresentation>)await GetARAsync();
			foreach (ArtisticRepresentation ar in all)
			{
				List<ArtisticRepresentation> foundARs = (List<ArtisticRepresentation>) await GetAllARWithArtistNameAsync(ar.artistName);
				Debug.Assert(foundARs.Count >= 1);
			}

			//add 1
			ArtisticRepresentation ar1 = new ArtisticRepresentation(1, "ccc", DateTime.Now, "aaa", 78, 90);
			Console.WriteLine("Saving ar {0}", ar1);
			ArtisticRepresentation ar1S = await SaveARAsync(ar1);
			Console.WriteLine("Recieved {0}", ar1S);

			//add 2
			ArtisticRepresentation ar2 = new ArtisticRepresentation(1, "ccc", DateTime.Now, "sdfgh", 78, 90);
			Console.WriteLine("Saving ar {0}", ar2);
			ArtisticRepresentation ar2S = await SaveARAsync(ar2);
			Console.WriteLine("Recieved {0}", ar2S);

			//test
			List<ArtisticRepresentation> foundARs2 = (List<ArtisticRepresentation>)await GetAllARWithArtistNameAsync("ccc");
			Debug.Assert(foundARs2.Count >= 2);
			
			Console.WriteLine("findAllARWithArtistName TESTS SUCCESSFULLY PASSED!");
			Console.WriteLine("\n");
		}

		static async void runTestUpdate() 
		{
			//getAllAR
			List<ArtisticRepresentation> all = (List<ArtisticRepresentation>)await GetARAsync();
			foreach (ArtisticRepresentation ar in all)
			{
				long before = ar.availableSeats;
				Console.WriteLine(before + "bbbbbbbb");
				ArtisticRepresentation arr = ar;
				arr.availableSeats = ar.availableSeats + 1;

				await UpdateARAsync(arr);

				ArtisticRepresentation foundAR = await FindOneARByIdAsync(arr.id.ToString());
				Debug.Assert(foundAR.availableSeats == before + 1);
			}
			
			Console.WriteLine("UPDATE TESTS SUCCESSFULLY PASSED!");
			Console.WriteLine("\n");
		}

		static async void runTestDelete()
        {
			//getAllAR
			List<ArtisticRepresentation> all = (List<ArtisticRepresentation>)await GetARAsync();
			int count = 0;
			foreach (ArtisticRepresentation ar in all)
			{
				if (ar.id != 32 && ar.id != 33 && ar.id != 34)
				{
					DeleteARAsync(ar.id.ToString());
					count = count + 1;
				}
			}
			List<ArtisticRepresentation> allAfterDelete = (List<ArtisticRepresentation>)await GetARAsync();
			Debug.Assert(all.Count == allAfterDelete.Count + count);

			Console.WriteLine("DELETE TESTS SUCCESSFULLY PASSED!");
			Console.WriteLine("\n");
		}

		static async void runTestGetAllFromDate()
        {
			//getAllAR
			List<ArtisticRepresentation> all = (List<ArtisticRepresentation>)await GetARAsync();
			foreach (ArtisticRepresentation ar in all)
			{
				List<ArtisticRepresentation> res = (List<ArtisticRepresentation>)await GetAllARFromDateAsync(ar.data.Year+"-"+ar.data.Month+"-"+ar.data.Day);
				Debug.Assert(res.Count >= 1);
			}

			Console.WriteLine("getAllARFromDate TESTS SUCCESSFULLY PASSED!");
			Console.WriteLine("\n");
		}



	}
	public class ArtisticRepresentation
	{
		public long id { get; set; }
		public DateTime data { get; set; }
		public string artistName { get; set; }
		public string location { get; set; }
		public long availableSeats { get; set; } 
		public long soldSeats { get; set; } 

		public ArtisticRepresentation(long id, string artistName, DateTime data, string location, long availableSeats, long soldSeats)
        {
			this.id = id;
			this.artistName = artistName;
			this.data = data;
			this.location = location;
			this.availableSeats = availableSeats;
			this.soldSeats = soldSeats;
        }

		public override string ToString()
		{
			return id + " " + artistName;
		}
	}


	public class DateFormatConverter : IsoDateTimeConverter
	{
		public DateFormatConverter(string format)
		{
			DateTimeFormat = format;
		}
	}

}
