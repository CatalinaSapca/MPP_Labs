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
import ticket.network.rpcprotocol.TicketSystemServicesRpcProxy;
import services.TicketSystemServices;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/ticketclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find ticketclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("ticket.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("ticket.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        TicketSystemServices server = new TicketSystemServicesRpcProxy(serverIP, serverPort);

        //----
        FXMLLoader mainPageLoader = new FXMLLoader();
        mainPageLoader.setLocation(getClass().getResource("/views/Window.fxml"));
        AnchorPane mainPageLayout = mainPageLoader.load();
        Scene scene = new Scene(mainPageLayout);
        primaryStage.setScene(scene);
        //Service service = getService();
        TicketSystemServices server1 = new TicketSystemServicesRpcProxy(serverIP, serverPort);

        WindowFX windowFX = mainPageLoader.getController();
        TicketClientCtrl controller=new TicketClientCtrl(server);
        windowFX.setController(controller);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                windowFX.logout();
                System.exit(0);
            }
        });
        primaryStage.show();




    }


}


