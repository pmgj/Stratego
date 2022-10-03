import Cell from "./Cell.js";

class GUI {
    constructor() {
        this.ws = null;
        this.player = null;
        this.beginCell = null;
        this.images = { SPY: "spy", SCOUT: "scout", MINER: "miner", SERGEANT: "sergeant", LIEUTENANT: "lieutenant", CAPTAIN: "captain", MAJOR: "major", COLONEL: "colonel", GENERAL: "general", MARSHAL: "marshal", BOMB: "bomb", FLAG: "flag", ENEMY: "enemy", EMPTY: "empty", LAKE: "lake" };
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
            let td2 = tableRow.querySelector("td:first-child + td span:first-child");
            let numOfPieces = myG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
            let td3 = tableRow.querySelector("td:first-child + td span:last-child");
            if (td3.textContent === "") {
                td3.textContent = numOfPieces;
            }
        }
        let opG = graveyard.find(g => g.player !== this.player).pieceCount;
        let piecesCaptured = document.querySelectorAll("#piecesCaptured tr");
        for (let tableRow of piecesCaptured) {
            let data = tableRow.dataset.piece;
            let td2 = tableRow.querySelector("td:first-child + td span:first-child");
            let numOfPieces = opG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
            let td3 = tableRow.querySelector("td:first-child + td span:last-child");
            if (td3.textContent === "") {
                td3.textContent = numOfPieces;
            }
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
                let piece = matrix[i][j];
                switch (piece.player) {
                    case "EMPTY":
                    case "LAKE":
                        td.innerHTML = `<img src='images/stratego-${this.images[piece.player]}.svg' alt='' title='${this.images[piece.player]}'>`;
                        break;
                    default:
                        td.innerHTML = `<img src='images/stratego-${this.images[piece.armyPiece]}.svg' alt='' title='${this.images[piece.armyPiece]}'>`;
                        break;
                }
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
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
    }
}
let gui = new GUI();
gui.init();
