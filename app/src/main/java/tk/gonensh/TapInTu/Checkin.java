package tk.gonensh.TapInTu;

import java.util.Date;
import java.util.UUID;

/**
 * Created by gonen on 4/19/15.
 */
public class Checkin {
    Long id;
    String timestamp;
    Long userId;

    public Checkin(Long userId){
        this.userId = userId;
        this.timestamp = new Date().toString();
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }
}
