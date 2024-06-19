import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
/* import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter; */

public class GUI extends JFrame
{
    private Board board;
    private JPanel boardPanel;
    private JPanel mainPanel;
    private JLabel currentPlayerLabel;
    private Piece selectedPiece = null;
    private int selectedX = -1;
    private int selectedY = -1;
    private int currentPlayer = 1; // 1 for starting with Player 1, 2 for starting with Player 2
    ArrayList<Piece> capturedPieces = new ArrayList<>();

    public GUI()
    {
        board = new Board();
        setup_GUI();
    }

    private void setup_GUI()
    {
        setTitle("Checkers Game");
        setSize(600, 650); // Increased height to accommodate the button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating the main panel with padding and brown border
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 10));
        mainPanel.setBackground(new Color(244, 164, 96));

        // Creating the player indicator label with padding
        currentPlayerLabel = new JLabel("Current Player: Player 1");
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentPlayerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(currentPlayerLabel, BorderLayout.NORTH);

        // Creating the board panel with padding
        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        update_Board();
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // Creating the End Game button with padding
        JButton endGameButton = new JButton("End Game");
        endGameButton.addActionListener(e -> endGame(0));
        endGameButton.setPreferredSize(new Dimension(100, 30));
        endGameButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(endGameButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private void update_Board()
    {
        Color dark = new Color(139, 69, 19);    // Dark wood color
        Color light = new Color(222, 184, 135); // Light wood color

        Color player1 = new Color(165, 42, 42);
        Color player2 = new Color(34, 139, 34);

        Color highlight = new Color(255, 223, 186);
        

        boardPanel.removeAll();
        for (int i = 7; i >= 0; i--)
        {
            for (int j = 0; j < 8; j++)
            {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(50, 50));
                cell.addActionListener(new CellClickListener(j, i));
                if (!board.check_if_available(j, i))
                {
                    Piece piece = board.get_piece_at(j, i);
                    if (board.owner_of_piece_at(j, i) == 1)
                    {
                        if (piece.get_type() == 1)
                        {
                            cell.setIcon(new GUI_Piece_Icon(player2, true));
                            cell.setBackground(light);
                        }
                        else if (piece.get_type() == 2)
                        {
                            cell.setIcon(new GUI_Queen_Icon(player2, true));
                            cell.setBackground(light);
                        }
                    }
                    else if (board.owner_of_piece_at(j, i) == 2)
                    {
                        if (piece.get_type() == 1)
                        {
                            cell.setIcon(new GUI_Piece_Icon(player1, true));
                            cell.setBackground(light);
                        }
                        else if (piece.get_type() == 2)
                        {
                            cell.setIcon(new GUI_Queen_Icon(player1, true));
                            cell.setBackground(light);

                        }
                    }
                }
                else
                {
                    if ((i + j) % 2 == 0)
                        cell.setBackground(light);
                    else
                        cell.setBackground(dark);
                }
                if (j == selectedX && i == selectedY)
                    cell.setBackground(highlight);
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private class CellClickListener implements ActionListener
    {
        private int x, y;
        static boolean has_taken = false;

        public CellClickListener(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            if (selectedPiece == null && !has_taken)
            {
                if (!board.check_if_available(x, y) && board.owner_of_piece_at(x, y) == currentPlayer)
                {
                    selectedPiece = board.get_piece_at(x, y);
                    selectedX = x;
                    selectedY = y;
                    }
                    if ((board.owner_of_piece_at(x, y) != currentPlayer)
                    || (board.check_if_any_can_eat(currentPlayer) && (board.get_piece_at(x, y) != null) && !board.get_piece_at(x, y).can_eat(board)))
                    {
                        selectedPiece = null;
                        selectedX = -1;
                        selectedY = -1;
                    }
                    update_Board();
            }
            else
            {
                if (!board.check_if_available(x, y) && !has_taken)
                {
                    if (board.owner_of_piece_at(x, y) == currentPlayer)
                    {
                        selectedPiece = board.get_piece_at(x, y);
                        selectedX = x;
                        selectedY = y;
                        update_Board();
                    }
                    if ((board.owner_of_piece_at(x, y) != currentPlayer)
                    || (board.check_if_any_can_eat(currentPlayer) && (board.get_piece_at(x, y) != null) && !board.get_piece_at(x, y).can_eat(board)))
                    {
                        selectedPiece = null;
                        selectedX = -1;
                        selectedY = -1;
                    }
                }
                else
                {
                    if (selectedPiece != null || (board.check_if_any_can_eat(currentPlayer) && selectedPiece.can_eat(board)))
                    {
                        if (selectedPiece.is_move_possible(x, y, board, false) && !(selectedPiece.can_eat(board))
                        || (selectedPiece.is_move_possible(x, y, board, true) && selectedPiece.can_eat(board) && (selectedY != y + 1 && selectedY != y - 1) && !board.check_if_available((x + selectedX)/2, (y + selectedY)/2) && board.owner_of_piece_at((x + selectedX)/2, (y + selectedY)/2) != currentPlayer))
                        { 
                            board.move_piece(selectedX, selectedY, x, y);
                            if (selectedY == y + 2 || selectedY == y - 2)
                            {
                                if (x < selectedX && y < selectedY)
                                    capturedPieces.add(board.remove_piece(x + 1, y + 1));
                                else if (x > selectedX && y < selectedY)
                                    capturedPieces.add(board.remove_piece(x - 1 , y + 1));
                                else if (x < selectedX && y > selectedY)
                                    capturedPieces.add(board.remove_piece(x + 1, y - 1));
                                else if (x > selectedX && y > selectedY)
                                    capturedPieces.add(board.remove_piece(x - 1, y - 1));
                                has_taken = true;
                            }
                            selectedPiece = null;
                            selectedX = -1;
                            selectedY = -1;

                            if (currentPlayerMustEatAgain(x, y) && (has_taken == true))
                            {
                                selectedPiece = board.get_piece_at(x, y);
                                selectedX = x;
                                selectedY = y;
                            }
                            else
                            {
                                if (!board.check_if_available(x, y) && board.get_piece_at(x, y).get_type() == 1
                                && ((currentPlayer == 1 && y == 7) || (currentPlayer == 2 && y == 0)))
                                {
                                    board.replace_pawn_for_queen(x, y);
                                }
                                currentPlayer = currentPlayer == 1 ? 2 : 1;
                                updateCurrentPlayerLabel();
                                has_taken = false;
                            }
                        }
                    }
                }
                update_Board();
                if (board.count_player_pieces(1) == 0)
                    endGame(2);
                else if (board.count_player_pieces(2) == 0)
                    endGame(1);
            }
        }

        private boolean currentPlayerMustEatAgain(int x, int y)
        {
            Piece piece = board.get_piece_at(x, y);
            return (piece != null && piece.can_eat(board));
        }
    }

    private void updateCurrentPlayerLabel()
    {
        String playerText = currentPlayer == 1 ? "Player 1" : "Player 2";
        currentPlayerLabel.setText("Current Player: " + playerText);
    }

    private void endGame(int player)
    {
        int option;
        if (player == 1)
            option = JOptionPane.showConfirmDialog(this, "Player 1 Wins! Do you want to start a new game?", "Game Over", JOptionPane.YES_NO_OPTION);
        else if (player == 2)
            option = JOptionPane.showConfirmDialog(this, "Player 2 Wins! Do you want to start a new game?", "Game Over", JOptionPane.YES_NO_OPTION);
        else
            option = JOptionPane.showConfirmDialog(this, "Do you want to start a new game?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION)
        {
            board = new Board();
            currentPlayer = 1;
            updateCurrentPlayerLabel();
            update_Board();
            if(updateXML(capturedPieces) == 0)
                JOptionPane.showMessageDialog(this, "Game state saved to pieces_taken.xml");
            else
                JOptionPane.showMessageDialog(this, "Error saving pieces taken to file");
            capturedPieces.clear();
        }
        else
        {
            if(updateXML(capturedPieces) == 0)
                JOptionPane.showMessageDialog(this, "Game state saved to pieces_taken.xml");
            else
                JOptionPane.showMessageDialog(this, "Error saving pieces taken to file");
            capturedPieces.clear();
            dispose();
        }
    }
    

    public static int updateXML(ArrayList<Piece> capturedPieces)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			Element root = doc.createElement("CapturedPieces");
			doc.appendChild(root);

			Element player1 = doc.createElement("Player1");
			Element player2 = doc.createElement("Player2");
			root.appendChild(player1);
			root.appendChild(player2);

			for (Piece piece : capturedPieces)
			{
				if (piece != null)
				{
					Element pieceElement = null;
					if (piece.get_type() == 1)
						pieceElement = doc.createElement("Pawn");
					else if (piece.get_type() == 2)
						pieceElement = doc.createElement("Queen");
					

					if (pieceElement != null)
					{
						pieceElement.setAttribute("coords", "" + (char)(piece.get_PosX() + 'A')+Integer.toString(piece.get_PosY() + 1));

						if (piece.get_player() == 1)
							player2.appendChild(pieceElement);
						else if (piece.get_player() == 2)
							player1.appendChild(pieceElement);
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);

			StreamResult file = new StreamResult(new File("pieces_taken.xml"));
			transformer.transform(source, file);
            return (0);

    	}
		catch (Exception e)
		{
			return (-1);
		}
	}

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(GUI::new);
    }
}
