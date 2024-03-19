package software.example;

public class Main {
    public static void main(String[] args) throws Exception {
        HandlerWithAutomaticPriming handler = new HandlerWithAutomaticPriming();
        handler.beforeCheckpoint(null);
        handler.handleRequest(null, null);
    }
}
