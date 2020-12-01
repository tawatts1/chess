from Board import Board
from pyjava_ai import java_ai
from functools import partial
import tkinter as tk
def run_visual():
    #white ai:
    ai1 = partial(java_ai, **{'N':4, 'post_strategy': 'kill,pawns'})

    # black ai:
    ai2 = partial(java_ai, **{'N':4, 'post_strategy': 'pawns'})

    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root, ai = ai1, ai2 = ai2)
    root.mainloop()
def pre_score(board):
    score = None
    if in_checkmate(board, 'b'):
        score = 1
    elif in_checkmate(board, 'w'):
        score = 0
    elif in_stalemate(board):
        score = .5
    return score
def board_score(board):
    white_points = 0
    total_points = 0
    value = {'p':1,'b':3,'n':3,'r':5,'q':9,'k':0}
    ps = pre_score(board)
    if ps is not None:
        score = ps
    else:
        for i in range(8):
            for j in range(8):
                occ = board.sq_dict[(i,j)]
                if occ is not None:
                    if occ[0] == 'w':
                        white_points += value[occ[1]]
                    total_points += value[occ[1]]
        score = white_points/total_points
    return score
if __name__ == "__main__":
    from Virtual_board import gen_standard_board, VBoard
    from moves0 import in_stalemate, in_checkmate
    from time import sleep, time
    def run_battle(N=2, white = 'pawns', black='kill'):
        fname = f'game_results/{N}{white}_V_{N}{black}'
        #white ai:
        ai1 = partial(java_ai, **{'N':N, 'post_strategy': white})

        # black ai:
        ai2 = partial(java_ai, **{'N':N, 'post_strategy': black})
        ais = {'w':ai1, 'b':ai2}
        
        turn = 'w'
        change = {'w':'b','b':'w'}
        #print(board)
        max_T = time() + 60*60*1 # number of extra seconds to run
        while time() < max_T:
            board = gen_standard_board()
            for i in range(150):
                if in_checkmate(board, turn) or in_stalemate(board):
                    break
                board.execute_move(*ais[turn](board, turn))
                turn = change[turn]
                print(board)
            score = board_score(board)
            with open(fname, 'a') as file:
                file.write(f'{score:.3f}\n')
    run_battle()
        




