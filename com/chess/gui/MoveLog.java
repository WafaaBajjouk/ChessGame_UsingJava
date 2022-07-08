package com.chess.gui;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.board.Move;

public  class MoveLog {

    private List<Move> moves;

    MoveLog() {
        this.moves = new ArrayList<Move>();
    }

    List<Move> getMoves() {
        return this.moves;
    }

    void addMove(Move move) {
        this.moves.add(move);
    }

    int size() {
        return this.moves.size();
    }
}
