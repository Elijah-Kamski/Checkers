import java.util.ArrayList;

public class Board
{
	private ArrayList<Queen> queens;
	private ArrayList<Pawn> pawns;

	public Board()
	{
		ArrayList<Pawn> pawns = new ArrayList<>();
		ArrayList<Queen> queens = new ArrayList<>();
	
		int test_mode = 0;

		if (test_mode == 1)
		{
			Pawn pawn_A_C3 = new Pawn(2, 2, 1);
			pawns.add(pawn_A_C3);

			Pawn pawn_B_F6 = new Pawn(5, 5, 2);
			Queen queen_B_D4 = new Queen(3, 3, 2);
			Pawn pawn_B_H8 = new Pawn(7, 7, 2);
			pawns.add(pawn_B_F6);
			queens.add(queen_B_D4);
			pawns.add(pawn_B_H8);
		}
		else if (test_mode == 2)
		{
			Pawn pawn_A_1 = new Pawn(1, 1, 1);
			Pawn pawn_A_2 = new Pawn(1, 5, 1);
			pawns.add(pawn_A_1);
			pawns.add(pawn_A_2);

			Pawn pawn_B_1 = new Pawn(4, 4, 2);
			Queen queen_B_1 = new Queen(2, 2, 2);
			Pawn pawn_B_2 = new Pawn(6, 6, 2);
			Pawn pawn_B_3 = new Pawn(2, 6, 2);
			pawns.add(pawn_B_1);
			queens.add(queen_B_1);
			pawns.add(pawn_B_2);
			pawns.add(pawn_B_3);
		}
		else
		{
			Pawn pawn1_1 = new Pawn(0, 0, 1);
			Pawn pawn1_2 = new Pawn(0, 2, 1);
			Pawn pawn1_3 = new Pawn(1, 1, 1);
			Pawn pawn1_4 = new Pawn(2, 0, 1);
			Pawn pawn1_5 = new Pawn(2, 2, 1);
			Pawn pawn1_6 = new Pawn(3, 1, 1);
			Pawn pawn1_7 = new Pawn(4, 0, 1);
			Pawn pawn1_8 = new Pawn(4, 2, 1);
			Pawn pawn1_9 = new Pawn(5, 1, 1);
			Pawn pawn1_10 = new Pawn(6, 0, 1);
			Pawn pawn1_11 = new Pawn(6, 2, 1);
			Pawn pawn1_12 = new Pawn(7, 1, 1);
			pawns.add(pawn1_1);
			pawns.add(pawn1_2);
			pawns.add(pawn1_3);
			pawns.add(pawn1_4);
			pawns.add(pawn1_5);
			pawns.add(pawn1_6);
			pawns.add(pawn1_7);
			pawns.add(pawn1_8);
			pawns.add(pawn1_9);
			pawns.add(pawn1_10);
			pawns.add(pawn1_11);
			pawns.add(pawn1_12);

			Pawn pawn2_1 = new Pawn(0, 6, 2);
			Pawn pawn2_2 = new Pawn(1, 5, 2);
			Pawn pawn2_3 = new Pawn(1, 7, 2);
			Pawn pawn2_4 = new Pawn(2, 6, 2);
			Pawn pawn2_5 = new Pawn(3, 5, 2);
			Pawn pawn2_6 = new Pawn(3, 7, 2);
			Pawn pawn2_7 = new Pawn(4, 6, 2);
			Pawn pawn2_8 = new Pawn(5, 5, 2);
			Pawn pawn2_9 = new Pawn(5, 7, 2);
			Pawn pawn2_10 = new Pawn(6, 6, 2);
			Pawn pawn2_11 = new Pawn(7, 5, 2);
			Pawn pawn2_12 = new Pawn(7, 7, 2);
			pawns.add(pawn2_1);
			pawns.add(pawn2_2);
			pawns.add(pawn2_3);
			pawns.add(pawn2_4);
			pawns.add(pawn2_5);
			pawns.add(pawn2_6);
			pawns.add(pawn2_7);
			pawns.add(pawn2_8);
			pawns.add(pawn2_9);
			pawns.add(pawn2_10);
			pawns.add(pawn2_11);
			pawns.add(pawn2_12);
		}
		this.queens = queens;
		this.pawns = pawns;
	}

