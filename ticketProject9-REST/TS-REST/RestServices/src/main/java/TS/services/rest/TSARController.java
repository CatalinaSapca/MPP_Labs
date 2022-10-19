package TS.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket.model.ArtisticRepresentation;
import ticket.persistance.ARRepository;
import ticket.persistance.RepositoryException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/TS/ArtisticRepresentations")
public class TSARController {

    private static final String template = "Hello, %s!";

    @Autowired
    private ARRepository repoAR;
    //private ARRepositoryREST repoAR;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }

    @RequestMapping( method=RequestMethod.GET)
    public Iterable<ArtisticRepresentation> getAllAR() throws SQLException {
        return repoAR.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getARById(@PathVariable String id){
        ArtisticRepresentation ar = repoAR.findOne(Long.parseLong(id));
        if (ar == null)
            return new ResponseEntity<String>("ArtisticRepresentation not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<ArtisticRepresentation>(ar, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ArtisticRepresentation saveAR(@RequestBody ArtisticRepresentation ar){
        ArtisticRepresentation response = repoAR.save(ar);
        return ar;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ArtisticRepresentation updateAR(@RequestBody ArtisticRepresentation ar) {
        System.out.println("Updating ArtisticRepresentation ...");
        repoAR.update(ar);
        return ar;
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> deleteAR(@PathVariable String id){
        System.out.println("Deleting artistic representation ... "+id);
        try {
            repoAR.delete(Long.parseLong(id));
            return new ResponseEntity<ArtisticRepresentation>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete ar exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getAllARWith-artistName")
    public Iterable<ArtisticRepresentation> findAllARWithName(@RequestParam String artistName){
        Iterable<ArtisticRepresentation> result = repoAR.findAllARWithARName(artistName);
        System.out.println("Result ..." + result.toString());

        return result;
    }

    @GetMapping("getAllARFromDate")
    public Iterable<ArtisticRepresentation> findAllARFromDate(@RequestParam String data) throws SQLException {
        String[] parts = data.split("-");
        LocalDateTime date = LocalDateTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 0, 0, 0);
        ArrayList<ArtisticRepresentation> all=new ArrayList<>();
        this.repoAR.findAll().forEach(x->{
            if(x.getData().getYear()==date.getYear() && x.getData().getMonth()==date.getMonth() && x.getData().getDayOfMonth()==date.getDayOfMonth())
                all.add(x);
        });
        System.out.println("Result ..." + all.toString());
        return all;
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
