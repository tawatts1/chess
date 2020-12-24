from Board import Board
from pyjava_ai import java_ai
from functools import partial
import tkinter as tk
from Virtual_board import VBoard
from move_calc import in_stalemate, in_checkmate
from time import sleep, time


EMPTY = ['0','0']

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
    value = {'p':1,'b':3,'n':3,'r':5,'q':9,'k':2}
    ps = pre_score(board)
    if ps is not None:
        score = ps
    else:
        for i in range(8):
            for j in range(8):
                occ = board[i][j]
                if occ != EMPTY:
                    if occ[0] == 'w':
                        white_points += value[occ[1]]
                    total_points += value[occ[1]]
        score = white_points/total_points
    return score


def run_battle(N1=2,N2=2, white = 'pawns', black='kill', T = 60):
    fname = f'game_results/{N1}{white}_V_{N2}{black}'
    #white ai:
    ai1 = partial(java_ai, **{'N':N1, 'scoring': white})

    # black ai:
    ai2 = partial(java_ai, **{'N':N2, 'scoring': black})
    ais = {'w':ai1, 'b':ai2}
    
    turn = 'w'
    change = {'w':'b','b':'w'}
    #print(board)
    max_T = time() + 60*60*T # number of extra seconds to run
    while time() < max_T:
        board = VBoard()
        n_moves = 0
        for i in range(150):
            n_moves = i
            if in_checkmate(board, turn) or in_stalemate(board):
                break
            board = board.execute_move(*ais[turn](board, turn))
            turn = change[turn]
            #print(board)
        print(board)
        score = board_score(board)
        print(score)
        print()
        with open(fname, 'a') as file:
            file.write(f'{score:.3f}, {n_moves}\n')    
if __name__ == "__main__":
    import sys

    n1 = sys.argv[1]
    w  = sys.argv[2]
    
    n2 = sys.argv[3]
    b  = sys.argv[4]
    
    t  = sys.argv[5]

    
    run_battle(N1=n1, N2 = n2, white=w, black=b, T=float(t))
        




