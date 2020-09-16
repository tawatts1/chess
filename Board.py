#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""
from Square import Square
from Piece import Piece, in_board_space
import tkinter as tk
from tkinter import ttk
#import tkMessageBox
from time import sleep
import numpy as np

def piece_to_fname(piece):
    if len(piece) == 2:
        if piece[0] in 'bw':
            if piece[1] in 'kqbnrp':
                return 'images/' + piece + '.png'
    return None




def index_to_piece(index, piece_f = piece_to_fname):
    if index[0] is 1:
        out = 'bp'
    elif index[0] is 6:
        out = 'wp'
    elif index[0] is 0:
        out0 = {0:'r', 1:'n', 2:'b', 3:'q', 4:'k', 5:'b', 6:'n', 7:'r'}
        out = 'b' + out0[index[1]]
    elif index[0] is 7:
        out0 = {0:'r', 1:'n', 2:'b', 3:'q', 4:'k', 5:'b', 6:'n', 7:'r'}
        out = 'w' + out0[index[1]]
    else:
        return None
    
    return piece_f(out)

def f1():
    print(100)

class Board(ttk.Frame):
    def __init__(self, parent, piece_func = piece_to_fname):
        super().__init__(parent)
        
        self.parent = parent
        self.squares = []
        
        self.board = self.setup_new_game()
        self.grid(row=0, column=0)
        
    def setup_new_game(self):
        board0 = []
        for i in range(8):
            row = []
            for j in range(8):
                if (i%2+j%2)%2==0:
                    bg_color = 'grey'
                else:
                    bg_color = 'brown'
                fname = index_to_piece([i,j])
                sq = Square(self, 
                            img = fname,#piece_to_fname('bk'),#
                            color = bg_color,
                            index = np.array([i,j]), 
                            callback = f1)
                self.squares.append(sq)
                sq.grid(row=i, column=j)
                row.append(sq)
            board0.append(row)
        return board0
        


if __name__ == '__main__':
   
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root)
    root.mainloop()
