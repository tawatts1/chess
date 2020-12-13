#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 14:53:51 2020

@author: ted
"""
import tkinter as tk
from tkinter import ttk
def none_callback():
    print('None callback')

class Square(tk.Button):
    def __init__(self, parent, fname = None, color = 'yellow'):
        super().__init__(parent)

        self.parent = parent
        self.fname = fname
        
        if fname is None:
            photo = tk.PhotoImage()
        else:
            photo = tk.PhotoImage(file = fname)
        
        self.config(image = photo, bg = color, 
                            height = 60, width = 60)
        self.image = photo
        self.grid(row=0, column=0)
    def change_photo(self, fname):
        if fname is not None:
            photo = tk.PhotoImage(file = fname)
        else:
            photo = tk.PhotoImage()
        self.config(image = photo)
        self.image = photo
    def set_command(self,cmd):
        self.config(command = cmd)
        self.command = cmd
    
if __name__ == '__main__':
    
    root = tk.Tk()
    s1 = Square(root, fname="images/bq.png")
    s1.set_command(lambda : s1.change_photo(None))
    root.mainloop()
