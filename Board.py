#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""

from Virtual_board import VBoard
from Square import Square
import tkinter as tk
from tkinter import ttk
#import tkMessageBox
import time
from time import sleep
from functools import partial
from move_calc import moves, in_checkmate, special_move
from game_setup import piece_to_fname
from game_ai import random_move



class Board(ttk.Frame):
    def __init__(self, parent, piece_func = piece_to_fname,
                 board_color = {0:'grey',1:'brown'}, 
                 ai = random_move, ai2 = None):
        super().__init__(parent)

        self.parent = parent
        self.board_color = board_color
        self.ai = ai
        self.ai2 = ai2
        self.turn = 'w'
        self.setup_new_game()
        self.grid(row=0, column=0)
        

    def setup_new_game(self):
        self.vb = VBoard()
        #self.board_sq, self.board_str, self.sq_dict = standard_game(self)
        #self.reset_move_commands()
        self.sq_arr = []
        for i in range(8):
            row = []
            for j in range(8):
                piece = self.vb[i][j]
                fname = piece_to_fname(piece)
                sq = Square(self.parent,
                            fname = fname)
                sq.grid(row=i, column=j)
                row.append(sq)
            self.sq_arr.append(row)
        self.reset_move_commands()
        self.paint_checkerboard()

    def highlight_squares(self, i0, indeces):
        self.paint_checkerboard()
        if indeces is not None:
            for index in indeces:
                sq = self.sq_arr[index[0]][index[1]]
                sq.config(bg = 'yellow')
            self.enable_move(i0, indeces)
        else:
            self.reset_move_commands(color = self.turn)
     
    def enable_move(self, i0, indeces):
        for i in indeces:
            sqi = self.sq_arr[i[0]][i[1]]
            
            def cmd(c1, c2):
                if not self.ai2:
                    if not in_checkmate(self.vb, self.turn):
                        self.paint_checkerboard()
                        self.execute_sq_move(c1,c2)
                        #print(vb)
                        self.change_turn()
                        if not in_checkmate(self.vb, self.turn):
                            self.parent.update()
                            
                            # have ai do its thing
                            if self.ai:
                                c3, c4 = self.ai(self.vb, color = 'b')
                                self.execute_sq_move(c3,c4)
                                #vb = VBoard(self.sq_dict)
                                self.change_turn()
                        else:
                            print('checkmate')
                        self.reset_move_commands(color = self.turn)
                    else:
                        print('checkmate')
                else: # if there are two ais:
                    
                    for i in range(150): 
                        t0 = time.time()
                        if in_checkmate(self.vb,self.turn):
                            break
                       
                        if self.turn=='w':
                            c3,c4 = self.ai(self.vb, self.turn)
                        else: 
                            c3,c4 = self.ai2(self.vb, self.turn)
                        t1 = time.time()
                        
                        self.execute_sq_move(c3, c4)
                        self.change_turn()
                        self.parent.update()
                        
                        if t1-t0 < 2:
                            sleep(2-t1+t0)
                        #sleep(1)
                    else:
                        print('stalemate')

            sqi.set_command(partial(cmd, i0, i))
            
    def execute_sq_move(self, c1, c2):
        piece1 = self.vb[c1[0]][c1[1]]
        piece2 = self.vb[c2[0]][c2[1]]
        spc_mv = special_move(self.vb, c1, c2)
        if spc_mv == 'promotion':
            piece1 = piece1[0] + 'q'
        
        self.sq_arr[c1[0]][c1[1]].change_photo(None)
        fname = piece_to_fname(piece1)
        self.sq_arr[c2[0]][c1[1]].change_photo(fname)
        
        print(c1, ' --> ', c2)
    def reset_move_commands(self, color = 'w'):
        for i in range(8):
            for j in range(8):
                square = self.sq_arr[i][j]
                cmd0 = partial(self.highlight_squares, (i,j),
                                moves(self.vb, (i,j), color))
                square.set_command(cmd0)

    def paint_checkerboard(self):
        for i in range(8):
            for j in range(8):
                bg_color = self.board_color[(i%2+j%2)%2]
                self.sq_arr[i][j].config(bg = bg_color)
    def change_turn(self):
        self.turn = {'b':'w','w':'b'}[self.turn]
   
if __name__ == '__main__':
    from pyjava_ai import java_ai
    ai1 = partial(java_ai, **{'N':4, 'special_option': 'None'})
    ai2 = partial(java_ai, **{'N':2, 'special_option': 'None'})
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root, ai = ai1)#, ai2 = ai2)
    root.mainloop()
