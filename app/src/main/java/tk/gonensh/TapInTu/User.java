package tk.gonensh.TapInTu;

/**
 * Created by gonen on 4/19/15.
 */
class User {
    private String userId;
    private String fullName;

    public User() {}

    public User(String fullName, String userId) {
        this.fullName = fullName;
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    public String getFullName() {
        return fullName;
    }
}
