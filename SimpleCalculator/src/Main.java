import controller.SapperController;
import model.SapperModel;
import view.ui.SapperGameFrame;

public class Main {
    public static void main(String[] args) {
        SapperModel model = new SapperModel();
        SapperGameFrame view = new SapperGameFrame(600, 600);
        model.addSapperListener(view);

        SapperController controller = new SapperController(model);
        controller.setModel(model);
        view.setController(controller);

        model.start();
    }
}



