#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""

from Virtual_board import VBoard
import tkinter as tk
from tkinter import ttk
#import tkMessageBox
import time
from time import sleep
from functools import partial
from moves0 import moves, in_checkmate, special_move
from game_setup import piece_to_fname, standard_game
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
            self.reset_move_commands(color = self.turn)
        
    def enable_move(self, i0, indeces):
        for i in indeces:
            sqi = self.sq_dict[tuple(i)]
            
            def cmd(c1, c2):
                if not self.ai2:
                    vb = VBoard(self.sq_dict)
                    if not in_checkmate(vb, self.turn):
                        self.paint_checkerboard()
                        self.execute_sq_move(c1,c2)
                        vb = VBoard(self.sq_dict)
                        #print(vb)
                        self.change_turn()
                        if not in_checkmate(vb, self.turn):
                            self.parent.update()
                            
                            # have ai do its thing
                            if self.ai:
                                c3, c4 = self.ai(vb, color = 'b')
                                
                                self.execute_sq_move(c3,c4)
                                vb = VBoard(self.sq_dict)
                                self.change_turn()
                        else:
                            print('checkmate')
                        self.reset_move_commands(color = self.turn)
                    else:
                        print('checkmate')
                else: # if there are two ais:
                    vb = VBoard(self.sq_dict)
                    #brk = False
                    for i in range(150): 
                        t0 = time.time()
                        if in_checkmate(vb,self.turn):
                            break
                        #if not self.has_two_kings():
                        #    brk=True
                        if self.turn=='w':
                            c3,c4 = self.ai(vb, self.turn)
                        else: 
                            c3,c4 = self.ai2(vb, self.turn)
                        t1 = time.time()
                        
                        self.execute_sq_move(c3, c4)
                        self.change_turn()
                        self.parent.update()
                        vb = VBoard(self.sq_dict)
                        #if brk:
                        #    print('king slain')
                        #    break
                        if t1-t0 < 2:
                            sleep(2-t1+t0)
                        #sleep(1)
                    else:
                        print('stalemate')

            sqi.set_command(partial(cmd, i0, i))
            
    def execute_sq_move(self, c1, c2):
        sq1 = self.sq_dict[tuple(c1)]
        sq2 = self.sq_dict[tuple(c2)]
        spc_mv = special_move(self.sq_dict, c1, c2)
        if spc_mv == 'promotion':
            sq1.occupant = sq1.occupant[0] + 'q'
        
        sq1.change_photo(None)
        fname = piece_to_fname(sq1.occupant)
        sq2.change_photo(fname)
        sq2.change_occupant(sq1.occupant)
        sq1.change_occupant(None)
        print(c1, ' --> ', c2)
    def reset_move_commands(self, color = 'w'):
    
        for square in self.sq_dict.values():
            cmd0 = partial(self.highlight_squares, square.index,
                            moves(self.sq_dict, tuple(square.index), color))
            square.set_command(cmd0)

    def paint_checkerboard(self):
        for i in range(8):
            for j in range(8):
                bg_color = self.board_color[(i%2+j%2)%2]
                self.sq_dict[(i,j)].config(bg = bg_color)
    def change_turn(self):
        self.turn = {'b':'w','w':'b'}[self.turn]
    def has_two_kings(self):
        i=0
        for sq in self.sq_dict.values():
            if sq.occupant is not None:
                if sq.occupant[1] == 'k':
                    i+=1
        if i==2:
            return True
        else:
            return False
if __name__ == '__main__':
    from pyjava_ai import java_ai
    ai1 = partial(java_ai, **{'N':4, 'special_option': 'None'})
    ai2 = partial(java_ai, **{'N':2, 'special_option': 'None'})
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root, ai = ai1, ai2 = ai2)
    root.mainloop()
