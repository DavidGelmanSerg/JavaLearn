import controller.SapperController;
import controller.SapperControllerFactory;
import controller.SapperType;
import model.SapperModel;
import view.ui.SapperGameFrame;

public class Main {
    public static void main(String[] args) {
        SapperModel model = new SapperModel();
        SapperGameFrame view = new SapperGameFrame(600, 600);
        model.addPropertyChangeListener(view);

        SapperController controller = SapperControllerFactory.getSapperController(SapperType.CLASSIC);
        controller.setModel(model);
        view.setController(controller);

        model.start();
    }
}



