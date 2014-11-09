1. Explanation of algorithm (plus 7. Credit):

The algorithm is quite heavily commented in the code, but I will go through the basics here.

Deciding shift length;
In the byte-array, shift length is set using standard BCS, (the length of the needle
for every char not in the needle, and the length of the needle - 1 - their own index for the chars in the
needle. The difference is that the number of wildcards in the needle is also substracted, to avoid skipping
potential wildcards when shifting.

The traversal of the haystack is also standard BCS, but my code is somewhat different than the code in
the pdf-foil from the lecture on Boyer-Moore on 10th of October. This is because i wanted to try to code
the algorithm from the beginning, using what i've learned when doing BCS on paper. The code from the lecture
has helped me when i got stuck, e. g. that a match has been found when the needle-position is zero.

The traversal goes as follows;
Starting from the end of the needle, if a match is found (meaning either a genuin char in the needle
matches a char at that position in the haystack, or that a wildcard is at the current needle-index),
a check is made to see if the needle index is at the beginning. In that case, a "needle match" has been found,
and the needle position is reset to the end of the needle. The counter keeping track of the position
in the haystack is then calculated as if it was a mismatch. If the needle index isn't at the beginning,
the index is decrementet by one, moving the needle index one position to the left.

If a mismatch occurs, the counter is incrementet by getting the char value in the byte-array from the char
at the haystack where the end of the needle is pointing, even if the mismatch is in the middle of the needle.
This is because we want to be sure to not overjump the needle. In the example below, 'E' is looked up
in the byte-array to find its corresponding shift distance.

                  |
haystack: A B C D E F G
needle        C _ L

2. Compilation and running
    > javac Oblig3.java

    > java Oblig3 [file with haystack] [file with needle]

3. Main method

The main method is found in the only java-file (Oblig3.java)

4. Assumptions

Only one assumption made - the needle- and haystack-files contains words or sentences on only one (the first) line in the file.

5. Pecularities

None that i'm aware of :p

6. Status of delivery

Everything should work, i'm only concerned about line 161-162, if this check is necessary, or maybe if the check should be more thorough.

TEST CASES:

