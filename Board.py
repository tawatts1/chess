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


class Board(ttk.Frame):
    def __init__(self, parent, piece_func = piece_to_fname,
                 board_color = {0:'grey',1:'brown'}):
        super().__init__(parent)

        self.parent = parent
        self.squares = []
        self.board_color = board_color
        self.board_sq, self.board_str = self.setup_new_game()
        self.grid(row=0, column=0)
        self.old_commands = {}

    def setup_new_game(self):
        board0 = []
        boardstr = []
        self.sq_dict = {}
        self.blist = []
        self.wlist = []
        for i in range(8):
            row = []
            rowstr = []
            for j in range(8):
                full_name = index_to_piece([i,j])
                fname = piece_to_fname(full_name)
                coords0 = np.array([i,j])
                if isinstance(full_name, str):
                    occupant0 = full_name
                    # add to white or black list:
                    {'b':self.blist,
                        'w':self.wlist}[full_name[0]].append(coords0)
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
                rowstr.append(occupant0)
            board0.append(row)
            boardstr.append(rowstr)
        self.reset_move_commands()
        '''
        for square in self.squares:
            cmd0 = partial(self.highlight_squares, square.index,
                self.moves(tuple(square.index)))
            #square.config(command = cmd0)
            square.set_command(cmd0)
        '''
        self.paint_checkerboard()
        return board0, boardstr

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
        for i, square in enumerate(self.squares):
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