	public boolean check_if_available(int x, int y)
	{
		if (x < 0 || x > 7 || y < 0 || y > 7)
			return (false);

		for (Queen queen : queens)
		{
			if (queen.get_PosX() == x && queen.get_PosY() == y)
				return (false);
		}

		for (Pawn pawn : pawns)
		{
			if (pawn.get_PosX() == x && pawn.get_PosY() == y)
				return (false);
		}
		return (true);
	}

	public int owner_of_piece_at(int x, int y)
	{
		for (Queen queen : queens)
		{
			if (queen.get_PosX() == x && queen.get_PosY() == y)
				return (queen.get_player());
		}
		for (Pawn pawn : pawns)
		{
			if (pawn.get_PosX() == x && pawn.get_PosY() == y)
				return (pawn.get_player());
		}
		return (0);
	}

	public Piece remove_piece(int x, int y) {
		for (int i = 0; i < queens.size(); i++) {
			if (queens.get(i).get_PosX() == x && queens.get(i).get_PosY() == y) {
				Piece removedPiece = queens.get(i);
				queens.remove(i);
				return removedPiece;
			}
		}
		for (int i = 0; i < pawns.size(); i++) {
			if (pawns.get(i).get_PosX() == x && pawns.get(i).get_PosY() == y) {
				Piece removedPiece = pawns.get(i);
				pawns.remove(i);
				return removedPiece;
			}
		}
		return null;
	}
	
	public boolean move_piece(int startX, int startY, int endX, int endY)
	{
		for (Pawn pawn : pawns)
		{
			if (pawn.get_PosX() == startX && pawn.get_PosY() == startY)
			{
				if (check_if_available(endX, endY))
				{
					pawn.PosX = endX;
					pawn.PosY = endY;
					return (true);
				}
				return (false);
			}
		}
		for (Queen queen : queens)
		{
			if (queen.get_PosX() == startX && queen.get_PosY() == startY)
			{
				if (check_if_available(endX, endY))
				{
					queen.PosX = endX;
					queen.PosY = endY;
					return (true);
				}
				return (false);
			}
		}
		return (false);
	}

	public Piece get_piece_at(int x, int y)
	{
		for (Pawn pawn : pawns)
		{
			if (pawn.get_PosX() == x && pawn.get_PosY() == y)
				return (pawn);

		}
		for (Queen queen : queens)
		{
			if (queen.get_PosX() == x && queen.get_PosY() == y)
				return (queen);
		}
		return (null);
	}
	
	
	public boolean check_if_any_can_eat(int player)
	{
		for (Pawn pawn : pawns)
		{
			if (owner_of_piece_at(pawn.get_PosX(), pawn.get_PosY()) == player && pawn.can_eat(this))
				return (true);
		}
		for (Queen queen : queens)
		{
			if (owner_of_piece_at(queen.get_PosX(), queen.get_PosY()) == player && queen.can_eat(this))
				return (true);
		}
		return (false);
	}

	public int count_player_pieces(int player)
	{
		int count = 0;
		for (Pawn pawn : pawns)
		{
			if (pawn.get_player() == player)
				count++;
		}
		for (Queen queen : queens) 
		{
			if (queen.get_player() == player)
				count++;
		}
		return (count);
	}

	public void replace_pawn_for_queen(int x, int y)
	{
		int player = owner_of_piece_at(x, y);
		remove_piece(x, y);
		Queen queen = new Queen(x, y, player);
		queens.add(queen);
		return;  
	}

}