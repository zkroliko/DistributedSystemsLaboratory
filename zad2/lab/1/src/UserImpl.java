
public class UserImpl implements User {

    public String nick;

    public UserImpl(String nick) {
        this.nick = nick;
    }

    @Override
    public String getNick() {
        return nick;
    }
}
