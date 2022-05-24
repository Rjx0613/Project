package model;

import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueenChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image queenImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./images/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./images/queen-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, Chessboard chessboard) {
        super(chessboardPoint, location, color, listener, size,chessboard);
        initiateQueenImage(color);
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint,ChessColor color){
        super(chessboardPoint,color);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getX() + source.getY() == destination.getX() + destination.getY()) {
            int row1 = Math.max(source.getX(), destination.getX());
            int row2 = Math.min(source.getX(), destination.getX());
            int col1 = Math.max(source.getY(), destination.getY());
            int col2 = Math.min(source.getY(), destination.getY());
            for (int i = row1 - 1, j = col2 + 1; i > row2 && j < col1; i--, j++) {
                if (!(chessComponents[i][j] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getX() - source.getY() == destination.getX() - destination.getY()) {
            int row1 = Math.max(source.getX(), destination.getX());
            int row2 = Math.min(source.getX(), destination.getX());
            int col1 = Math.max(source.getY(), destination.getY());
            int col2 = Math.min(source.getY(), destination.getY());
            for (int i = row2 + 1, j = col2 + 1; i < row1 && j < col1; i++, j++) {
                if (!(chessComponents[i][j] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else { // Not on the same row or the same column.
            return false;
        }
        return true;
    }

    @Override
    public List<ChessboardPoint> trace() {
        int x = getChessboardPoint().getX();
        int y = getChessboardPoint().getY();
        ChessComponent[][] chessboard = ChessGameFrame.gameController.getChessboard().getChessComponents();
        List<ChessboardPoint> answer = new ArrayList<>();
        for (int j = y + 1; j < 8; j++) {
            if (chessboard[x][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(x, j));
            } else if (chessboard[x][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[x][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(x, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x - 1; i >= 0; i--) {
            if (chessboard[i][y] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, y));
            } else if (chessboard[i][y].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][y].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, y));
                break;
            } else {
                break;
            }
        }
        for (int j = y - 1; j >= 0; j--) {
            if (chessboard[x][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(x, j));
            } else if (chessboard[x][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[x][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(x, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x + 1; i < 8; i++) {
            if (chessboard[i][y] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, y));
            } else if (chessboard[i][y].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][y].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, y));
                break;
            } else {
                break;
            }
        }
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        return answer;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);

        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        if (isTrace()) {
            g.setColor(Color.BLUE);
            g.fillOval(0, 0, getWidth(), getHeight());
            this.setTrace(false);
        }g.drawImage(queenImage, 0, 0, getWidth(), getHeight(), this);
    }
}
