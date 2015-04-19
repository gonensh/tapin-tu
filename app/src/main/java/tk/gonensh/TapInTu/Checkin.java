package tk.gonensh.TapInTu;

/**
 * Created by gonen on 4/19/15.
 */

class Checkin {
    private long userId;
    private String timestamp;
    public Checkin() {}
    public Checkin(String timestamp, long userId) {
        this.timestamp = timestamp;
        this.userId = userId;
    }
    public long getUserId() {
        return userId;
    }
    public String getTimestamp() {
        return timestamp;
    }
}