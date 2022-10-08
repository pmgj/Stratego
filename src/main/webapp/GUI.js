import Cell from "./Cell.js";
import ArmyPiece from "./ArmyPiece.js";
import PieceType from "./PieceType.js";
import ConnectionType from "./ConnectionType.js";
import Winner from "./Winner.js";

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
    getTableData({ x, y }) {
        let board = document.querySelector("#board");
        return board.rows[x].cells[y];
    }
    readData(evt) {
        let data = JSON.parse(evt.data);
        switch (data.type) {
            case ConnectionType.OPEN:
                /* Informando cor da peça do usuário atual */
                this.player = data.turn;
                this.setMessage("Waiting for opponent.");
                break;
            case ConnectionType.START_BOARD:
                /* Recebendo o tabuleiro */
                this.printBoard(data.board);
                this.printGraveyard(data.graveyard);
                this.setMessage(data.turn === this.player ? "Your turn." : "Opponent's turn.");
                break;
            case ConnectionType.MESSAGE:
                /* Recebendo uma jogada */
                this.movePiece(data);
                break;
            case ConnectionType.ENDGAME:
                /* Fim do jogo */
                this.printBoard(data.board);
                this.printGraveyard(data.graveyard);
                this.ws.close(this.closeCodes.ENDGAME.code, this.closeCodes.ENDGAME.description);
                this.endGame(data.winner);
                break;
        }
    }
    movePiece(data) {
        const time = 1000;
        let beginCell = data.attackingCell;
        let endCell = data.defendingCell;
        let attackingPiece = data.attackingPiece;
        let defendingPiece = data.defendingPiece;
        let moveImage = (destinationCell, piece) => {
            destinationCell.innerHTML = "";
            destinationCell.appendChild(piece);
        };
        let animatePiece = (startPosition, endPosition) => {
            let bTD = this.getTableData(startPosition);
            let eTD = this.getTableData(endPosition);
            let piece = bTD.firstChild;
            let { x: a, y: b } = startPosition;
            let { x: c, y: d } = endPosition;
            let td = document.querySelector("#board td");
            let size = td.offsetWidth;
            let anim = piece.animate([{ top: 0, left: 0 }, { top: `${(c - a) * size}px`, left: `${(d - b) * size}px` }], time);
            anim.onfinish = () => moveImage(eTD, piece);
        };
        let removePiece = td => {
            let img = td.firstChild;
            img?.animate([{ opacity: 1 }, { opacity: 0 }], time);
        };
        let showPiece = (cell, piece, remove) => {
            let td = this.getTableData(cell);
            let img = td.firstChild;
            let source = img.src;
            img.src = `images/stratego-${piece.armyPiece.toLowerCase()}.svg`;
            setTimeout(() => {
                if (remove) {
                    removePiece(td);
                } else {
                    img.src = source;
                }
            }, time);
        };
        if (data.attackResult === Winner.NONE) {
            animatePiece(beginCell, endCell);
            let eTD = this.getTableData(endCell);
            removePiece(eTD);
        } else if (data.attackResult === Winner.DRAW) {
            showPiece(beginCell, attackingPiece, true);
            showPiece(endCell, defendingPiece, true);
        } else {
            if (attackingPiece.player === data.attackResult) {
                showPiece(beginCell, attackingPiece, false);
                showPiece(endCell, defendingPiece, true);
                animatePiece(beginCell, endCell);
            } else {
                showPiece(beginCell, attackingPiece, true);
                showPiece(endCell, defendingPiece, false);
            }
        }
        this.printGraveyard(data.graveyard);
        this.setMessage(data.turn === this.player ? "Your turn." : "Opponent's turn.");
    }
    endGame(type) {
        this.unsetEvents();
        this.ws = null;
        this.setButtonText(true);
        this.setMessage(`Game Over! ${(type === Winner.DRAW) ? "Draw!" : (type === this.player ? "You win!" : "You lose!")}`);
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
            let td2 = tableRow.querySelector("td:first-child");
            let numOfPieces = opG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
        }
    }
    printBoard(matrix) {
        let tbody = document.querySelector("#board tbody");
        tbody.innerHTML = "";
        for (let i = 0; i < matrix.length; i++) {
            let tr = document.createElement("tr");
            for (let j = 0; j < matrix[i].length; j++) {
                let td = document.createElement("td");
                td.onclick = this.play.bind(this);
                let piece = matrix[i][j], text;
                switch (piece.player) {
                    case PieceType.EMPTY:
                        break;
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
        let str1 = "", str2 = "";
        for (let piece in ArmyPiece) {
            if (piece !== ArmyPiece.ENEMY && piece !== ArmyPiece.FLAG) {
                str1 += `<tr data-piece="${piece}"><td><img src="images/stratego-${piece.toLowerCase()}.svg" alt="${piece.toLowerCase()}" title="${piece.toLowerCase()}" /></td><td></td></tr>`;
                str2 += `<tr data-piece="${piece}"><td></td><td><img src="images/stratego-${piece.toLowerCase()}.svg" alt="${piece.toLowerCase()}" title="${piece.toLowerCase()}" /></td></tr>`;
            }
        }
        piecesLost.innerHTML = str1;
        piecesCaptured.innerHTML = str2;
    }
    startGame() {
        if (this.ws) {
            this.ws.close(this.closeCodes.ADVERSARY_QUIT.code, this.closeCodes.ADVERSARY_QUIT.description);
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
