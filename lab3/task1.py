import os
import math
import matplotlib.pyplot as plot
DIR_PATH = "./"

def read_data(file_name):
    Index = 0
    X = 1
    Y = 2
    Z = 3

    parsed_lines = []
    with open(file_name, 'r') as file:
        line = file.readline()

        while "END " not in line:
            line = list(filter(None, line.split(" ")))
            if line[DATA_TYPE] == "ATOM" and \
                line[ATOM_TYPE] == "C" and \
                    line[ANNOTATED_ATOM_TYPE] == "CA":
                parsed_lines.append([float(line[Index]),
                                     float(line[X]),
                                     float(line[Y]),
                                     float(line[Z])])

            line = file.readline()
    return parsed_lines

def distance(a1, a2):
    """Calculates the euclidian distance between two atoms"""
    [x1, y1, z1] = a1
    [x2, y2, z2] = a2

    return math.sqrt((x1 - x2)**2 + (y1 - y2)**2 + (z1 - z2)**2)

def find_distance(atom, atoms)
    "compares an atom to all remainder atoms in the list"
    neighbours = 0
    for  j in range(len(atoms))
        if (distance(atom, atoms[j]) <= 4)
            neighbours += 1
    if neighbours == 1 
        return 1

def find_edge(parsed_lines):
    for j in range(len(atoms)):
        nbr_collisions += count_collisions(atoms[j], atoms[j:])
            
def main():
    file_name = input("Give a path to TXT file:")
    file_name = os.path.join(DIR_PATH, "./{}.txt"
                             .format(file_name.upper()))
    atoms = read_data(file_name)
    dist_map = distance_map(atoms)
    plot.show()

if __name__ == '__main__':
    main()