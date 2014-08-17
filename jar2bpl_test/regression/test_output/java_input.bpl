type ref;
type javaType;
type Field $GenericType__0;
type $heap_type = <$GenericType__0>[ref,Field $GenericType__0]$GenericType__0;
type boolArrHeap_type = [ref][int]bool;
type refArrHeap_type = [ref][int]ref;
type realArrHeap_type = [ref][int]int;
type intArrHeap_type = [ref][int]int;
const unique $null : ref;
const unique $type : Field javaType;
const unique $alloc : Field bool;
const { :sourceloc "Object.java",-1,-1,-1,-1 } unique java.lang.Object : javaType extends  complete;
const { :sourceloc "InfeasibleCode01.java",-1,-1,-1,-1 } unique InfeasibleCode01 : javaType extends  unique java.lang.Object complete;
const { :sourceloc "Serializable.java",-1,-1,-1,-1 } unique java.io.Serializable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "Comparable.java",-1,-1,-1,-1 } unique java.lang.Comparable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "CharSequence.java",-1,-1,-1,-1 } unique java.lang.CharSequence : javaType extends  unique java.lang.Object complete;
const { :sourceloc "String.java",-1,-1,-1,-1 } unique java.lang.String : javaType extends  unique java.lang.Comparable, unique java.lang.CharSequence, unique java.lang.Object, unique java.io.Serializable complete;
const { :sourceloc "InfeasibleCode02.java",-1,-1,-1,-1 } unique InfeasibleCode02 : javaType extends  unique java.lang.Object complete;
var $heap : $heap_type;
var $intArrayType : javaType;
var $charArrayType : javaType;
var $boolArrayType : javaType;
var $byteArrayType : javaType;
var $longArrayType : javaType;
var $arrSizeHeap : [ref]int;
var $stringSizeHeap : [ref]int;
var $boolArrHeap : boolArrHeap_type;
var $refArrHeap : refArrHeap_type;
var $realArrHeap : realArrHeap_type;
var $intArrHeap : intArrHeap_type;
var int$InfeasibleCode01$beginEndTag0 : Field int;
var int$InfeasibleCode02$x0 : Field int;
function $arrayType(t:javaType) returns ($ret:javaType);
function $intToReal(x:int) returns ($ret:real);
function $intToBool(x:int) returns ($ret:bool) { (if x == 0 then false else true) }
function $refToBool(x:ref) returns ($ret:bool) { (if x == $null then false else true) }
function $boolToInt(x:bool) returns ($ret:int) { (if x == true then 1 else 0) }
function $cmpBool(x:bool, y:bool) returns ($ret:int);
function $cmpRef(x:ref, y:ref) returns ($ret:int);
function $cmpReal(x:real, y:real) returns ($ret:int) { (if x > y then 1 else (if x < y then -1 else 0)) }
function $cmpInt(x:int, y:int) returns ($ret:int) { (if x > y then 1 else (if x < y then -1 else 0)) }
function $bitOr(x:int, y:int) returns ($ret:int);
function $bitAnd(x:int, y:int) returns ($ret:int);
function $xorInt(x:int, y:int) returns ($ret:int);
function $shlInt(x:int, y:int) returns ($ret:int);
function $ushrInt(x:int, y:int) returns ($ret:int);
function $shrInt(x:int, y:int) returns ($ret:int);
procedure $new(obj_type:javaType) returns ($obj:ref);    ensures $obj != $null;    requires $heap[$obj,$alloc] == false;    ensures $heap[$obj,$type] == obj_type;    ensures $heap[$obj,$alloc] == true;    

procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref);    ensures $other != $null;    ensures $heap[$other,$type] == $heap[$this,$type];        ensures $heap[$other,$alloc] == true;

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) returns ($return:int);    

procedure void$InfeasibleCode01$infeasible08$1889($this:ref, $in_parameter__0:ref, $in_parameter__1:ref, $in_parameter__2:int) returns ($exception:ref);    

