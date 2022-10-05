import Cell from "./Cell.js";
import ArmyPiece from "./ArmyPiece.js";
import PieceType from "./PieceType.js";

class GUI {
    constructor() {
        this.ws = null;
        this.player = null;
        this.beginCell = null;
        this.closeCodes = { ENDGAME: { code: 4000, description: "End of game." }, ADVERSARY_QUIT: { code: 4001, description: "The opponent quit the game" } };
    }
    coordinates(cell) {
        return new Cell(cell.parentNode.rowIndex, cell.cellIndex);
    }
    setMessage(msg) {
        let message = document.getElementById("message");
        message.innerHTML = msg;
    }
    readData(evt) {
        let data = JSON.parse(evt.data);
        switch (data.type) {
            case "OPEN":
                /* Informando cor da peça do usuário atual */
                this.player = data.turn;
                this.setMessage("Waiting for opponent.");
                break;
            case "MESSAGE":
                /* Recebendo o tabuleiro modificado */
                this.printBoard(data.board);
                this.printGraveyard(data.graveyard);
                this.setMessage(data.turn === this.player ? "Your turn." : "Opponent's turn.");
                break;
            case "ENDGAME":
                /* Fim do jogo */
                this.printBoard(data.board);
                this.printGraveyard(data.graveyard);
                this.ws.close(closeCodes.ENDGAME.code, closeCodes.ENDGAME.description);
                this.endGame(data.winner);
                break;
        }
    }
    endGame(type) {
        this.unsetEvents();
        this.ws = null;
        this.setButtonText(true);
        this.setMessage(`Game Over! ${(type === "DRAW") ? "Draw!" : (type === this.player ? "You win!" : "You lose!")}`);
    }
    setButtonText(on) {
        let button = document.querySelector("input[type='button']");
        button.value = on ? "Start" : "Quit";
    }
    unsetEvents() {
        let cells = document.querySelectorAll("td");
        cells.forEach(td => td.onclick = undefined);
    }
    play(evt) {
        let td = evt.currentTarget;
        if (this.beginCell === null) {
            this.beginCell = td;
        } else {
            let begin = this.coordinates(this.beginCell);
            let end = this.coordinates(td);
            this.beginCell = null;
            this.ws.send(JSON.stringify({ beginCell: begin, endCell: end }));
        }
    }
    printGraveyard(graveyard) {
        let myG = graveyard.find(g => g.player === this.player).pieceCount;
        let piecesLost = document.querySelectorAll("#piecesLost tr");
        for (let tableRow of piecesLost) {
            let data = tableRow.dataset.piece;
            let td2 = tableRow.querySelector("td:last-child");
            let numOfPieces = myG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
        }
        let opG = graveyard.find(g => g.player !== this.player).pieceCount;
        let piecesCaptured = document.querySelectorAll("#piecesCaptured tr");
        for (let tableRow of piecesCaptured) {
            let data = tableRow.dataset.piece;
            let td2 = tableRow.querySelector("td:last-child");
            let numOfPieces = opG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
        }
    }
    printBoard(matrix) {
        let tbody = document.querySelector("#tabuleiro tbody");
        tbody.innerHTML = "";
        for (let i = 0; i < matrix.length; i++) {
            let tr = document.createElement("tr");
            for (let j = 0; j < matrix[i].length; j++) {
                let td = document.createElement("td");
                td.onclick = this.play.bind(this);
                let piece = matrix[i][j], text;
                switch (piece.player) {
                    case PieceType.EMPTY:
                    case PieceType.LAKE:
                        text = piece.player.toLowerCase();
                        td.innerHTML = `<img src="images/stratego-${text}.svg" alt="${text}" title="${text}">`;
                        break;
                    default:
                        text = piece.armyPiece.toLowerCase();
                        td.innerHTML = `<img src="images/stratego-${text}.svg" alt="${text}" title="${text}">`;
                        break;
                }
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
    }
    printStartGraveyard() {
        let piecesLost = document.querySelector("#piecesLost tbody");
        let piecesCaptured = document.querySelector("#piecesCaptured tbody");
        let str = "";
        for (let piece in ArmyPiece) {
            if(piece !== ArmyPiece.ENEMY && piece !== ArmyPiece.FLAG) {
                str += `<tr data-piece="${piece}"><td><img src="images/stratego-${piece.toLowerCase()}.svg" alt="${piece.toLowerCase()}" title="${piece.toLowerCase()}" /></td><td></td></tr>`;
            }
        }
        piecesLost.innerHTML = str;
        piecesCaptured.innerHTML = str;
    }
    startGame() {
        if (this.ws) {
            this.ws.close(closeCodes.ADVERSARY_QUIT.code, closeCodes.ADVERSARY_QUIT.description);
            this.endGame();
        } else {
            this.ws = new WebSocket(`ws://${document.location.host}${document.location.pathname}stratego`);
            this.ws.onmessage = this.readData.bind(this);
            this.setButtonText(false);
        }
    }
    init() {
        let button = document.querySelector("input[type='button']");
        button.onclick = this.startGame.bind(this);
        this.setButtonText(true);
        this.printStartGraveyard();
    }
}
let gui = new GUI();
gui.init();
