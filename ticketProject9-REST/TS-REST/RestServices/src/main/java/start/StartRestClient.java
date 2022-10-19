package start;

import TS.services.rest.ServiceException;
import org.springframework.web.client.RestTemplate;
import rest.client.TSClient;
import services.ServicesException;
import ticket.model.ArtisticRepresentation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StartRestClient {
    private final static TSClient TS_CLIENT = new TSClient();
    public static void main(String[] args) throws IOException, ServicesException {
        RestTemplate restTemplate = new RestTemplate();
        ArtisticRepresentation ar = new ArtisticRepresentation("aaa", LocalDateTime.now(), "Bucharest", 14L, 10);
        ar.setId(1L);

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        System.out.println(objectMapper.writeValueAsString(ar));


        runTests();

    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }

    private static void runTests() throws ServicesException {
        runTestAdd();
        runTestFindOneById();
        runTestFindAllWithArtistName();
        runTestUpdate();
        runTestDelete();
        runTestGetAllFromDate();
    }

    private static void runTestAdd(){
        //getAllAR
        AtomicInteger noOfARsBefore = new AtomicInteger();
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                noOfARsBefore.set(res.size());
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        //add 1
        ArtisticRepresentation ar1 = new ArtisticRepresentation("aaa", LocalDateTime.now(), "Bucharest", 14L, 10);
        ar1.setId(1L);
        show(()-> {
            try {
                TS_CLIENT.saveAR(ar1);
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        //add 2
        ArtisticRepresentation ar2 = new ArtisticRepresentation("bbb", LocalDateTime.now(), "Bucharest", 14L, 10);
        ar2.setId(1L);
        show(()-> {
            try {
                TS_CLIENT.saveAR(ar2);
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        //getAllAR
        AtomicInteger noOfARsAfter = new AtomicInteger();
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                noOfARsAfter.set(res.size());
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        assert (noOfARsBefore.get() - noOfARsAfter.get() == 2);
        System.out.println("ADD TESTS SUCCESSFULLY PASSED!");
    }

    private static void runTestFindOneById(){
        //getAllAR
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                for (ArtisticRepresentation ar: res) {
                    assert (TS_CLIENT.findOneARById(ar.getId().toString()) != null);
                }
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        System.out.println("findOneById TESTS SUCCESSFULLY PASSED!");
    }

    private static void runTestFindAllWithArtistName() throws ServicesException {
        //getAllAR
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                for (ArtisticRepresentation ar: res) {
                    List<ArtisticRepresentation> ress = (List<ArtisticRepresentation>) TS_CLIENT.getAllARWithArtistName(ar.getArtistName());
                    assert (ress.size() >= 1);
                }
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        //add 1
        ArtisticRepresentation ar1 = new ArtisticRepresentation("ccc", LocalDateTime.now(), "Bucharest", 14L, 10);
        ar1.setId(1L);
        show(()-> {
            try {
                TS_CLIENT.saveAR(ar1);
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        //add 2
        ArtisticRepresentation ar2 = new ArtisticRepresentation("ccc", LocalDateTime.now(), "Bucharest", 14L, 10);
        ar2.setId(1L);
        show(()-> {
            try {
                TS_CLIENT.saveAR(ar2);
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });

        //test
        List<ArtisticRepresentation> ress = (List<ArtisticRepresentation>) TS_CLIENT.getAllARWithArtistName("ccc");
        assert (ress.size() >= 2);
        System.out.println("findAllARWithArtistName TESTS SUCCESSFULLY PASSED!");
    }

    private static void runTestUpdate() {
        //getAllAR
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                for (ArtisticRepresentation ar: res) {
                    ArtisticRepresentation arr = ar;
                    arr.setAvailableSeats(arr.getAvailableSeats()+1);
                    TS_CLIENT.updateAR(arr);

                    ArtisticRepresentation afterUpdate =  TS_CLIENT.findOneARById(arr.getId().toString());
                    assert (afterUpdate.getAvailableSeats() == ar.getAvailableSeats() + 1);
                }
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        System.out.println("UPDATE TESTS SUCCESSFULLY PASSED!");
    }

    private static void runTestDelete() {
        //getAllAR
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                int count = 0;
                for (ArtisticRepresentation ar: res) {
                    if(ar.getId()!=32 && ar.getId()!=33 && ar.getId()!=34) {
                        TS_CLIENT.deleteAR(ar.getId().toString());
                        count = count + 1;
                    }
                }
                List<ArtisticRepresentation> ress = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                assert (res.size() == ress.size() + count);

            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        System.out.println("DELETE TESTS SUCCESSFULLY PASSED!");
    }

    private static void runTestGetAllFromDate() {
        //getAllAR
        show(()->{
            List<ArtisticRepresentation> res = null;
            try {
                res = (List<ArtisticRepresentation>) TS_CLIENT.getAllAR();
                for (ArtisticRepresentation ar: res) {
                    List<ArtisticRepresentation> ress = (List<ArtisticRepresentation>) TS_CLIENT.getAllARFromDate(ar.getData().getYear()+"-"+ar.getData().getMonthValue()+"-"+ar.getData().getDayOfMonth());
                    assert (ress.size() >= 1);
                }
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        });
        System.out.println("getAllARFromDate TESTS SUCCESSFULLY PASSED!");
    }

}
