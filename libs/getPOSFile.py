
import sys

ARK_file = open(sys.argv[1], "r")
lines = ARK_file.readlines()

output = []

comment = ''

for line in lines:
	tokens = line.split()

	if (len(tokens) > 0):
		comment = comment + tokens[0] + "/" + tokens[1] + " "
	else:
		output.append(comment)
		comment = ''
ARK_file.close()

output_file = open(sys.argv[2], "a")
for output_line in output:
	output_file.write(output_line[0:-1] + "\n")
output_file.close()

