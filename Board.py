#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""

from Virtual_board import VBoard, WQ, BQ
from Square import Square
import tkinter as tk
from tkinter import ttk
import time
from time import sleep
from functools import partial
from move_calc import moves, in_checkmate, special_move
from game_setup import piece_to_fname
from game_ai import random_move


class Board(ttk.Frame):
    def __init__(self, parent, piece_func = piece_to_fname,
                 board_color = {0:'#FFB310',1:'#990033'},
                 indication_color =  {0:'#CAA453',1:'#974B64'}, 
                 highlight_color = "yellow",
                 ai = random_move, ai2 = None, 
                 debug_str = None):
        super().__init__(parent)
        icon = tk.PhotoImage(file = "images/bongcloud.png")
        parent.iconphoto(False, icon)
        self.parent = parent
        self.board_color = board_color
        self.indication_color = indication_color
        self.highlight_color = highlight_color
        self.ai = ai
        self.ai2 = ai2
        self.turn = 'w'
        self.setup_new_game(debug_str = debug_str)
        self.grid(row=0, column=0)
        

    def setup_new_game(self, debug_str):
        #each board object will contain a list of lists of piece lists:
        if debug_str:
            self.vb = VBoard(board_str = debug_str)
        else:
            self.vb = VBoard() # defualt is starting board positions
        
        #the 8x8 array of squares, each containing a button:
        self.sq_arr = []
        for i in range(8):
            row = []
            for j in range(8):
                piece = self.vb[i][j]
                fname = piece_to_fname(piece)
                # initiate each square:
                sq = Square(self.parent,
                            fname = fname)
                sq.grid(row=i, column=j)
                row.append(sq)
            self.sq_arr.append(row)
        
        self.reset_move_commands() # enables moves
        self.paint_checkerboard() # paints the light and dark squares

    def highlight_squares(self, i0, indeces):
        '''Highlight the squares that the user can move to based on the piece it 
        just clicked on, and allow the user to do that move by clicking on the 
        highlighted square. 

        i0: the coordinates of the square just clicked on
        indeces: the calculated coordinates that piece can move to. 

        If indeces is None it will reset the  move commands, '''
        self.paint_checkerboard(reset=False) #undo previous highlighting
        if indeces is not None:
            for index in indeces:
                sq = self.sq_arr[index[0]][index[1]]
                sq.config(bg = self.highlight_color)
                sq.set_command(partial(self.move_command, i0, index))
        else:
            self.reset_move_commands(color = self.turn)
     
    def move_command(self, c1, c2):
        '''The command that is carried out when the user clicks on a piece, 
         and then actually clicks on a move to carry it out. c1 and c2 are the
         coordinates of the piece and the coordinates of where it will go.
         
         If there is only one ai, this executes the move, and then initiates the
         ai deciding a move and then executing that move as well.

         If there are two ais, then it will not actually carry out the move, but
         will initiate each ai choosing moves and then executing them so the user
         can watch. 
         '''
        
        if not self.ai2: # only one ai
            self.paint_checkerboard()
            self.execute_move(c1,c2)
            print(self.vb)
            self.change_turn()

            if not in_checkmate(self.vb, self.turn):
                self.parent.update()
                
                # have ai do its thing
                if self.ai:
                    c3, c4 = self.ai(self.vb, color = 'b')
                    self.execute_move(c3,c4)
                    #vb = VBoard(self.sq_dict)
                    self.change_turn()
            else:
                print('checkmate')
            
            self.reset_move_commands(color = self.turn)
            
            if in_checkmate(self.vb, self.turn):
                print('checkmate, bruh. \nyou stink!')
            
        else: # if there are two ais:
            max_moves = 150
            for i in range(150): 
                print(self.vb)
                self.paint_checkerboard()
                if in_checkmate(self.vb,self.turn):
                    print(f'CHECKMATE DETECTED: {self.turn} loses')
                    print(self.vb)
                    break
            
                if self.turn=='w':
                    c3,c4 = self.ai(self.vb, self.turn)
                else: 
                    c3,c4 = self.ai2(self.vb, self.turn)
                
                
                self.execute_move(c3, c4)
                self.change_turn()
                self.parent.update()
                
            else:
                print(f'STALEMATE DETECTED: {max_moves} moves with no winner')

    def execute_move(self, c1, c2):
        '''
        record move in virtual board and in gui
        '''
        spc_mv = special_move(self.vb, c1, c2)
        if spc_mv == 'promotion':
            self.vb[c1[0]][c1[1]] = {'w':WQ,'b':BQ}[self.vb[c1[0]][c1[1]][0]]

        piece1 = self.vb[c1[0]][c1[1]]  
        sq1 = self.sq_arr[c1[0]][c1[1]]
        sq2 = self.sq_arr[c2[0]][c2[1]]

        sq1.change_photo(None)
        fname = piece_to_fname(piece1)
        sq2.change_photo(fname)
        
        # indicate the move by changing the background colors
        sq1.config(bg=self.indication_color[(c1[0]%2 + c1[1]%2)%2])
        sq2.config(bg=self.indication_color[(c2[0]%2 + c2[1]%2)%2])
        #record move in virtual board
        self.vb = self.vb.execute_move(c1,c2)
        print(c1, ' --> ', c2)
        
    def reset_move_commands(self, color = 'w'):
        for i in range(8):
            for j in range(8):
                square = self.sq_arr[i][j]
                cmd0 = partial(self.highlight_squares, (i,j),
                                moves(self.vb, (i,j), color))
                square.set_command(cmd0)

    def paint_checkerboard(self, reset = True):
        '''
        This colors all the squares to their normal color. If reset is False, if
        there are any move-indicating colored squares, they will remain that 
        color. Otherwise all will be changed to the standard board colors.
        '''
        for i in range(8):
            for j in range(8):
                shade_num = (i%2+j%2)%2
                sq = self.sq_arr[i][j]
                old_color = sq.cget('bg')
                if old_color in self.indication_color.values() and not reset:
                    bg_color = self.indication_color[shade_num]
                else:
                    bg_color = self.board_color[shade_num]
                    self.sq_arr[i][j].config(bg = bg_color)
    def change_turn(self):
        self.turn = {'b':'w','w':'b'}[self.turn]

def compile_java():
    import subprocess
    subprocess.run(
        ['javac', 'next_move.java'],
        check=True, 
        stdout=subprocess.PIPE).stdout.decode('ascii')

if __name__ == '__main__':
    from pyjava_ai import java_ai
    compile_java()
    #partial(java_ai, **{'N':N, 'post_strategy': white})
    #debug_str0 = "br_00_bb_00_bk_bb_00_br=bp_bp_bp_bp_00_bp_bp_bp=00_00_00_00_bp_bq_00_00=00_00_00_00_00_00_00_00=00_bn_00_wp_wk_00_00_00=00_00_00_00_wp_wn_00_00=wp_wp_wp_00_00_00_wp_wp=wr_wn_wb_00_00_wr_00_00"
    ai1 = partial(java_ai, **{'N':6, 'scoring': 'build_up'})
    #ai2 = partial(java_ai, **{'N':6, 'scoring': 'pos'})
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root, ai = ai1)#, ai2 = ai2)
    root.mainloop()
