public abstract class Piece
{
	int PosX;
	int PosY;
	int player;
	int type;

	public Piece (int PosX, int PosY, int player)
	{
		this.PosX = PosX;
		this.PosY = PosY;
		this.player = player;
	}

	public abstract boolean can_eat(Board board);
	public abstract boolean is_move_possible(int moveX, int moveY, Board board, boolean has_jumped);

	public int get_type()
	{
		return (type);
	}

	public int get_PosX()
	{
		return (PosX);
	}

	public int get_PosY()
	{
		return (PosY);
	}

	public int get_player()
	{
		return (player);
	}

	public boolean is_move_possible_up(int moveX, int moveY, Board board, boolean has_jumped)
	{
		if((PosX == moveX)
		|| (PosY >= moveY)
		|| (!board.check_if_available(moveX, moveY))
		|| (moveY > PosY + 2)
		|| (moveX < 0 || moveX > 7 || moveY < 0 || moveY > 7)
		|| (moveX + moveY) % 2 != 0)
			return (false);
		if (!has_jumped)
		{
			if(board.check_if_available(PosX + 1, PosY + 1) && board.owner_of_piece_at(PosX + 1, PosY + 1) != player && (moveX == PosX + 1 && moveY == PosY + 1))
				return (true);
			else if (board.check_if_available(PosX - 1, PosY + 1) && board.owner_of_piece_at(PosX - 1, PosY + 1) != player && (moveX == PosX - 1 && moveY == PosY + 1))
				return (true);
			else
				has_jumped = true;
		}
		if (has_jumped)
		{
			if (!board.check_if_available(PosX - 1, PosY + 1) && board.owner_of_piece_at(PosX - 1, PosY + 1) != player && board.check_if_available(PosX - 2, PosY + 2))
				return (true);
			else if (!board.check_if_available(PosX + 1, PosY + 1) && board.owner_of_piece_at(PosX + 1, PosY + 1) != player && board.check_if_available(PosX + 2, PosY + 2))
				return (true);
			else
				return (false);
		}
		return (false);
	}

	public boolean is_move_possible_down(int moveX, int moveY, Board board, boolean has_jumped)
	{
		if((PosX == moveX)
		|| (PosY <= moveY)
		|| (!board.check_if_available(moveX, moveY))
		|| (moveY < PosY - 2)
		|| (moveX < 0 || moveX > 7 || moveY < 0 || moveY > 7)
		|| (moveX + moveY) % 2 != 0)
			return (false);
		if (!has_jumped)
		{
			if(board.check_if_available(PosX + 1, PosY - 1) && board.owner_of_piece_at(PosX + 1, PosY - 1) != player && (moveX == PosX + 1 && moveY == PosY - 1))
				return (true);
			else if (board.check_if_available(PosX - 1, PosY - 1) && board.owner_of_piece_at(PosX - 1, PosY - 1) != player && (moveX == PosX - 1 && moveY == PosY - 1))
				return (true);
			else
				has_jumped = true;
		}
		if (has_jumped)
		{
			if (!board.check_if_available(PosX - 1, PosY - 1) && (board.owner_of_piece_at(PosX - 1, PosY - 1) != player) && board.check_if_available(PosX - 2, PosY - 2))
				return (true);
			else if (!board.check_if_available(PosX + 1, PosY - 1) && (board.owner_of_piece_at(PosX + 1, PosY - 1) != player) && board.check_if_available(PosX + 2, PosY - 2))
				return (true);
			else
				return (false);
		}
		return (false);
	}

	public boolean can_eat_up(Board board)
	{
		if (!board.check_if_available(PosX - 1, PosY + 1) && 
			board.owner_of_piece_at(PosX - 1, PosY + 1) != player && 
			board.check_if_available(PosX - 2, PosY + 2))
			return (true);
		if (!board.check_if_available(PosX + 1, PosY + 1) && 
			board.owner_of_piece_at(PosX + 1, PosY + 1) != player && 
			board.check_if_available(PosX + 2, PosY + 2))
			return (true);
		return (false);
	}
	
	public boolean can_eat_down(Board board)
	{
		if (!board.check_if_available(PosX - 1, PosY - 1) && 
			board.owner_of_piece_at(PosX - 1, PosY - 1) != player && 
			board.check_if_available(PosX - 2, PosY - 2))
			return (true);
		if (!board.check_if_available(PosX + 1, PosY - 1) && 
			board.owner_of_piece_at(PosX + 1, PosY - 1) != player && 
			board.check_if_available(PosX + 2, PosY - 2)) 
			return (true);
		return (false);
	}
}
