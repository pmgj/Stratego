package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.json.bind.JsonbBuilder;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import controller.Message;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import model.Cell;
import model.Piece;
import model.Player;

@ClientEndpoint
public class MainWindow extends JFrame {

    private final Square[][] board = new Square[10][10];
    private JLabel lMessage;
    private final String[] imageNames = { "spy", "scout", "miner", "sergeant", "lieutenant", "captain", "major",
            "colonel", "general", "marshal", "bomb", "flag", "enemy", "empty", "lake" };
    private Player player;
    private Cell beginCell;
    private Session session;

    public MainWindow() {
        super("Stratego");
    }

    @OnOpen
    public void onOpen(Session s) throws IOException {
        this.session = s;
    }

    @OnMessage
    public void onMessage(Session s, String message) throws IOException {
        System.out.println(message);
        Message msg = JsonbBuilder.create().fromJson(message, Message.class);
        switch (msg.getType()) {
            case OPEN:
                /* Informando cor da peça do usuário atual */
                this.player = msg.getTurn();
                break;
            case START_BOARD:
            case MESSAGE:
                /* Recebendo o tabuleiro modificado */
                String info = (this.player == msg.getTurn()) ? "É a sua vez de jogar."
                        : "É a vez do adversário de jogar.";
                this.player = msg.getTurn();
                lMessage.setText(info);
                this.atualizarTabuleiro(msg.getBoard());
                break;
            case QUIT_GAME:
                /* Fim do jogo */
                String info2 = (msg.getTurn() == player) ? "Você venceu!" : "Você perdeu!";
                lMessage.setText(info2);
                s.close();
                break;
        }
    }

    private void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://localhost:8080/WebSockets-Stratego/endpoint";
            container.connectToServer(this, URI.create(uri));
        } catch (DeploymentException | IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizarTabuleiro(Piece[][] pecas) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Square b = board[i][j];
                int index = pecas[i][j].getArmyPiece().ordinal();
                ImageIcon icon = new ImageIcon(String.format("web/images/stratego-%s.png", imageNames[index]));
                b.setIcon(icon);
                b.setToolTipText(pecas[i][j].getArmyPiece().name());
            }
        }
    }

    private void createWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel chessPanel = new JPanel(new GridLayout(0, 10));
        chessPanel.setBorder(new LineBorder(Color.BLACK));
        this.add(chessPanel);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Square b = new Square(i, j);
                ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                board[i][j] = b;
                chessPanel.add(board[i][j]);
                b.addActionListener((ae) -> {
                    Square button = (Square) ae.getSource();
                    if (beginCell == null) {
                        beginCell = new Cell(button.getRow(), button.getCol());
                    } else {
                        try {
                            this.session.getBasicRemote().sendText("");
                            // this.session.getBasicRemote().sendText(mapper.writeValueAsString(coords));
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                        beginCell = null;
                    }
                });
            }
        }

        JPanel bottomPanel = new JPanel();
        lMessage = new JLabel("");
        bottomPanel.add(lMessage);
        this.add(bottomPanel);

        this.pack();
        this.connect();
    }

    public static void main(String[] args) throws IOException {
        MainWindow mw = new MainWindow();
        mw.createWindow();
    }
}
