package rest.client;

import TS.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import services.ServicesException;
import ticket.model.ArtisticRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by grigo on 5/11/17.
 */
public class TSClient {
    public static final String URL = "http://localhost:8080/TS/ArtisticRepresentations";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws ServicesException {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServicesException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Iterable<ArtisticRepresentation> getAllAR() throws ServicesException {
        ArtisticRepresentation[] x = execute(() -> restTemplate.getForObject(URL, ArtisticRepresentation[].class));
        List<ArtisticRepresentation> xx = new ArrayList<>(Arrays.asList(x));
        return xx;
    }

    public Iterable<ArtisticRepresentation> getAllARWithArtistName(String artistName) throws ServicesException {
        ArtisticRepresentation[] x = execute(() -> restTemplate.getForObject(URL+"/getAllARWith-artistName?artistName=" + artistName, ArtisticRepresentation[].class));
        List<ArtisticRepresentation> xx = new ArrayList<>(Arrays.asList(x));
        return xx;
    }

    public Iterable<ArtisticRepresentation> getAllARFromDate(String data) throws ServicesException {
        ArtisticRepresentation[] x = execute(() -> restTemplate.getForObject(URL+"/getAllARFromDate?data="+data, ArtisticRepresentation[].class));
        List<ArtisticRepresentation> xx = new ArrayList<>(Arrays.asList(x));
        return xx;
    }

    public ArtisticRepresentation findOneARById(String id) throws ServicesException {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), ArtisticRepresentation.class));
    }

    public ArtisticRepresentation saveAR(ArtisticRepresentation ar) throws ServicesException {
        return execute(() -> restTemplate.postForObject(URL, ar, ArtisticRepresentation.class));
    }

    public void updateAR(ArtisticRepresentation ar) throws ServicesException {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, ar.getId()), ar);
            return null;
        });
    }

    public void deleteAR(String id) throws ServicesException {
        System.out.println("delete");
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
