type ref;
type javaType;
type Field $GenericType__0;
type $heap_type = <$GenericType__0>[ref,Field $GenericType__0]$GenericType__0;
type boolArrHeap_type = [ref][int]bool;
type refArrHeap_type = [ref][int]ref;
type realArrHeap_type = [ref][int]int;
type intArrHeap_type = [ref][int]int;
const unique $type : Field javaType;
const unique $alloc : Field bool;
const unique $null : ref;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.Object : javaType extends  complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique FalsePositives01 : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.util.Enumeration : javaType extends  unique java.lang.Object complete;
const unique $StringConst0 : ref extends  complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.io.Serializable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.Comparable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.CharSequence : javaType extends  unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.String : javaType extends  unique java.lang.CharSequence, unique java.io.Serializable, unique java.lang.Comparable, unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.Throwable : javaType extends  unique java.io.Serializable, unique java.lang.Object complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.Exception : javaType extends  unique java.lang.Throwable complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.RuntimeException : javaType extends  unique java.lang.Exception complete;
const { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } unique java.lang.ClassCastException : javaType extends  unique java.lang.RuntimeException complete;
const unique $StringConst1 : ref extends  complete;
const unique $StringConst2 : ref extends  complete;
const unique $StringConst3 : ref extends  complete;
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
var java.lang.String$lp$$rp$$FalsePositives01$sourceKeys234 : Field ref;
var int$FalsePositives01$countSource0 : Field int;
var int$FalsePositives01$beginEndTag0 : Field int;
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
axiom (forall t : javaType :: $heap[$null,$type] <: t);
procedure $new(obj_type:javaType) returns ($obj:ref);        ensures $heap[$obj,$alloc] == true;    ensures $heap[$obj,$type] == obj_type;    requires $heap[$obj,$alloc] == false;    ensures $obj != $null;

procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref);        ensures $heap[$other,$alloc] == true;    ensures $other != $null;    ensures $heap[$other,$type] == $heap[$this,$type];

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) returns ($return:int);    

procedure int$FalsePositives01$foo$1889($this:ref) returns ($return:int, $exception:ref);    

procedure java.util.Enumeration$FalsePositives01$bar$1890($this:ref) returns ($return:ref, $exception:ref);    

procedure void$FalsePositives01$removeAttribute$1891($this:ref) returns ($exception:ref);    

procedure java.lang.String$java.lang.String$toString$124($this:ref) returns ($return:ref, $exception:ref);    

procedure void$FalsePositives01$FalsePositive01$1892($this:ref) returns ($exception:ref);    modifies $heap;

procedure void$FalsePositives01$FalsePositive02$1893($this:ref, $in_parameter__0:ref) returns ($exception:ref);    modifies $heap;

procedure java.lang.String$java.lang.Object$toString$44($this:ref) returns ($return:ref, $exception:ref);    

procedure void$FalsePositives01$FalsePositive03$1894($this:ref, $in_parameter__0:ref, $in_parameter__1:ref, $in_parameter__2:int) returns ($exception:ref);    

procedure int$java.lang.String$indexOf$102($this:ref, $in_parameter__0:ref, $in_parameter__1:int) returns ($return:int, $exception:ref);    

procedure void$FalsePositives01$$la$init$ra$$1895($this:ref) returns ($exception:ref);    

procedure void$java.lang.Object$$la$init$ra$$38($this:ref) returns ($exception:ref);    

implementation int$FalsePositives01$foo$1889($this:ref) returns ($return:int, $exception:ref){
    
var temp$02 : int;    
var this1 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",23,2,23,29 } true;
    this1 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",23,2,23,29 } true;
    temp$02 := 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",23,20,23,28 } true;
    $return := temp$02;
    return;
}


implementation java.util.Enumeration$FalsePositives01$bar$1890($this:ref) returns ($return:ref, $exception:ref){
    
var this3 : ref;    
var temp$04 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assume $heap[$return,$type] <: java.util.Enumeration;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",24,2,24,40 } true;
    this3 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",24,2,24,40 } true;
    temp$04 := $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",24,28,24,39 } true;
    $return := temp$04;
    return;
}


