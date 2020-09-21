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
from functools import partial 

def index_to_piece(index):
    if index[0] == 1:
        out = 'bp'
    elif index[0] == 6:
        out = 'wp'
    elif index[0] == 0:
        out0 = {0:'r', 1:'n', 2:'b', 3:'q', 4:'k', 5:'b', 6:'n', 7:'r'}
        out = 'b' + out0[index[1]]
    elif index[0] == 7:
        out0 = {0:'r', 1:'n', 2:'b', 3:'q', 4:'k', 5:'b', 6:'n', 7:'r'}
        out = 'w' + out0[index[1]]
    else:
        return None
    return out
def piece_to_fname(piece):
    if piece is None:
        return None
    elif len(piece) == 2:
        if piece[0] in 'bw':
            if piece[1] in 'kqbnrp':
                return 'images/' + piece + '.png'
    return None
def index_to_fname(index, piece_f = piece_to_fname):    
    return piece_f(  index_to_piece(index)  )


def f1():
    print('Default callback from board')

class Board(ttk.Frame):
    def __init__(self, parent, piece_func = piece_to_fname, 
                 board_color = {0:'grey',1:'brown'}):
        super().__init__(parent)
        
        self.parent = parent
        self.squares = []
        self.board_color = board_color
        self.board = self.setup_new_game()
        self.grid(row=0, column=0)
        self.old_commands = {}
        
    def setup_new_game(self):
        board0 = []
        self.sq_dict = {}
        self.blist = []
        self.wlist = []
        for i in range(8):
            row = []
            for j in range(8):
                full_name = index_to_piece([i,j])
                fname = piece_to_fname(full_name)
                coords0 = np.array([i,j])
                if isinstance(full_name, str):
                    occupant0 = Piece(full_name, coords0)
                    # add to white or black list:
                    {'b':self.blist, 'w':self.wlist}[full_name[0]].append(coords0)
                else: 
                    occupant0 = None
                sq = Square(self, 
                            img = fname,
                            index = coords0, 
                            occupant = occupant0)
                #sq.config(command = sq.get_moves)
                self.sq_dict[tuple(coords0)] = sq
                self.squares.append(sq)
                sq.grid(row=i, column=j)
                row.append(sq)
            board0.append(row)
        for square in self.squares:
            cmd0 = partial(self.highlight_squares, square.index, self.moves(tuple(square.index)))
            #square.config(command = cmd0)
            square.set_command(cmd0)
        self.paint_checkerboard()
        return board0
    
    def highlight_squares(self, i0, indeces):
        self.paint_checkerboard()
        if indeces is not None:
            for index in indeces:
                sq = self.sq_dict[tuple(index)]
                sq.config(bg = 'yellow')
            self.enable_move(i0, indeces)
        else:
            pass
    def enable_move(self, i0, indeces):
        #print(i0, indeces)
        sq0 = self.sq_dict[tuple(i0)]
        for i in indeces:
            sqi = self.sq_dict[tuple(i)]
            def cmd(sq0, sqi):
                self.paint_checkerboard()
                sq0.change_photo(None)
                fname = piece_to_fname(sq0.occupant.color_name)
                sqi.change_photo(fname)
                sqi.change_occupant(sq0.occupant)
                sq0.change_occupant(None)
                self.reset_move_commands()
            sqi.set_command(partial(cmd, sq0, sqi))
            
    def reset_move_commands(self):
        for square in self.squares:
            cmd0 = partial(self.highlight_squares, square.index, self.moves(tuple(square.index)))
            #square.config(command = cmd0)
            square.set_command(cmd0)
                
                
    def paint_checkerboard(self):
        for i in range(8):
            for j in range(8):
                bg_color = self.board_color[(i%2+j%2)%2]
                self.sq_dict[(i,j)].config(bg = bg_color)
    def moves(self, coords):#, friendlies, enemies):
        sq = self.sq_dict[tuple(coords)]
        out = []
        if sq.occupant is None:
            return None
        name = sq.occupant.name
        if coords is None:
            return None
        elif name is 'n':# Knight
            for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1), (-2,-1)]:
                out0 = np.array([x,y]) + coords
                if in_board_space(out0):
                    out.append(out0)
                    
        elif name is 'b': # Bishop
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(-8,9):
                    out0 = coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == coords).all():
                        out.append(out0)
                        
        elif name is 'r': # Rook
            for xy in [np.array([1,0]), np.array([0,1])]:
                for multiplier in range(-8,9):
                    out0 = coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == coords).all():
                        out.append(out0)
        elif name is 'q': # Queen
            for xy in [np.array([1,0]), np.array([0,1]), 
                       np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(-8,9):
                    out0 = coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == coords).all():
                        out.append(out0)
        elif name is 'k':
            for x in range(-1,2):
                for y in range(-1,2):
                    out0 = coords + np.array([x,y])
                    if in_board_space(out0) and not (out0 == coords).all():
                        out.append(out0)
            
        elif name is 'p': # Pawn
            sign = {'b':1,'w':-1}[sq.occupant.color]
            for xy in [(sign,0), (sign,1),(sign,-1)]:
                out0 = np.array(xy) + coords
                if in_board_space(out0):
                    out.append(out0)
        else:
            out = [coords]
        
        return out
if __name__ == '__main__':
   
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root)
    root.mainloop()
