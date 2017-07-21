# This is a piece of code to match some syntax in Perl and translate into Python.
# This is COMP2041 individual coursework.

my ($line) = @_;
    $line =~ s/;//g;
    # change comments:
    if ($line =~ /^\#/ && $line !~ /#!\/usr\/bin\/perl -w/) {
        push(@outputs,$line);
    # calling different types of print functions:
    } elsif ($line =~ /#!\/usr\/bin\/perl -w/) {
        Title $line;
    } elsif ($line =~ /print/) {
        $buff = $line;
        @buff = $buff =~ /./g;
        $indent = 0;
        foreach my $x (@buff) {
            if ($x eq "p") {
                last;
            }
            $indent++;
        }
        if ($line =~ /\w*\s*\$\w+\s+\w+/) {
            # this is to match combined strings with variables.
            # such as "$num_line lines" --> (%s lines)
            $line =~ s/^[ ]*//g;
            @line = split("\"", $line);
            CombinedPrint $indent, @line;
        } elsif ($line !~ /\$/ && $line !~ /\@/) {
            $line =~ s/^[ ]*//g;
            @line = split("\"", $line);
            PrintFunction $indent, @line;
        } elsif ($line =~ /\$/) {
            if ($line =~ /print/) {
                $line =~ s/^[ ]*//g;
                @line = split("\"", $line);
                VariablesPrint $indent, @line;
            }
        } elsif ($line =~ /\@/) {
            if ($line =~ /join/) {
                @line = split("\"", $line);
                PrintJoin $indent, @line;
            } elsif ($line =~ /reverse/) {
                $line =~ s/\@//g;
                @words = split(" ", $line);
				$exp0 = $words[0];
				$exp1 = $words[1];
				$exp2 = $words[2];
                $line =~ s/print\s*reverse.*/$exp0\($exp2\.$exp1\(\)\)/;
                push(@outputs, $line);
            } else {
                $line =~ s/\@//g;
                @words = split(" ", $line);
				$exp0 = $words[0];
				$exp1 = $words[1];
                $line =~ s/print.*/$exp0\($exp1\)/;
                push(@outputs, $line);
            }
        }
    # foreach statements:
    # conditions have:
    # foreach $i (@array)
    # foreach $i (sort keys @array)
    # foreach $i (0..4)
    } elsif ($line =~ /foreach\s*/) {
        $line =~ s/my\s*//g;
        $line =~ s/foreach/for/;
        $line =~ s/\$//g;
        $line =~ s/\(/in /;
        if ($line =~ /\@ARGV/) {
            $line =~ s/\@ARGV/sys.argv[1:]/
        } else {
            $line =~ s/\@//g;
        }
        $line =~ s/\)//;
        $line =~ s/[\s]*{/:/;
        if ($line =~ /keys/) {
            $buffer = $line;
            $buffer =~ s/.*keys/keys/;
            $buffer =~ s/\%//;
            $buffer =~ s/://;
            @words = split(" ", $buffer);
            $expr = $words[1].".keys():";
            $line =~ s/keys.*/$expr/;
        }
        if ($line =~ /\d\.\./) {
            $buffer = $line;
            @preNumbers = split(" ", $buffer);
            foreach my $x (@preNumbers) {
                if ($x =~ /\d\.\./) {
                    $x =~ s/://;
                    $x =~ s/\.\./,/;
                    if ($x !~ /#ARGV/) {
                        @nums = split(",",$x);
                        for (my $y = 0; $y < 2;$y++) {
                            if ($y == 0) {
                                $start = $nums[$y];
                            } else {
                                $end = $nums[$y];
                                $end++;
                            }
                        }
                    } elsif ($x =~ /\#ARGV/) {
                        $line =~ s/0\.\.\#ARGV/range(len(sys.argv) - 1)/;
                    }
                }
            }
            if ($line !~ /ARGV/ && $line !~ /sys\.argv/) {
                $line =~ s/\d\.\.[\d\#\w]*/range($start,$end)/;
            }
        }
        push(@outputs, $line);
    # calling for loop functions:
    } elsif ($line =~ /for\s*/) {
        $line =~ s/my//g;
        $buff = $line;
        @buff = $buff =~ /./g;
        $indent = 0;
        foreach my $x (@buff) {
            if ($x ne " ") {
                last;
            }
            $indent++;
        }
        @line = split(" ", $line);
        ForLoop $indent, @line;
    # grep any stuff associate with $variable:
    # if some functions are not associate with $variable, I also include outside this condition.
    } 