procedure int$java.lang.String$indexOf$102($this:ref, $in_parameter__0:ref, $in_parameter__1:int) returns ($return:int, $exception:ref);    

procedure void$InfeasibleCode01$$la$init$ra$$1890($this:ref) returns ($exception:ref);    

procedure void$java.lang.Object$$la$init$ra$$38($this:ref) returns ($exception:ref);    

procedure void$InfeasibleCode02$infeasible01$2084($this:ref) returns ($exception:ref);    

procedure void$InfeasibleCode02$infeasible07$2085($this:ref, $in_parameter__0:ref) returns ($exception:ref);    

procedure void$InfeasibleCode02$$la$init$ra$$2086($this:ref) returns ($exception:ref);    

implementation void$InfeasibleCode01$infeasible08$1889($this:ref, $in_parameter__0:ref, $in_parameter__1:ref, $in_parameter__2:int) returns ($exception:ref){
    
var searchString3 : ref;    
var temp$412 : int;    
var temp$513 : int;    
var temp$311 : int;    
var temp$614 : int;    
var temp$816 : int;    
var interncaret4 : int;    
var source2 : ref;    
var hitUp7 : int;    
var flaghitup6 : int;    
var temp$210 : int;    
var temp$08 : int;    
var temp$19 : int;    
var temp$715 : int;    
var temphitpoint5 : int;    
var this1 : ref;
    assume $this != $null;
    this1 := $this;
    source2 := $in_parameter__0;
    searchString3 := $in_parameter__1;
    interncaret4 := $in_parameter__2;
    temphitpoint5 := -1;
    flaghitup6 := 0;
    hitUp7 := 0;
  block1:
    temp$08 := 0;
    flaghitup6 := temp$08;
    assert { :sourceloc "InfeasibleCode01.java",96,4,96,60 } source2 != $null;
    call temp$19, $exception := int$java.lang.String$indexOf$102(source2, searchString3, interncaret4);
    temphitpoint5 := temp$19;
    if (temphitpoint5 > 0) {
        assert { :sourceloc "InfeasibleCode01.java",97,7,97,22 } true;
        goto block2;
    } else {
        assert { :sourceloc "InfeasibleCode01.java",97,7,97,22 } true;
    }
    goto block3;
  block2:
    assert { :sourceloc "InfeasibleCode01.java",97,42,97,52 } this1 != $null;
    temp$210 := $heap[this1,int$InfeasibleCode01$beginEndTag0];
    if (temphitpoint5 < temp$210) {
        assert { :sourceloc "InfeasibleCode01.java",97,42,97,52 } true;
        goto block4;
    } else {
        assert { :sourceloc "InfeasibleCode01.java",97,42,97,52 } true;
    }
    goto block3;
    goto block4;
  block4:
    temp$311 := hitUp7;
    temp$412 := temp$311 + 1;
    hitUp7 := temp$412;
    temp$513 := 1;
    flaghitup6 := temp$513;
    temp$614 := temphitpoint5;
    temp$715 := $stringSizeHeap[searchString3];
    temp$816 := temp$614 + temp$715;
    interncaret4 := temp$816;
  block3:
    if (flaghitup6 == 0) {
        assert { :sourceloc "InfeasibleCode01.java",103,11,103,19 } true;
        goto block5;
    } else {
        assert { :sourceloc "InfeasibleCode01.java",103,11,103,19 } true;
    }
    goto block1;
  block5:
    if (hitUp7 == 0) {
        assert { :sourceloc "InfeasibleCode01.java",104,6,104,15 } true;
        goto block6;
    } else {
        assert { :sourceloc "InfeasibleCode01.java",104,6,104,15 } true;
    }
    goto block7;
  block6:
  block7:
    return;
}


implementation void$InfeasibleCode01$$la$init$ra$$1890($this:ref) returns ($exception:ref){
    
var this17 : ref;
    assume $this != $null;
    this17 := $this;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this17);
    return;
}


