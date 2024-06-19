public class Queen extends Piece
{
	public Queen (int PosX, int PosY, int player)
	{
		super(PosX, PosY, player);
		this.type = 2;
	}

	public boolean is_move_possible(int moveX, int moveY, Board board, boolean has_jumped)
	{
		return(is_move_possible_up(moveX, moveY, board, has_jumped) || is_move_possible_down(moveX, moveY, board, has_jumped));
	}

	@Override
	public boolean can_eat(Board board)
	{
		return(can_eat_up(board) || can_eat_down(board));
	}

}
