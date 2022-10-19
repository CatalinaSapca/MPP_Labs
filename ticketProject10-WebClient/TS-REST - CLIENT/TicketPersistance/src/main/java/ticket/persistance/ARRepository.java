package ticket.persistance;

import ticket.model.ArtisticRepresentation;

public interface ARRepository extends Repository<Long, ArtisticRepresentation> {
    Iterable<ArtisticRepresentation> findAllARWithARName(String artistName);
}
