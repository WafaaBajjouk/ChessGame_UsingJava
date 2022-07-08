package com.chess.gui;

import com.chess.engine.board.*;

import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BAJJOUK WAFAA
 *
 */

public class Table {
	
//	graphic elements gifs 
    private static final String PIECES_PATH = "src/pieces/fancy/";

	
    //	square colors
	    private Color white = Color.decode("#729972");
	    private Color black = Color.decode("#826353");

	    //gui objects

	    //list of captured pieces
    private TakenPiecesPanel takenPiecesPanel;
    
    private BoardPanel boardPanel;
    private Board chessBoard;
    private MoveLog moveLog;
    private Tile source;
    private Tile destination;
    private Piece MovedPiece;
    
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;
    
    //contructure 

    public Table() {
        JFrame Game = new JFrame("Bajjouk wafaa Project");
        Game.setLayout(new BorderLayout());
        
        //objects instantiation

        this.takenPiecesPanel = new TakenPiecesPanel();
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;

        // adding compoments in the frame
        Game.setSize(600, 600);
        Game.add(this.takenPiecesPanel, BorderLayout.WEST);
        Game.add(this.boardPanel, BorderLayout.CENTER);

        Game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game.setVisible(true);
    }


    private class BoardPanel extends JPanel {

        List<TilePanel> boardTiles;

        //contructure
        BoardPanel() {
        	//In the chess board we have 64 squares 
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<TilePanel>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            	
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(new Dimension(400, 350));
            validate();
        }

        private void drawBoard(Board board) {
            removeAll();
            for (TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    /**
     * The Move Log class used for GameHistoryPanel
     */
    static class MoveLog {

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

    /**
     * The Tile Panel  with the MouseListener gestion 
     */
    public class TilePanel extends JPanel {

        private int tileId;
        
        //constructure

        TilePanel(BoardPanel boardPanel, int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(new Dimension(10, 10));
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        source = null;
                        destination = null;
                        MovedPiece = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (source == null) {
                            source = chessBoard.getTile(tileId);
                            MovedPiece = source.getPiece();
                            if (MovedPiece == null) {
                                source = null;
                            }
                        } else {
                            if (source == null) {
                                source = chessBoard.getTile(tileId);
                                MovedPiece = source.getPiece();
                                if (MovedPiece == null) {
                                    source = null;
                                }
                            } else {
                                destination = chessBoard.getTile(tileId);
                                Move move = MoveFactory.createMove(chessBoard, source.getTileCoordinate(),
                                        destination.getTileCoordinate());
                                MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                                if (transition.getMoveStatus().isDone()) {
                                    chessBoard = transition.getToBoard();
                                    moveLog.addMove(move);
                                    System.out.println(chessBoard);
                                }
                                source = null;
                                destination = null;
                                MovedPiece = null;
                            }
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                takenPiecesPanel.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // ignored
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // ignored
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // ignored
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // ignored
                }
            });
            validate();
        }

        /**
         * Drawing the tiles
         *
         * @param board The board
         */
        public void drawTile(Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    System.out.println(getPath(board));
                    BufferedImage image = ImageIO.read(new File(getPath(board)));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException ex) {
                    Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * Highlighting the legal moves
         *
         * @param board The board
         */
        private void highlightLegals(Board board) {
            if (highlightLegalMoves) {
                for (Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("src/misc/green_dot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        /**
         * Gets the legal moves for each piece
         *
         * @param board The board
         * @return Iterable of move
         */
        private Iterable<Move> pieceLegalMoves(Board board) {
            if (MovedPiece != null && MovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return MovedPiece.calculateLegalMoves(board);
            }

            return Collections.emptyList();
        }

        /**
         * Assigning the tile chess color
         */
        private void assignTileColor() {
            if (BoardUtils.FIRST_ROW[this.tileId] ||
                    BoardUtils.THIRD_ROW[this.tileId] ||
                    BoardUtils.FIFTH_ROW[this.tileId] ||
                    BoardUtils.SEVENTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? white : black);
            } else if (BoardUtils.SECOND_ROW[this.tileId] ||
                    BoardUtils.FOURTH_ROW[this.tileId] ||
                    BoardUtils.SIXTH_ROW[this.tileId] ||
                    BoardUtils.EIGHTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? white : black);
            }
        }

        private String getPath(Board board) {
            return PIECES_PATH
                    + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1)
                    + board.getTile(this.tileId).getPiece().toString() + ".gif";
        }
    }
}
