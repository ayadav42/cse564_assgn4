import java.util.List;

public class Logger {

    private static Logger instance;
    private List<String>messages;

    public Logger getInstance(String logFile){
        
            if(instance==null){
                instance = new Logger();
            }
        
        return instance;
    }

    public void setMessage(String message) {
        this.cityList.add(0,message);
    }

    public String getMessage() {
        return this.messages.get(0);
    }
}