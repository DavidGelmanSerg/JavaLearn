package controller;

public class SapperControllerFactory {
    public static SapperController getSapperController(SapperType type) {
        return switch (type) {
            case CLASSIC -> new ClassicSapperController();
        };
    }
}
