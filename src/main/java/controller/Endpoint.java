package controller;

import model.Stratego;
import java.io.IOException;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Cell;
import model.Move;
import model.Player;
import model.Winner;

@ServerEndpoint(value = "/stratego", encoders = MessageEncoder.class, decoders = MoveMessageDecoder.class)
public class Endpoint {

    private static Session s1;
    private static Session s2;
    private static Stratego game;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        if (s1 == null) {
            s1 = session;
            s1.getBasicRemote().sendObject(new Message(ConnectionType.OPEN, Player.PLAYER1));
        } else if (s2 == null) {
            game = new Stratego();
            s2 = session;
            s2.getBasicRemote().sendObject(new Message(ConnectionType.OPEN, Player.PLAYER2));
            Message m = new Message(ConnectionType.MESSAGE, Player.PLAYER1, game.hiddenBoard(Player.PLAYER1), game.getGraveyard());
            s1.getBasicRemote().sendObject(m);
            m.setBoard(game.hiddenBoard(Player.PLAYER2));
            s2.getBasicRemote().sendObject(m);
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, MoveMessage message) throws IOException, EncodeException {
        Cell beginCell = message.getBeginCell(), endCell = message.getEndCell();
        Move ret = game.move(session == s1 ? Player.PLAYER1 : Player.PLAYER2, beginCell, endCell);
        Message m;
        switch (ret) {
            case VALID:
                m = new Message(ConnectionType.MESSAGE, game.getTurn(), game.hiddenBoard(Player.PLAYER1), game.getGraveyard());
                s1.getBasicRemote().sendObject(m);
                m.setBoard(game.hiddenBoard(Player.PLAYER2));
                s2.getBasicRemote().sendObject(m);
                break;
            case INVALID:
                break;
            default:
                Winner winner = ret == Move.PLAYER1_WIN ? Winner.PLAYER1 : ret == Move.PLAYER2_WIN ? Winner.PLAYER2 : Winner.DRAW;
                m = new Message(ConnectionType.ENDGAME, game.hiddenBoard(Player.PLAYER1), game.getGraveyard(), winner);
                s1.getBasicRemote().sendObject(m);
                m.setBoard(game.hiddenBoard(Player.PLAYER2));
                s2.getBasicRemote().sendObject(m);
                break;
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException, EncodeException {
        switch (reason.getCloseCode().getCode()) {
            case 4000:
                if (session == s1) {
                    s1 = null;
                } else {
                    s2 = null;
                }
                break;
            case 4001:
                if (session == s1) {
                    s2.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, game.hiddenBoard(Player.PLAYER2), game.getGraveyard(), Winner.PLAYER2));
                    s1 = null;
                } else {
                    s1.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, game.hiddenBoard(Player.PLAYER1), game.getGraveyard(), Winner.PLAYER1));
                    s2 = null;
                }
                break;
            default:
                System.out.println(String.format("Close code %d incorrect", reason.getCloseCode().getCode()));
        }
    }
}
