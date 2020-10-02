#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep 30 22:40:28 2020

@author: ted
"""
import tkinter as tk
from random import choice
from Board import Board
from numpy import inf

def board_score(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    sign = {color:1, enemy_color:-1}
    value = {'p':1,'b':3,'n':3,'r':5,'q':9,'k':0}
    score = 0
    for sq in board.sq_dict.values():
        occ = sq.occupant
        if occ is not None:
            score += sign[occ[0]]*value[occ[1]]
    #print(score)
    return score
def two_step(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    #one_step_boards = board.get_next_boards(color = color)
    lower_limit = -inf
    best_move = None
    for c1, c2, board1 in board.get_next_boards(color):
        wcs = inf # if the worst is worse than the lower limit, don't go
        scores_list = []
        for c3, c4,board2 in board1.get_next_boards(enemy_color):
            score_2 = board_score(board2, color)
            scores_list.append(score_2)
            if score_2 < wcs:
                wcs = score_2
            if wcs < lower_limit:
                break
        if wcs > lower_limit:
            best_move = (c1,c2)
            lower_limit = wcs
    return best_move
def four_step(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    lower_limit = -inf
    best_move = None

def get_occ_color(board, coords):
    occ = board.sq_dict[tuple(coords)].occupant
    if occ is None:
        return None
    else:
        return occ[0]
def agressive_ai(board, color):
    out = []
    mvs = board.get_next_boards(color = color)
    for c1, c2, new_board in mvs:
        if get_occ_color(board, c2) == {'b':'w','w':'b'}[color]:
            out.append([c1,c2,new_board])
    if len(out) == 0:
        out = mvs
    c3, c4, new_board1 = choice(out)    
    return c3, c4

if __name__ == '__main__':
    
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root, ai = two_step)
    root.mainloop()


