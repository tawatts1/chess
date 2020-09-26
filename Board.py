#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""
from Square import Square
import tkinter as tk
from tkinter import ttk
#import tkMessageBox
from time import sleep
import numpy as np
from functools import partial
from moves import in_board_space, moves
from game_setup import piece_to_fname, standard_game




class Board(ttk.Frame):
    def __init__(self, parent, piece_func = piece_to_fname,
                 board_color = {0:'grey',1:'brown'}):
        super().__init__(parent)

        self.parent = parent
        #self.squares = []
        self.board_color = board_color
        self.setup_new_game()
        self.grid(row=0, column=0)
        self.old_commands = {}

    def setup_new_game(self):
        self.board_sq, self.board_str, self.sq_dict = standard_game(self)
        self.reset_move_commands()
        
        self.paint_checkerboard()

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
                fname = piece_to_fname(sq0.occupant)
                sqi.change_photo(fname)
                sqi.change_occupant(sq0.occupant)
                sq0.change_occupant(None)
                self.reset_move_commands()
            sqi.set_command(partial(cmd, sq0, sqi))

    def reset_move_commands(self):
        for square in self.sq_dict.values():
            cmd0 = partial(self.highlight_squares, square.index,
                            moves(self.sq_dict, tuple(square.index)))
            square.set_command(cmd0)


    def paint_checkerboard(self):
        for i in range(8):
            for j in range(8):
                bg_color = self.board_color[(i%2+j%2)%2]
                self.sq_dict[(i,j)].config(bg = bg_color)
    
if __name__ == '__main__':

    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root)
    root.mainloop()