implementation void$FalsePositives01$removeAttribute$1891($this:ref) returns ($exception:ref){
    
var temp$1119 : int;    
var temp$816 : int;    
var temp$311 : ref;    
var temp$917 : ref;    
var temp$210 : int;    
var temp$08 : ref;    
var cce21 : ref;    
var temp$19 : int;    
var hit6 : int;    
var temp$1220 : int;    
var temp$1018 : ref;    
var temp$513 : ref;    
var temp$412 : int;    
var temp$715 : ref;    
var countSource7 : int;    
var this5 : ref;    
var temp$614 : int;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",26,2,52,2 } true;
    this5 := $this;
  block1:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",26,2,52,2 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",31,4,31,23 } true;
    hit6 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,8,32,26 } true;
    countSource7 := 0;
  block2:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,8,32,26 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } true;
    temp$08 := $heap[this5,java.lang.String$lp$$rp$$FalsePositives01$sourceKeys234];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } temp$08 != $null;
    temp$19 := $arrSizeHeap[temp$08];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } true;
    if (countSource7 < temp$19) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } true;
        goto block3;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } true;
    goto block4;
  block3:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,43,32,52 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",34,5,34,7 } true;
    temp$210 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",34,5,34,7 } true;
    hit6 := temp$210;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
    temp$311 := $heap[this5,java.lang.String$lp$$rp$$FalsePositives01$sourceKeys234];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
    temp$412 := countSource7;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } temp$412 < $arrSizeHeap[temp$311] && temp$412 >= 0;
    temp$513 := $refArrHeap[temp$311][temp$412];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
    if (temp$513 == $StringConst0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
        goto block5;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
    goto block6;
  block5:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",35,8,35,17 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",37,6,37,8 } true;
    temp$614 := 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",37,6,37,8 } true;
    hit6 := temp$614;
  block6:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",37,6,37,8 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",41,9,41,11 } true;
    if (hit6 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",41,9,41,11 } true;
        goto block7;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",41,9,41,11 } true;
    goto block8;
  block7:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",41,9,41,11 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } true;
    temp$715 := $heap[this5,java.lang.String$lp$$rp$$FalsePositives01$sourceKeys234];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } true;
    temp$816 := countSource7;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } temp$816 < $arrSizeHeap[temp$715] && temp$816 >= 0;
    temp$917 := $refArrHeap[temp$715][temp$816];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } temp$917 != $null;
    call temp$1018, $exception := java.lang.String$java.lang.String$toString$124(temp$917);
    if ($exception != $null) {
        if ($heap[$exception,$type] <: java.lang.ClassCastException) {
            assert { :clone } true;
            goto block9;
        } else {
            assert { :clone } true;
        }
    }
  block8:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",44,6,44,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,62,32,74 } true;
    temp$1119 := countSource7;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,62,32,72 } true;
    temp$1220 := temp$1119 + 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,62,32,72 } true;
    countSource7 := temp$1220;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,4,46,4 } true;
    goto block2;
  block4:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",32,4,46,4 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",28,3,51,3 } true;
    goto block10;
  block9:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",28,3,51,3 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",48,3,51,3 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",48,3,51,3 } $exception != $null;
    assume $heap[$exception,$type] <: java.lang.Throwable;
    cce21 := $exception;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",48,3,51,3 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",48,3,51,3 } true;
    goto block10;
  block10:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",48,3,51,3 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",48,3,51,3 } true;
    return;
}


