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

class VBoard():
    def __init__(self, board_sq_dict):
        self.sq_dict = {}
        for sq in board_sq_dict.values():
            self.sq_dict[tuple(sq.index)] = VSquare(index = tuple(sq.index), occupant = sq.occupant)
        
    def moves(self, coords, color = 'b'):
        if 'moves' not in sys.modules:
            from moves0 import moves
            #print(sys.modules)
        return moves(self.sq_dict, coords, color)
    def get_next_boards(self, color = 'b'):
        '''
        
        
        Parameters
        ----------
        color : string, optional
            Color that can move, either 'b' or 'w'. The default is 'b'.

        Returns
        -------
        All of the possible virtual board configurations after one move by the color.
        '''
        
        out = []
        for i in range(8):
            for j in range(8):
                occ = self.sq_dict[(i,j)].occupant
                if occ is not None and occ[0] == color:
                    mvs = self.moves((i,j))
                    for move in mvs:
                        board_1 = VBoard(self.sq_dict.copy())
                        board_1.execute_move((i,j), move)
                        out.append([(i,j),move,board_1])
        return out
        
    def execute_move(self, c1, c2):
        occ1 = self.sq_dict[tuple(c1)].occupant
        #occ2 = self.sq_dict[tuple(c2)].occupant
        self.sq_dict[tuple(c1)].change_occupant(None)
        self.sq_dict[tuple(c2)].change_occupant(occ1)
        return self
    def __str__(self):
        out = ''
        for i in range(8):
            s = ''
            for j in range(8):
                occ = self.sq_dict[(i,j)].occupant
                if occ is None:
                    occ='0'
                s+= occ + '\t'
            out +=s + '\n'
        return out
            
    
    
    
if __name__ == '__main__':

    pass