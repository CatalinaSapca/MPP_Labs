package ticket.network.objectprotocol;

import java.time.LocalDateTime;

public class GetAllARFromDateRequest implements Request {
    private LocalDateTime date;

    public GetAllARFromDateRequest(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }
}