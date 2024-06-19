public class Pawn extends Piece
{
	public Pawn (int PosX, int PosY, int player)
	{
		super(PosX, PosY, player);
		this.type = 1;
	}

	@Override
	public boolean can_eat(Board board)
	{
		if (player == 1)
			return(can_eat_up(board));
		else if (player == 2)
			return(can_eat_down(board));
		return (false);
	}

	@Override
	public boolean is_move_possible(int moveX, int moveY, Board board, boolean has_jumped)
	{
		if (player == 1)
			return(is_move_possible_up(moveX, moveY, board, has_jumped));
		else if (player == 2)
			return(is_move_possible_down(moveX, moveY, board, has_jumped));
		return (false);
	}
		
}