implementation void$InfeasibleCode02$infeasible01$2084($this:ref) returns ($exception:ref){
    
var temp$122 : int;    
var temp$223 : int;    
var temp$324 : int;    
var temp$425 : int;    
var counter20 : int;    
var hit19 : int;    
var temp$021 : int;    
var this18 : ref;
    assume $this != $null;
    this18 := $this;
    hit19 := 0;
    counter20 := 0;
  block8:
    temp$021 := 0;
    hit19 := temp$021;
    assert { :sourceloc "InfeasibleCode02.java",67,7,67,7 } this18 != $null;
    temp$122 := $heap[this18,int$InfeasibleCode02$x0];
    if (temp$122 == counter20) {
        assert { :sourceloc "InfeasibleCode02.java",67,7,67,7 } true;
        goto block9;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",67,7,67,7 } true;
    }
    goto block10;
  block9:
    temp$223 := counter20;
    temp$324 := temp$223 + 1;
    counter20 := temp$324;
    temp$425 := 1;
    hit19 := temp$425;
    if (counter20 > 10000) {
        assert { :sourceloc "InfeasibleCode02.java",71,8,71,22 } true;
        goto block11;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",71,8,71,22 } true;
    }
    goto block12;
  block11:
    return;
  block12:
  block10:
    if (hit19 == 0) {
        assert { :sourceloc "InfeasibleCode02.java",76,11,76,13 } true;
        goto block13;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",76,11,76,13 } true;
    }
    goto block8;
  block13:
    return;
}


implementation void$InfeasibleCode02$infeasible07$2085($this:ref, $in_parameter__0:ref) returns ($exception:ref){
    
var temp$233 : ref;    
var j30 : int;    
var temp$738 : int;    
var temp$637 : int;    
var temp$334 : int;    
var temp$031 : int;    
var this26 : ref;    
var temp$536 : int;    
var temp$839 : int;    
var end29 : int;    
var temp27 : ref;    
var temp$435 : int;    
var repos28 : int;    
var temp$940 : int;    
var temp$132 : int;
    assume $this != $null;
    this26 := $this;
    temp27 := $in_parameter__0;
    repos28 := -1;
    end29 := -1;
    j30 := end29;
  block14:
    temp$031 := j30;
    temp$132 := temp$031 + 1;
    j30 := temp$132;
    temp$233 := temp27;
    temp$334 := j30;
    assert { :sourceloc "InfeasibleCode02.java",85,7,85,18 } temp$334 < $arrSizeHeap[temp$233] && temp$334 >= 0;
    temp$435 := $intArrHeap[temp$233][temp$334];
    temp$536 := temp$435;
    if (temp$536 == 97) {
        assert { :sourceloc "InfeasibleCode02.java",85,7,85,18 } true;
        goto block15;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",85,7,85,18 } true;
    }
    goto block16;
  block15:
    temp$637 := j30 - end29;
    temp$738 := temp$637 - 1;
    repos28 := temp$738;
  block16:
    if (repos28 == -1) {
        assert { :sourceloc "InfeasibleCode02.java",88,11,88,21 } true;
        goto block17;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",88,11,88,21 } true;
    }
    goto block18;
  block17:
    assert { :sourceloc "InfeasibleCode02.java",88,26,88,40 } temp27 != $null;
    temp$839 := $arrSizeHeap[temp27];
    if (j30 < temp$839) {
        assert { :sourceloc "InfeasibleCode02.java",83,2,88,42 } true;
        goto block14;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",88,26,88,40 } true;
    }
    goto block18;
    goto block14;
  block18:
    if (repos28 == -1) {
        assert { :sourceloc "InfeasibleCode02.java",89,6,89,16 } true;
        goto block19;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",89,6,89,16 } true;
    }
    goto block20;
  block19:
    temp$940 := 0;
    repos28 := temp$940;
  block20:
    return;
}


implementation void$InfeasibleCode02$$la$init$ra$$2086($this:ref) returns ($exception:ref){
    
var this41 : ref;
    assume $this != $null;
    this41 := $this;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this41);
    return;
}


