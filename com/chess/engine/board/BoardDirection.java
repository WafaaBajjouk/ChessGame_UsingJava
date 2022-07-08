package com.chess.engine.board;

import com.chess.gui.Table.TilePanel;
import com.google.common.collect.Lists;

import java.util.List;


public enum BoardDirection {
    NORMAL {
        @Override
        public List<TilePanel> traverse(List<TilePanel> boardTiles) {
            return boardTiles;
        }

        @Override
        public BoardDirection opposite() {
            return FLIPPED;
        }

    },
    FLIPPED {
        @Override
        public List<TilePanel> traverse(List<TilePanel> boardTiles) {
            return Lists.reverse(boardTiles);
        }

        @Override
        public BoardDirection opposite() {
            return NORMAL;
        }
    };

    public abstract List<TilePanel> traverse(List<TilePanel> boardTiles);

    public abstract BoardDirection opposite();
}