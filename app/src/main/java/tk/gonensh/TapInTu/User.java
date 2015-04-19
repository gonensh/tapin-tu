package tk.gonensh.TapInTu;

/**
 * Created by gonen on 4/19/15.
 */
class User {
    private long userId;
    private String fullName;

    public User() {}

    public User(String fullName, long userId) {
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
