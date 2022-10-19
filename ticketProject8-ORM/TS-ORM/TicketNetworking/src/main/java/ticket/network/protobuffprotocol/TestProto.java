package ticket.network.protobuffprotocol;

import ticket.model.Seller;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: 4/20/14
 * Time: 2:39 AM
 */
public class TestProto {
    public static void main(String[] args) {
        System.out.println("ChatRequest: ");
        try {
            ProtoUtils.createLoginRequest(new Seller("ana","ana")).writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