implementation void$FalsePositives01$FalsePositive01$1892($this:ref) returns ($exception:ref){
    
var temp$328 : int;    
var counter24 : int;    
var temp$227 : int;    
var temp$025 : int;    
var hit23 : int;    
var this22 : ref;    
var temp$530 : int;    
var temp$126 : int;    
var temp$429 : int;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",55,2,72,2 } true;
    this22 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",56,3,56,22 } true;
    hit23 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",57,3,57,18 } true;
    counter24 := 0;
  block11:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",58,3,70,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",60,4,60,6 } true;
    temp$025 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",60,4,60,6 } true;
    hit23 := temp$025;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",61,7,61,17 } true;
    temp$126 := $heap[this22,int$FalsePositives01$countSource0];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",61,7,61,17 } true;
    if (temp$126 > counter24) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",61,7,61,17 } true;
        goto block12;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",61,7,61,17 } true;
    goto block13;
  block12:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",61,7,61,17 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",63,5,63,14 } true;
    temp$227 := counter24;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",63,5,63,11 } true;
    temp$328 := temp$227 + 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",63,5,63,11 } true;
    counter24 := temp$328;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",64,5,64,7 } true;
    temp$429 := 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",64,5,64,7 } true;
    hit23 := temp$429;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",65,8,65,22 } true;
    if (counter24 > 10000) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",65,8,65,22 } true;
        goto block14;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",65,8,65,22 } true;
    goto block15;
  block14:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",65,8,65,22 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",67,6,67,12 } true;
    return;
  block15:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",67,6,67,12 } true;
  block13:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",67,6,67,12 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",67,6,67,12 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",70,11,70,13 } true;
    if (hit23 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",70,11,70,13 } true;
        goto block16;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",70,11,70,13 } true;
    goto block11;
  block16:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",70,11,70,13 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",71,3,71,13 } true;
    temp$530 := 3;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",71,3,71,13 } true;
    $heap := $heap[this22,int$FalsePositives01$countSource0 := temp$530];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",71,3,71,13 } true;
    return;
}


implementation void$FalsePositives01$FalsePositive02$1893($this:ref, $in_parameter__0:ref) returns ($exception:ref){
    
var temp$336 : ref;    
var temp$740 : int;    
var temp$437 : int;    
var temp$538 : int;    
var this31 : ref;    
var temp$841 : int;    
var htmlTag32 : ref;    
var temp$639 : ref;    
var temp$033 : ref;    
var temp$134 : int;    
var temp$235 : int;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assume $heap[$in_parameter__0,$type] <: java.lang.Object;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",74,2,87,2 } true;
    this31 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",74,30,74,43 } true;
    htmlTag32 := $in_parameter__0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } htmlTag32 != $null;
    call temp$033, $exception := java.lang.String$java.lang.Object$toString$44(htmlTag32);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } temp$033 != $null;
    call temp$134 := int$java.lang.String$compareTo$87(temp$033, $StringConst1);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } true;
    if (temp$134 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } true;
        goto block17;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } true;
    goto block18;
  block17:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,7,75,47 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",77,4,77,14 } true;
    temp$235 := 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",77,4,77,14 } true;
    $heap := $heap[this31,int$FalsePositives01$countSource0 := temp$235];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,3,86,3 } true;
    goto block19;
  block18:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",75,3,86,3 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } true;
    call temp$336, $exception := java.lang.String$java.lang.Object$toString$44(htmlTag32);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } temp$336 != $null;
    call temp$437 := int$java.lang.String$compareTo$87(temp$336, $StringConst2);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } true;
    if (temp$437 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } true;
        goto block20;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } true;
    goto block21;
  block20:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,12,79,49 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",81,4,81,14 } true;
    temp$538 := 2;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",81,4,81,14 } true;
    $heap := $heap[this31,int$FalsePositives01$countSource0 := temp$538];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,8,86,3 } true;
    goto block22;
  block21:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",79,8,86,3 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } true;
    call temp$639, $exception := java.lang.String$java.lang.Object$toString$44(htmlTag32);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } temp$639 != $null;
    call temp$740 := int$java.lang.String$compareTo$87(temp$639, $StringConst3);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } true;
    if (temp$740 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } true;
        goto block23;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } true;
    goto block24;
  block23:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",83,12,83,49 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",85,4,85,14 } true;
    temp$841 := 3;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",85,4,85,14 } true;
    $heap := $heap[this31,int$FalsePositives01$countSource0 := temp$841];
  block24:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",85,4,85,14 } true;
  block22:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",85,4,85,14 } true;
  block19:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",85,4,85,14 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",85,4,85,14 } true;
    return;
}


