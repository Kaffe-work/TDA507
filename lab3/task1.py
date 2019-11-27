import os
import math
DIR_PATH = "./"
threshold = 4
threshold_min = 3.7

def read_data(file_name):

    index = 0
    X = 1
    Y = 2
    Z = 3    
    parsed_lines = []
    with open(file_name, 'r') as file:
        line = file.readline()
        while line:
            line = line.replace("\t", " ")
            line = list(filter(None, line.split(" ")))
            parsed_lines.append({"index": int(line[index]),"x": float(line[X]),"y": float(line[Y]),"z": float(line[Z])})
            line = file.readline()
    return parsed_lines

def distance(a1, a2):
    """Calculates the euclidian distance between two atoms"""
    return math.sqrt((a1["x"] - a2["x"])**2 + (a1["y"] - a2["y"])**2 + (a1["z"] - a2["z"])**2)


def within_threshold(a1, a2):
    return ((a1['index']!= a2['index']) and distance(a1,a2) < threshold)

def find_first_atom(atom, atoms):
    "compares an atom to all remainder atoms in the list"
    neighbours = []
    for  atom_i in atoms:
        if within_threshold(atom, atom_i):
            neighbours.append(atom_i)
    return neighbours 
 

def find_end(atoms):
    nbr_collisions = 0 
    for a in atoms:
        if len(find_first_atom(a, atoms)) == 1:
            return a

def find_edge(fixed, rest):
    if len(rest) < 2:
        return fixed + rest
    else:
        head = fixed[-1]
        neighbours = find_first_atom(fixed, rest)
        fixed.append(neighbours)
        rest = remove_atom(neighbours, rest)
        find_edge(fixed, rest)
  

def remove_atom(atom_remove, atoms):
    return filter(lambda a : a['index'] != edge['index'], atoms)

            
def main():
    file_name = input("Give a path to TXT file:")
    file_name = os.path.join(DIR_PATH, "{}.txt".format(file_name))
    atoms = read_data(file_name)
    edge = find_end(atoms)
    for a in atoms:
        print(find_end(atoms))
    sequence = find_edge([edge], remove_atom(edge, atoms))

if __name__ == '__main__':
    main()