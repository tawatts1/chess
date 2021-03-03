from Board import Board
from pyjava_ai import java_ai, java_board_string
from functools import partial
import tkinter as tk
from Virtual_board import VBoard
from move_calc import in_stalemate, in_checkmate
from time import sleep, time
from random import choice
from numpy.random import uniform


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


def run_battle(T = 1):
    fname = 'midgame_boards'
    turn = 'w'
    change = {'w':'b','b':'w'}
    #print(board)
    max_T = time() + 60*60*T # number of extra seconds to run
    depths =  [ 1,2,3,
                4,4,4,4,4,4,4,4,4,4,4,4,4,4,
                5,5,5,5,5,5,5,5,5,5,5,5,5,6]
    strategies = ['pos','pos','pos','pos','val']
    while time() < max_T:
        board = VBoard()
        for i in range(150):
            if in_checkmate(board, turn) or in_stalemate(board):
                break

            if uniform()<.1 and turn == 'b':
                print(board)
                with open(fname, 'a') as file:
                    file.write(java_board_string(board)+'\n')    
            ai = partial(java_ai, **{'N':choice(depths), 'scoring': choice(strategies)})
            board = board.execute_move(*ai(board, turn))
            turn = change[turn]
        print(board)
        score = board_score(board)
        print(score)
        print()
        
if __name__ == "__main__":
    
    
    run_battle(T = 8)
        




