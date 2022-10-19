package ticket.client;

import ticket.client.gui.TicketClientCtrl;
import ticket.client.gui.WindowFX;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.TicketSystemServices;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;


public class StartRMIClientFX extends Application {
    private Stage primaryStage;

    private static String defaultServer="localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        Properties clientProps=new Properties();
        try {
            clientProps.load(StartRMIClientFX.class.getResourceAsStream("/ticketclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find ticketclient.properties "+e);
            return;
        }

        String name=clientProps.getProperty("ticket.rmi.serverID","Ticket");
        String serverIP=clientProps.getProperty("ticket.server.host",defaultServer);

        try {
            Registry registry = LocateRegistry.getRegistry(serverIP);
            TicketSystemServices server = (TicketSystemServices) registry.lookup(name);
            System.out.println("Obtained a reference to remote TicketSystem server");

            TicketClientCtrl controller = new TicketClientCtrl(server);


            //----
            FXMLLoader mainPageLoader = new FXMLLoader();
            mainPageLoader.setLocation(getClass().getResource("/views/Window.fxml"));
            AnchorPane mainPageLayout = mainPageLoader.load();
            Scene scene = new Scene(mainPageLayout);
            primaryStage.setScene(scene);

            WindowFX windowFX = mainPageLoader.getController();
            windowFX.setController(controller);

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    windowFX.logout();
                    System.exit(0);
                }
            });
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("TicketSystem Initialization  exception:"+e);
            e.printStackTrace();
        }




    }


}


