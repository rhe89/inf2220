For å kjøre programmet etter kompilering:

> java Oblig2 <tekstfil> <manpower>

Sjekk også index.html i javadoc-mappen!

Programmet gjør som oppgaven ber om. Det er likevel et par ting som
nok kunne vært gjort bedre. Blant annet er kjøringen av oppgavene
(runProject()) veldig knotete, med mange if-statements for å sjekke
ditten og datten.

I tillegg tror jeg det å finne tidligste start for en oppgave
kan gjøres med et dybde-først-søk, mens jeg gjør det med et
bredde-først-søk (hvor en oppgave ikke går til barna før alle foreldre
er ferdig).

Jeg tar heller ikke særlig hensyn til manpower (utenom å lese det inn
fra kommandolinje, samt å skrive ut samlet manpower under kjøringen
av prosjektet).

De fire spørsmålene i 2.2 føler jeg besvares i koden.
De kritiske oppgavene finner man ut ved å kjøre listTasks() i Project,
mens kortest mulig kjøretid samt bruk av manpower vises under kjøringen
av prosjektet (altså i utfil.txt).

Jeg har valgt å bare "pushe" det som kommer i terminalen fra
println til utfil.txt (med '>'-parameteren), istedet for å
implementere skriving til fil i programmet, da dette ville
blitt så og si likt som runProject(), og jeg så på det som
unødvendig å gjøre.
