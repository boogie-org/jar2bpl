
procedure java.lang.Object$java.lang.Object$clone($this:ref) returns ($other:ref)
    modifies $heap;
{
        havoc $other;
        assume !$heap[$other,$alloc];
        $heap := $heap[$other,$alloc := true];
        assume $other != $null;
        $heap := $heap[$other,$type := $heap[$this,$type] ];
}
