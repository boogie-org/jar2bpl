
procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref, $exception:ref)
    modifies $heap;
	{
        havoc $other;
        assume !$heap[$other,$alloc];
        $heap := $heap[$other,$alloc := true];
        assume $other != $null;
        $heap := $heap[$other,$type := $heap[$this,$type] ];
	}

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) 
	returns ($return:int, $exception:ref); 
	ensures ( ($this==$other ==> $return==1) && ($this!=$other ==> $return==0) );
