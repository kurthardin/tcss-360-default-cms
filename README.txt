File structure, feel free to modify and add stuff as needed.

/
root directory of the system. (not *nix style root...)

/cms.jar
the executable program

/users.dat
a text file that contains a list of users, one per line, with a delimiter
character separating each field

/conf/
the directory which contains conferences

/conf/[conference hash]/
directories for particular conferences (perhaps we should just use an
index instead of a hash?)

/conf/[conference hash]/info.dat
the file that contains information about the particular conference, such
as name of the conference, program chair, etc

/conf/[conference hash]/[paper index]/
directories for a particular paper in the conference. using the name of
the paper might make directory titles too long.

/conf/[conference hash]/[paper index]/info.dat
a file that contains information about a paper, such as the title (for
displaying in the software), author, reviewers, etc

/conf/[conference hash]/[paper index]/[review index]/
a directory for a particular review

/conf/[conference hash]/[paper index]/[review index]/info.dat
a file that contains all the numerical data (1-5 ratings) of a review as
well as the path of the document that contains the rational for the review.