implementation void$FalsePositives01$FalsePositive03$1894($this:ref, $in_parameter__0:ref, $in_parameter__1:ref, $in_parameter__2:int) returns ($exception:ref){
    
var temp$453 : int;    
var temp$756 : int;    
var this42 : ref;    
var searchString44 : ref;    
var temp$049 : int;    
var temp$251 : int;    
var flaghitup47 : int;    
var temphitpoint46 : int;    
var temp$554 : int;    
var temp$655 : int;    
var source43 : ref;    
var temp$150 : int;    
var interncaret45 : int;    
var hitUp48 : int;    
var temp$857 : int;    
var temp$352 : int;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assume $heap[$in_parameter__1,$type] <: java.lang.String;
    assume $heap[$in_parameter__0,$type] <: java.lang.String;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",90,2,111,2 } true;
    this42 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",90,30,90,42 } true;
    source43 := $in_parameter__0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",90,45,90,63 } true;
    searchString44 := $in_parameter__1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",90,66,90,80 } true;
    interncaret45 := $in_parameter__2;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",91,3,91,24 } true;
    temphitpoint46 := -1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",92,3,92,28 } true;
    flaghitup47 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",93,3,93,16 } true;
    hitUp48 := 0;
  block25:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",94,3,104,21 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",96,4,96,12 } true;
    temp$049 := 0;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",96,4,96,12 } true;
    flaghitup47 := temp$049;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",97,4,97,60 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",97,4,97,60 } source43 != $null;
    call temp$150, $exception := int$java.lang.String$indexOf$102(source43, searchString44, interncaret45);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",97,4,97,15 } true;
    temphitpoint46 := temp$150;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,7,98,22 } true;
    if (temphitpoint46 > 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,7,98,22 } true;
        goto block26;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,7,98,22 } true;
    goto block27;
  block26:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,7,98,22 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,42,98,52 } true;
    temp$251 := $heap[this42,int$FalsePositives01$beginEndTag0];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,42,98,52 } true;
    if (temphitpoint46 < temp$251) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,42,98,52 } true;
        goto block28;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,42,98,52 } true;
    goto block27;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,42,98,52 } true;
    goto block28;
  block28:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",98,42,98,52 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",100,5,100,12 } true;
    temp$352 := hitUp48;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",100,5,100,9 } true;
    temp$453 := temp$352 + 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",100,5,100,9 } true;
    hitUp48 := temp$453;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",101,5,101,13 } true;
    temp$554 := 1;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",101,5,101,13 } true;
    flaghitup47 := temp$554;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",102,5,102,55 } true;
    temp$655 := temphitpoint46;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",102,5,102,55 } true;
    temp$756 := $stringSizeHeap[searchString44];
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",102,5,102,55 } true;
    temp$857 := temp$655 + temp$756;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",102,5,102,15 } true;
    interncaret45 := temp$857;
  block27:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",102,5,102,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",102,5,102,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",104,11,104,19 } true;
    if (flaghitup47 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",104,11,104,19 } true;
        goto block29;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",104,11,104,19 } true;
    goto block25;
  block29:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",104,11,104,19 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",105,6,105,15 } true;
    if (hitUp48 == 0) {
        assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",105,6,105,15 } true;
        goto block30;
    }
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",105,6,105,15 } true;
    goto block31;
  block30:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",105,6,105,15 } true;
  block31:
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",105,6,105,15 } true;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",105,6,105,15 } true;
    return;
}


implementation void$FalsePositives01$$la$init$ra$$1895($this:ref) returns ($exception:ref){
    
var this58 : ref;
    assume { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",-1,-1,-1,-1 } $this != $null;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",4,-1,-1,-1 } true;
    this58 := $this;
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",4,-1,-1,-1 } true;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this58);
    assert { :sourceloc "/Users/schaef/git/jar2bpl/jar2bpl_test/regression/false_positives/fp01/FalsePositives01.java",4,-1,-1,-1 } true;
    return;
}


