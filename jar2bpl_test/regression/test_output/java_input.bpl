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
procedure $new(obj_type:javaType) returns ($obj:ref);    ensures $heap[$obj,$type] == obj_type;        requires $heap[$obj,$alloc] == false;    ensures $obj != $null;    ensures $heap[$obj,$alloc] == true;

procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref);    ensures $other != $null;        ensures $heap[$other,$type] == $heap[$this,$type];    ensures $heap[$other,$alloc] == true;

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) returns ($return:int);        ensures ($this == $other ==> $return == 1) && ($this != $other ==> $return == 0);

procedure void$InfeasibleCode01$$la$init$ra$$1889($this:ref) returns ($exception:ref);    

procedure void$java.lang.Object$$la$init$ra$$38($this:ref) returns ($exception:ref);    

procedure void$InfeasibleCode02$infeasible01$1890($this:ref) returns ($exception:ref);    

procedure void$InfeasibleCode02$infeasible07$1891($this:ref, $in_parameter__0:ref) returns ($exception:ref);    

procedure void$InfeasibleCode02$$la$init$ra$$1892($this:ref) returns ($exception:ref);    

implementation void$InfeasibleCode01$$la$init$ra$$1889($this:ref) returns ($exception:ref){
    
var this1 : ref;
    assume $this != $null;
    this1 := $this;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this1);
    return;
}


implementation void$InfeasibleCode02$infeasible01$1890($this:ref) returns ($exception:ref){
    
var this2 : ref;    
var counter4 : int;    
var temp$16 : int;    
var temp$49 : int;    
var hit3 : int;    
var temp$05 : int;    
var temp$38 : int;    
var temp$27 : int;
    assume $this != $null;
    this2 := $this;
    hit3 := 0;
    counter4 := 0;
  block1:
    temp$05 := 0;
    hit3 := temp$05;
    assert { :sourceloc "InfeasibleCode02.java",67,7,67,7 } this2 != $null;
    temp$16 := $heap[this2,int$InfeasibleCode02$x0];
    if (temp$16 == counter4) {
        assert { :sourceloc "InfeasibleCode02.java",67,7,67,7 } true;
        goto block2;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",67,7,67,7 } true;
    }
    goto block3;
  block2:
    temp$27 := counter4;
    temp$38 := temp$27 + 1;
    counter4 := temp$38;
    temp$49 := 1;
    hit3 := temp$49;
    if (counter4 > 10000) {
        assert { :sourceloc "InfeasibleCode02.java",71,8,71,22 } true;
        goto block4;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",71,8,71,22 } true;
    }
    goto block5;
  block4:
    return;
  block5:
  block3:
    if (hit3 == 0) {
        assert { :sourceloc "InfeasibleCode02.java",76,11,76,13 } true;
        goto block6;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",76,11,76,13 } true;
    }
    goto block1;
  block6:
    return;
}


implementation void$InfeasibleCode02$infeasible07$1891($this:ref, $in_parameter__0:ref) returns ($exception:ref){
    
var temp$621 : int;    
var end13 : int;    
var temp$116 : int;    
var temp$823 : int;    
var j14 : int;    
var temp$520 : int;    
var temp$419 : int;    
var temp$924 : int;    
var this10 : ref;    
var temp$015 : int;    
var temp11 : ref;    
var temp$217 : ref;    
var temp$318 : int;    
var repos12 : int;    
var temp$722 : int;
    assume $this != $null;
    this10 := $this;
    temp11 := $in_parameter__0;
    repos12 := -1;
    end13 := -1;
    j14 := end13;
  block7:
    temp$015 := j14;
    temp$116 := temp$015 + 1;
    j14 := temp$116;
    temp$217 := temp11;
    temp$318 := j14;
    assert { :sourceloc "InfeasibleCode02.java",85,7,85,18 } temp$318 < $arrSizeHeap[temp$217] && temp$318 >= 0;
    temp$419 := $intArrHeap[temp$217][temp$318];
    temp$520 := temp$419;
    if (temp$520 == 97) {
        assert { :sourceloc "InfeasibleCode02.java",85,7,85,18 } true;
        goto block8;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",85,7,85,18 } true;
    }
    goto block9;
  block8:
    temp$621 := j14 - end13;
    temp$722 := temp$621 - 1;
    repos12 := temp$722;
  block9:
    if (repos12 == -1) {
        assert { :sourceloc "InfeasibleCode02.java",88,11,88,21 } true;
        goto block10;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",88,11,88,21 } true;
    }
    goto block11;
  block10:
    assert { :sourceloc "InfeasibleCode02.java",88,26,88,40 } temp11 != $null;
    temp$823 := $arrSizeHeap[temp11];
    if (j14 < temp$823) {
        assert { :sourceloc "InfeasibleCode02.java",83,2,88,42 } true;
        goto block7;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",88,26,88,40 } true;
    }
    goto block11;
    goto block7;
  block11:
    if (repos12 == -1) {
        assert { :sourceloc "InfeasibleCode02.java",89,6,89,16 } true;
        goto block12;
    } else {
        assert { :sourceloc "InfeasibleCode02.java",89,6,89,16 } true;
    }
    goto block13;
  block12:
    temp$924 := 0;
    repos12 := temp$924;
  block13:
    return;
}


implementation void$InfeasibleCode02$$la$init$ra$$1892($this:ref) returns ($exception:ref){
    
var this25 : ref;
    assume $this != $null;
    this25 := $this;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this25);
    return;
}


