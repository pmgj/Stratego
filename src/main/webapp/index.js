function GUI() {
    var ws = null;
    var player = null;
    var beginCell = null;
    const images = {SPY: "spy", SCOUT: "scout", MINER: "miner", SERGEANT: "sergeant", LIEUTENANT: "lieutenant", CAPTAIN: "captain", MAJOR: "major", COLONEL: "colonel", GENERAL: "general", MARSHAL: "marshal", BOMB: "bomb", FLAG: "flag", ENEMY: "enemy", EMPTY: "empty", LAKE: "lake"};
    const closeCodes = {ENDGAME: {code: 4000, description: "End of game."}, ADVERSARY_QUIT: {code: 4001, description: "The opponent quit the game"}};
    function coordinates(cell) {
        return new Cell(cell.parentNode.rowIndex, cell.cellIndex);
    }
    function setMessage(msg) {
        let message = document.getElementById("message");
        message.innerHTML = msg;
    }
    function readData(evt) {
        let data = JSON.parse(evt.data);
        switch (data.type) {
            case "OPEN":
                /* Informando cor da peça do usuário atual */
                player = data.turn;
                setMessage("Waiting for opponent.");
                break;
            case "MESSAGE":
                /* Recebendo o tabuleiro modificado */
                printBoard(data.board);
                printGraveyard(data.graveyard);
                setMessage(data.turn === player ? "Your turn." : "Opponent's turn.");
                break;
            case "ENDGAME":
                /* Fim do jogo */
                printBoard(data.board);
                printGraveyard(data.graveyard);
                ws.close(closeCodes.ENDGAME.code, closeCodes.ENDGAME.description);
                endGame(data.winner);
                break;
        }
    }
    function endGame(type) {
        unsetEvents();
        ws = null;
        setButtonText(true);
        setMessage(`Game Over! ${(type === "DRAW") ? "Draw!" : (type === player ? "You win!" : "You lose!")}`);
    }
    function setButtonText(on) {
        let button = document.querySelector("input[type='button']");
        button.value = on ? "Start" : "Quit";
    }
    function unsetEvents() {
        let cells = document.querySelectorAll("td");
        cells.forEach(td => td.onclick = undefined);
    }
    function play() {
        if (beginCell === null) {
            beginCell = this;
        } else {
            let begin = coordinates(beginCell);
            let end = coordinates(this);
            beginCell = null;
            ws.send(JSON.stringify({beginCell: begin, endCell: end}));
        }
    }
    function printGraveyard(graveyard) {
        let myG = graveyard.find(g => g.player === player).pieceCount;
        let piecesLost = document.querySelectorAll("#piecesLost tr");
        for (let tableRow of piecesLost) {
            let data = tableRow.dataset.piece;
            let td2 = tableRow.querySelector("td:first-child + td span:first-child");
            let numOfPieces = myG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
            let td3 = tableRow.querySelector("td:first-child + td span:last-child");
            if(td3.textContent === "") {
                td3.textContent = numOfPieces;
            }
        }
        let opG = graveyard.find(g => g.player !== player).pieceCount;
        let piecesCaptured = document.querySelectorAll("#piecesCaptured tr");
        for (let tableRow of piecesCaptured) {
            let data = tableRow.dataset.piece;
            let td2 = tableRow.querySelector("td:first-child + td span:first-child");
            let numOfPieces = opG.find(obj => obj.piece === data).count;
            td2.textContent = numOfPieces;
            let td3 = tableRow.querySelector("td:first-child + td span:last-child");
            if(td3.textContent === "") {
                td3.textContent = numOfPieces;
            }
        }
    }
    function printBoard(matrix) {
        let tbody = document.querySelector("#tabuleiro tbody");
        tbody.innerHTML = "";
        for (let i = 0; i < matrix.length; i++) {
            let tr = document.createElement("tr");
            for (let j = 0; j < matrix[i].length; j++) {
                let td = document.createElement("td");
                td.onclick = play;
                let piece = matrix[i][j];
                switch (piece.player) {
                    case "EMPTY":
                    case "LAKE":
                        td.innerHTML = `<img src='images/stratego-${images[piece.player]}.svg' alt='' title='${images[piece.player]}'>`;
                        break;
                    default:
                        td.innerHTML = `<img src='images/stratego-${images[piece.armyPiece]}.svg' alt='' title='${images[piece.armyPiece]}'>`;
                        break;
                }
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
    }
    function startGame() {
        if (ws) {
            ws.close(closeCodes.ADVERSARY_QUIT.code, closeCodes.ADVERSARY_QUIT.description);
            endGame();
        } else {
            ws = new WebSocket("ws://" + document.location.host + document.location.pathname + "stratego");
            ws.onmessage = readData;
            setButtonText(false);
        }
    }
    function init() {
        let button = document.querySelector("input[type='button']");
        button.onclick = startGame;
        setButtonText(true);
    }

    return {init};
}
onload = function () {
    let gui = new GUI();
    gui.init();
};
