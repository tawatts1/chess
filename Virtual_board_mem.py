#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""
from Virtual_square import VSquare
import sys

import numpy as np
#from moves import moves
from game_setup import piece_to_fname, standard_game

class VBoard_m():
    def __init__(self, board_sq_dict, row_sq_dict):
        if row_sq_dict:
            #this is necessary because setting a dict outright equal is different
            #than setting every element equal to every element. 
            #Because the elements are lists there will be two different board types
            #with rows that have the same elements, and they will be referencing the 
            #same memory location!
            self.rsq_dict = {}
            for i in range(8):
                self.rsq_dict[i] = row_sq_dict[i]#.copy()
        elif board_sq_dict:
            self.rsq_dict = {}
            for i in range(8):
                row = []
                for j in range(8):
                    row.append(board_sq_dict[(i,j)].occupant)
                self.rsq_dict[i] = row
        

    def moves(self, coords, color):
        if 'moves' not in sys.modules:
            from moves_mem import moves_m
            #print(sys.modules)
        return moves_m(self.rsq_dict, coords, color)
    def get_next_boards(self, color):
        '''
        
        
        Parameters
        ----------
        color : string
            Color that can move, either 'b' or 'w'. 
        Returns
        -------
        All of the possible virtual board configurations after one move by the color.
        '''
        
        out = []
        for i in range(8):
            for j in range(8):
                occ = self.rsq_dict[i][j]
                if occ is not None and occ[0] == color:
                    mvs = self.moves((i,j), color)
                    if len(mvs)>0:
                        for move in mvs:
                            board_1 = VBoard_m(None, self.rsq_dict)
                            board_1.execute_move((i,j), move)
                            out.append([(i,j),move,board_1])
        return out
    def has_move(self, color):
        for i in range(8):
            for j in range(8):
                occ = self.rsq_dict[i][j]
                if occ is not None and occ[0] == color:
                    mvs = self.moves((i,j), color)
                    if len(mvs)>0:
                        return True
        return False
    def execute_move(self, c1, c2):
        occ1 = self.rsq_dict[c1[0]][c1[1]]
        #occ2 = self.sq_dict[tuple(c2)].occupant
        i1,j1 = c1
        i2,j2 = c2
        
        if i1 == i2:
            for i in range(8):
                if i==i1:
                    row = self.rsq_dict[i].copy()
                    row[j2] = occ1
                    row[j1] = None
                else:
                    row = self.rsq_dict[i] # no copy necessary
                self.rsq_dict[i] = row
        else:
            row1 = self.rsq_dict[i1].copy()
            row2 = self.rsq_dict[i2].copy()
            row2[j2] = row1[j1]
            row1[j1] = None
            self.rsq_dict[i1] = row1
            self.rsq_dict[i2] = row2
            for i in range(8):
                if i not in [i1,i2]:
                    self.rsq_dict[i] = self.rsq_dict[i]
        return self
    def __str__(self):
        out = ''
        for i in range(8):
            s = ''
            for j in range(8):
                occ = self.rsq_dict[i][j]
                if occ is None:
                    occ='0'
                s+= occ + '\t'
            out +=s + '\n'
        return out
            
def gen_standard_board():
    from game_setup import index_to_piece
    sqd = {}
    for i in range(8):
        row = []
        for j in range(8):
            row.append(index_to_piece((i,j)))
        sqd[i] = row
    vbm = VBoard_m(None,rsd)  
    return vbm  
if __name__ == '__main__':

    pass