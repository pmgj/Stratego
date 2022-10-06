package controller;

import java.io.IOException;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Cell;
import model.Player;
import model.Stratego;
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
            Message m = new Message(ConnectionType.START_BOARD, Player.PLAYER1, game.hiddenBoard(Player.PLAYER1),
                    game.getGraveyard());
            s1.getBasicRemote().sendObject(m);
            m.setBoard(game.hiddenBoard(Player.PLAYER2));
            s2.getBasicRemote().sendObject(m);
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, MoveMessage message) throws IOException, EncodeException {
        try {
            Cell beginCell = message.getBeginCell(), endCell = message.getEndCell();
            game.move(session == s1 ? Player.PLAYER1 : Player.PLAYER2, beginCell, endCell);
            Message m;
            switch (game.getWinner()) {
                case NONE:
                    m = new Message(ConnectionType.MESSAGE, game.getTurn(), game.getGraveyard());
                    m.setAttackingPiece(game.getAttackingPiece());
                    m.setDefendingPiece(game.getDefendingPiece());
                    m.setAttackingCell(beginCell);
                    m.setDefendingCell(endCell);
                    s1.getBasicRemote().sendObject(m);
                    s2.getBasicRemote().sendObject(m);
                    break;
                default:
                    m = new Message(ConnectionType.ENDGAME, game.getGraveyard(), game.getWinner());
                    m.setAttackingPiece(game.getAttackingPiece());
                    m.setDefendingPiece(game.getDefendingPiece());
                    m.setAttackingCell(beginCell);
                    m.setDefendingCell(endCell);
                    s1.getBasicRemote().sendObject(m);
                    s2.getBasicRemote().sendObject(m);
                    break;
            }    
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
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
                    s2.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, game.getGraveyard(), Winner.PLAYER2));
                    s1 = null;
                } else {
                    s1.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, game.getGraveyard(), Winner.PLAYER1));
                    s2 = null;
                }
                break;
            default:
                System.out.println(String.format("Close code %d incorrect", reason.getCloseCode().getCode()));
        }
    }
}
