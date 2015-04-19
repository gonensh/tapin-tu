package tk.gonensh.TapInTu;

/** NOT REALY USED SO FAR
 * Created by gonen on 4/19/15.
 */

class Checkin {
    private long userId;
    private String timestamp;
    public Checkin() {}
    public Checkin(long userId, String timestamp) {
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