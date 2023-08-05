package controller;

import model.Cell;

public record MoveMessage(Cell beginCell, Cell endCell) {

}