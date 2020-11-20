from Board import Board
from pyjava_ai import java_ai
from functools import partial
import tkinter as tk

#white ai:
ai1 = partial(java_ai, **{'N':4, 'special_option': 'None'})

# black ai:
ai2 = partial(java_ai, **{'N':4, 'specialty':'pawn','extra_moves':1})

root = tk.Tk()
#root.protocol("WM_DELETE_WINDOW", quit_window())
b1 = Board(root, ai = ai1, ai2 = ai2)
root.mainloop()