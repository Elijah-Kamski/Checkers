import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UI_console
{
	public static void updateXML(ArrayList<Piece> capturedPieces)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			Element root = doc.createElement("CapturedPieces");
			doc.appendChild(root);

			Element playerA = doc.createElement("PlayerA");
			Element playerB = doc.createElement("PlayerB");
			root.appendChild(playerA);
			root.appendChild(playerB);

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
							playerB.appendChild(pieceElement);
						else if (piece.get_player() == 2)
							playerA.appendChild(pieceElement);
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);

			StreamResult file = new StreamResult(new File("NOM_NOM_log.xml"));
			transformer.transform(source, file);

    	}
		catch (Exception e)
		{
			System.out.println("\nError creating game log.\n");
		}
	}

	public static void print_board(Board board)
	{
		System.out.print("   ╔══════════════════════════╗\n");
		for (int i = 7; i >= 0; i--)
		{
			System.out.print((i + 1) + "  ║ ");
			for (int j = 0; j < 8; j++)
			{
				if (!board.check_if_available(j, i))
				{
					System.out.print(" ");
					if (!board.check_if_available(j, i) && board.owner_of_piece_at(j, i) == 1 && board.get_piece_at(j, i).get_type() == 1)
						System.out.print('a');
					else if (!board.check_if_available(j, i) && board.owner_of_piece_at(j, i) == 2 && board.get_piece_at(j, i).get_type() == 1)
						System.out.print('b');
					else if (!board.check_if_available(j, i) && board.owner_of_piece_at(j, i) == 1 && board.get_piece_at(j, i).get_type() == 2)
						System.out.print('A');
					else
						System.out.print('B');
					System.out.print(" ");
				}
				else
				{
					if ((i + j)%2 == 0)
					{
						System.out.print("░");
						System.out.print("░░");
					}
					else
					{
						System.out.print("▓");
						System.out.print("▓▓");
					}
				}
			}
			System.out.print(" ║  " +(i + 1));
			System.out.print("\n");
		}
		System.out.print("   ╚══════════════════════════╝");
		System.out.print("\n      A  B  C  D  E  F  G  H\n");

		System.out.print("\n");
		return;
	}

	public static int get_int(String input)
	{
		if (input.equals("1") || input.toUpperCase().equals("A"))
			return (0);
		else if (input.equals("2") || input.toUpperCase().equals("B"))
			return (1);
		else if (input.equals("3") || input.toUpperCase().equals("C"))
			return (2);
		else if (input.equals("4") || input.toUpperCase().equals("D"))
			return (3);
		else if (input.equals("5") || input.toUpperCase().equals("E"))
			return (4);
		else if (input.equals("6") || input.toUpperCase().equals("F"))
			return (5);
		else if (input.equals("7") || input.toUpperCase().equals("G"))
			return (6);
		else if (input.equals("8") || input.toUpperCase().equals("H"))
			return (7);
		return (-1);    
	}

	public static void check_end_msg(String input, int player, ArrayList<Piece> piecesTaken, Scanner reader, Board board)
	{
		if (!input.equals("end"))
			return ;
		else
		{
			System.out.println("\n-------------------------------------------\n");
			if (player == 1)
				System.out.println("\n    PLAYER 'A' HAS TERMINATED THE GAME!\n");
			else
				System.out.println("\n    PLAYER 'B' HAS TERMINATED THE GAME!\n");
			System.out.println("\n-------------------------------------------");
			updateXML(piecesTaken);
			reader.close();
			System.exit(0);
		}
	}

	public static boolean keep_eating (Board board, int piece_x, int piece_y, int player, boolean game_over, Scanner reader, ArrayList<Piece> piecesTaken)
	{
		int int_value_x = -1, int_value_y = -1;
		String input;
		String letterPart, numberPart;
		System.out.println("\n-----------------------------------\n");
		System.out.printf("         Playing: Player %c\n\n", player +'A' - 1);    
		print_board(board);
		System.out.printf("You are forced to eat! Move again!\n", player);
		System.out.printf("Moving piece %c%d\n", piece_x + 'A', piece_y + 1);
			
		while (board.get_piece_at(piece_x, piece_y).can_eat(board) && !game_over)
		{
			int_value_x = -1;
			int_value_y = -1;
			
			while (int_value_x == -1 || int_value_y == -1)
			{
				System.out.println("\n-----------------------------------\n");
				System.out.printf("         Playing: Player %c\n\n", player +'A' - 1);    
						print_board(board);
				System.out.printf("(Player %c) Choose the coordenates for your piece %c%d to move to: ", player + 'A' - 1, piece_x + 'A', piece_y + 1);
				input = reader.nextLine();
				check_end_msg(input, player, piecesTaken, reader, board);

				if (input.length() == 2)
				{
					letterPart = input.substring(0, 1).toUpperCase();
					numberPart = input.substring(1, 2);
					int_value_x = get_int(letterPart);
					int_value_y = get_int(numberPart);
				}
				if (!board.get_piece_at(piece_x, piece_y).is_move_possible(int_value_x, int_value_y, board, game_over) || (int_value_y == piece_y + 1 || int_value_y == piece_y - 1))
						int_value_x = -1;
				if ((int_value_x == -1 || int_value_y == -1))
				{
					System.out.println("\n-----------------------------------\n");
					System.out.printf("         Playing: Player %c\n\n", player +'A' - 1);    
					print_board(board);
					System.out.println("Error. Move not valid. Please try again with a valid move.");
					System.out.printf("You are forced to eat, do not forget that!\n", player);
					System.out.printf("Moving piece %c%d\n", piece_x + 'A', piece_y + 1);
				}
				else
					System.out.println("\n-----------------------------------");
			}

			if (board.get_piece_at(piece_x, piece_y) != null && board.get_piece_at(piece_x, piece_y).is_move_possible(int_value_x, int_value_y, board, true))
			{
				System.out.println("\n\n             NOM NOM. \n");
				
				board.move_piece(piece_x, piece_y, int_value_x, int_value_y);
				if (int_value_x < piece_x && int_value_y < piece_y)
					piecesTaken.add(board.remove_piece(int_value_x + 1, int_value_y + 1));
				else if (int_value_x > piece_x && int_value_y < piece_y)
					piecesTaken.add(board.remove_piece(int_value_x - 1 , int_value_y + 1));
				else if (int_value_x < piece_x && int_value_y > piece_y)
					piecesTaken.add(board.remove_piece(int_value_x + 1, int_value_y - 1));
				else if (int_value_x > piece_x && int_value_y > piece_y)
					piecesTaken.add(board.remove_piece(int_value_x - 1, int_value_y - 1));
				
				piece_x = int_value_x;
				piece_y = int_value_y;
				
				if (board.count_player_pieces(1) == 0 || board.count_player_pieces(2) == 0)
				game_over = true;
				if (!game_over &&
				((player == 1 && piece_y == 7)
				|| (player == 2 && piece_y == 0)))
				{
					if (!board.check_if_available(piece_x, piece_y) && board.get_piece_at(piece_x, piece_y).get_type()==1)
					{
						board.replace_pawn_for_queen(piece_x, piece_y);
						System.out.printf("Player %c's piece Upgraded!!\n\n\n", player + 'A' - 1);
						break;
					}
				}
			}
		}
		return (game_over);
	}
	
	public static void main(String[] args)
	{ 
		Scanner reader = new Scanner(System.in);
		Board board = new Board();

		ArrayList<Piece> piecesTaken = new ArrayList<>();
		
		int player = 2; // starting player: 1 = Player B, 2 = Player A
		int int_value_x = -1, int_value_y = -1;
		int piece_x = 0, piece_y = 0;
		int move_x = 0, move_y = 0;
		boolean valid = false;
		boolean has_jumped = false;
		boolean has_played = true;
		String input;
		String numberPart, letterPart;
		boolean game_over = false;
		
		System.out.print("\n      WELCOME TO CHECKERZZZZ!\n\n");

		try
		{
			while (!game_over)
			{
				if (player == 1 && has_played)
				{
					player = 2;
					has_played = false;
				}
				else if (player == 2 && has_played)
				{
					player = 1;
					has_played = false;
				}

				if(board.check_if_any_can_eat(player))
				{
					

					int_value_x = -1;
					int_value_y = -1;
					while ((int_value_x == -1 || int_value_y == -1))
					{
						System.out.printf("         Playing: Player %c\n\n", player +'A' - 1);    
						print_board(board);
						System.out.println("Help: Examples of coordenates: 'A3' 'G6' 'B2'\nTo terminate the game at any point type 'end'");
						System.out.printf("(Player %c) You are forced to eat, choose a piece that can eat: ", player + 'A' - 1);
						input = reader.nextLine();
						check_end_msg(input, player, piecesTaken, reader, board);
						if (input.length() == 2)
						{
							letterPart = input.substring(0, 1).toUpperCase();
							numberPart = input.substring(1, 2);
							int_value_x = get_int(letterPart);
							int_value_y = get_int(numberPart);
						}
						if (!board.check_if_available(int_value_x, int_value_y) && board.owner_of_piece_at(int_value_x, int_value_y) == player && !(board.get_piece_at(int_value_x, int_value_y).can_eat(board))
							|| board.check_if_available(int_value_x, int_value_y) || (!board.check_if_available(int_value_x, int_value_y) && board.owner_of_piece_at(int_value_x, int_value_y) != player))
							int_value_x = -1;
						if (int_value_x == -1 || int_value_y == -1)
						{
							System.out.println("Error. Invalid coordinates or piece. Please try again.");
							System.out.println("\n-----------------------------------\n");
						}
					}
					game_over = keep_eating(board, int_value_x, int_value_y, player, game_over, reader, piecesTaken);
					has_played = true;
					if(!game_over)
						System.out.println("\n-----------------------------------\n");
				}
				else
				{
					has_jumped = false;
					System.out.printf("         Playing: Player %c\n\n", player +'A' - 1);    
					print_board(board);
					System.out.println("Help: Examples of coordenates: 'A3' 'G6' 'B2'\nTo terminate the game at any point type 'end'");
					has_jumped = false;
					valid = false;
					while (!valid)
					{
						int_value_x = -1;
						int_value_y = -1;
						while (int_value_x == -1 || int_value_y == -1)
						{
							System.out.printf("(Player %c) Choose the coordinates of your piece: ", player + 'A' - 1);
							input = reader.nextLine();
							check_end_msg(input, player, piecesTaken, reader, board);
			
							if (input.length() == 2)
							{
								letterPart = input.substring(0, 1).toUpperCase();
								numberPart = input.substring(1, 2);
								int_value_x = get_int(letterPart);
								int_value_y = get_int(numberPart);
							}
							if (int_value_x == -1 || int_value_y == -1)
								System.out.println("Error. Invalid value. Please try again.");
						}
						piece_x = int_value_x;
						piece_y = int_value_y;
						if(board.owner_of_piece_at(piece_x, piece_y) == player)
								valid = true;
						else
							System.out.printf("No piece at position %c%d owned by player %c.\n\n", piece_x + 'A', piece_y + 1, player + 'A' - 1);
					}
					System.out.printf("(Player %c) Moving piece %c%d\n", player + 'A' - 1 , piece_x + 'A', piece_y + 1);
					int_value_x = -1;
					int_value_y = -1;
					while (int_value_x == -1 || int_value_y == -1)
					{
						System.out.printf("(Player %c) Choose the coordenates for your piece to move to: ", player + 'A' - 1);
						input = reader.nextLine();
						check_end_msg(input, player, piecesTaken, reader, board);

						if (input.length() == 2)
						{
							letterPart = input.substring(0, 1).toUpperCase();
							numberPart = input.substring(1, 2);
							int_value_x = get_int(letterPart);
							int_value_y = get_int(numberPart);
						}
						if (int_value_x == -1 || int_value_y == -1)
							System.out.println("Error. Invalid coordinates. Please try again with a valid move.");
						else
							System.out.println("\n-----------------------------------");

					}
					move_x = int_value_x;
					move_y = int_value_y;
					if (board.check_if_any_can_eat(player) == true && (move_x == piece_x + 1|| move_x == piece_x - 1))
						System.out.println("\nInvalid coordinates!! Don't forget you are forced to eat!");
					else if (board.get_piece_at(piece_x, piece_y) != null && board.get_piece_at(piece_x, piece_y).is_move_possible(move_x, move_y, board, has_jumped))
					{
						board.move_piece(piece_x, piece_y, move_x, move_y);
						valid = true;
						if (move_x < piece_x && move_y < piece_y)
							piecesTaken.add(board.remove_piece(move_x + 1, move_y + 1));
						else if (move_x > piece_x && move_y < piece_y)
							piecesTaken.add(board.remove_piece(move_x - 1 , move_y + 1));
						else if (move_x < piece_x && move_y > piece_y)
							piecesTaken.add(board.remove_piece(move_x + 1, move_y - 1));
						else if (move_x > piece_x && move_y > piece_y)
							piecesTaken.add(board.remove_piece(move_x - 1, move_y - 1));

						if(move_x > piece_x + 1 || move_x < piece_x - 1)
							has_jumped = true;
						has_played = true;
					}
					else
						System.out.println("\nError. Invalid coordinates. Please try again with a valid move.");
					System.out.print("\n"); 
					if (board.count_player_pieces(1) == 0 || board.count_player_pieces(2) == 0)
						game_over = true;

					if (!game_over
					&& ((player == 1 && has_played == true && move_y == 7)
					|| (player == 2 && has_played == true && move_y == 0)))
					{
						if (!board.check_if_available(move_x, move_y) && board.get_piece_at(move_x, move_y).get_type()==1)
						{
							board.replace_pawn_for_queen(move_x, move_y);
							has_jumped = false;
							System.out.printf("Player %c's piece Upgraded!!\n\n\n", player + 'A' - 1);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("\n\n-----------------------\n");
			System.out.println("\n    GAME TERMINATED\n");
			System.out.println("\n-----------------------");
			reader.close();
			updateXML(piecesTaken);
			System.exit(-1);
		}
		if (board.count_player_pieces(2) == 0)
			System.out.println("\n    GAME OVER! PLAYER 'A' WINS!\n\n");
		else
			System.out.println("\n    GAME OVER! PLAYER 'B' WINS!\n\n");
		System.out.println("-----------------------------------");
		reader.close();
		updateXML(piecesTaken);
	}

}
