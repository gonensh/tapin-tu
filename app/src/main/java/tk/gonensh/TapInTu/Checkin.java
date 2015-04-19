package tk.gonensh.TapInTu;

/**
 * Created by gonen on 4/19/15.
 */
/*public class Checkin {
    Long id;
    String timestamp;
    Long userId;

    public Checkin(Long userId){
        this.userId = userId;
        this.timestamp = new Date().toString();
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }
}
*/
class Checkin {
    private long userId;
    private String fullName;
    public Checkin() {}
    public Checkin(String fullName, long userId) {
        this.fullName = fullName;
        this.userId = userId;
    }
    public long getUserId() {
        return userId;
    }
    public String getFullName() {
        return fullName;
    }
}