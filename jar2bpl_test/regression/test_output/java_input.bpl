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
const { :sourceloc "Object.java",-1,-1,-1,-1 } unique java.lang.Object : javaType extends  complete;
const { :sourceloc "FalsePositives04.java",-1,-1,-1,-1 } unique FalsePositives04 : javaType extends  unique java.lang.Object complete;
const { :sourceloc "Serializable.java",-1,-1,-1,-1 } unique java.io.Serializable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "Throwable.java",-1,-1,-1,-1 } unique java.lang.Throwable : javaType extends  unique java.io.Serializable, unique java.lang.Object complete;
const { :sourceloc "Exception.java",-1,-1,-1,-1 } unique java.lang.Exception : javaType extends  unique java.lang.Throwable complete;
const { :sourceloc "RuntimeException.java",-1,-1,-1,-1 } unique java.lang.RuntimeException : javaType extends  unique java.lang.Exception complete;
const { :sourceloc "NullPointerException.java",-1,-1,-1,-1 } unique java.lang.NullPointerException : javaType extends  unique java.lang.RuntimeException complete;
const { :sourceloc "Comparable.java",-1,-1,-1,-1 } unique java.lang.Comparable : javaType extends  unique java.lang.Object complete;
const { :sourceloc "CharSequence.java",-1,-1,-1,-1 } unique java.lang.CharSequence : javaType extends  unique java.lang.Object complete;
const { :sourceloc "String.java",-1,-1,-1,-1 } unique java.lang.String : javaType extends  unique java.io.Serializable, unique java.lang.CharSequence, unique java.lang.Comparable, unique java.lang.Object complete;
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
var java.lang.Object$FalsePositives04$first234 : Field ref;
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
procedure $new(obj_type:javaType) returns ($obj:ref);    ensures $heap[$obj,$type] == obj_type;    requires $heap[$obj,$alloc] == false;        ensures $obj != $null;    ensures $heap[$obj,$alloc] == true;

procedure java.lang.Object$java.lang.Object$clone$43($this:ref) returns ($other:ref);        ensures $other != $null;    ensures $heap[$other,$alloc] == true;    ensures $heap[$other,$type] == $heap[$this,$type];

procedure int$java.lang.String$compareTo$87($this:ref, $other:ref) returns ($return:int);    

procedure java.lang.Object$FalsePositives04$peekFirst$1890($this:ref) returns ($return:ref, $exception:ref);    modifies $heap;

procedure java.lang.Object$FalsePositives04$foo$1889($this:ref) returns ($return:ref, $exception:ref);    modifies $heap;

procedure java.lang.String$java.lang.Object$toString$44($this:ref) returns ($return:ref, $exception:ref);    

procedure void$FalsePositives04$$la$init$ra$$1891($this:ref) returns ($exception:ref);    

procedure void$java.lang.Object$$la$init$ra$$38($this:ref) returns ($exception:ref);    

implementation java.lang.Object$FalsePositives04$peekFirst$1890($this:ref) returns ($return:ref, $exception:ref){
    
var temp$24 : ref;    
var this1 : ref;    
var temp$35 : ref;    
var temp$13 : ref;    
var temp$68 : ref;    
var temp$57 : ref;    
var temp$79 : ref;    
var temp$02 : ref;    
var temp$46 : ref;
    assume $this != $null;
    assume $heap[$return,$type] <: java.lang.Object;
    assert { :sourceloc "FalsePositives04.java",8,5,17,5 } true;
    this1 := $this;
  block1:
    assert { :sourceloc "FalsePositives04.java",8,5,17,5 } true;
    assert { :sourceloc "FalsePositives04.java",10,18,10,22 } true;
    call temp$02, $exception := java.lang.Object$FalsePositives04$foo$1889(this1);
    if ($exception != $null && $heap[$exception,$type] <: java.lang.Throwable) {
        goto block2;
    }
    assert { :sourceloc "FalsePositives04.java",10,10,10,14 } true;
    $heap := $heap[this1,java.lang.Object$FalsePositives04$first234 := temp$02];
    assert { :sourceloc "FalsePositives04.java",11,20,11,24 } true;
    temp$13 := $heap[this1,java.lang.Object$FalsePositives04$first234];
    assert { :sourceloc "FalsePositives04.java",11,20,11,24 } true;
    if (temp$13 != $null) {
        assert { :sourceloc "FalsePositives04.java",11,20,11,24 } true;
    } else {
        assert { :noverify } true;
        call $exception := $new(java.lang.NullPointerException);
        goto block2;
    }
    call temp$24, $exception := java.lang.String$java.lang.Object$toString$44(temp$13);
    if ($exception != $null && $heap[$exception,$type] <: java.lang.Throwable) {
        goto block2;
    }
  block3:
    assert { :sourceloc "FalsePositives04.java",11,20,11,24 } true;
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } this1 != $null;
    temp$35 := $heap[this1,java.lang.Object$FalsePositives04$first234];
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    if (temp$35 == $null) {
        assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
        goto block4;
    } else {
        assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    }
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    goto block5;
  block4:
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    temp$46 := $null;
    assert { :sourceloc "FalsePositives04.java",14,11,14,22 } true;
    $return := temp$46;
    return;
  block5:
    assert { :sourceloc "FalsePositives04.java",14,11,14,22 } true;
    assert { :sourceloc "FalsePositives04.java",11,13,11,36 } true;
    $return := temp$24;
    return;
  block2:
    assert { :sourceloc "FalsePositives04.java",11,13,11,36 } true;
    assert { :sourceloc "FalsePositives04.java",9,9,16,9 } true;
    assert { :sourceloc "FalsePositives04.java",9,9,16,9 } $exception != $null;
    assume $heap[$exception,$type] <: java.lang.Throwable;
    temp$57 := $exception;
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } this1 != $null;
    temp$68 := $heap[this1,java.lang.Object$FalsePositives04$first234];
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    if (temp$68 == $null) {
        assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
        goto block6;
    } else {
        assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    }
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    goto block7;
  block6:
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    assert { :sourceloc "FalsePositives04.java",13,14,13,18 } { :clone } true;
    temp$79 := $null;
    assert { :sourceloc "FalsePositives04.java",14,11,14,22 } true;
    $return := temp$79;
    return;
  block7:
    assert { :sourceloc "FalsePositives04.java",14,11,14,22 } true;
    assert { :sourceloc "FalsePositives04.java",14,11,14,22 } true;
    $exception := temp$57;
    return;
}


implementation void$FalsePositives04$$la$init$ra$$1891($this:ref) returns ($exception:ref){
    
var this10 : ref;
    assume $this != $null;
    assert { :sourceloc "FalsePositives04",3,-1,-1,-1 } true;
    this10 := $this;
    assert { :sourceloc "FalsePositives04",3,-1,-1,-1 } true;
    call $exception := void$java.lang.Object$$la$init$ra$$38(this10);
    assert { :sourceloc "FalsePositives04",3,-1,-1,-1 } true;
    return;
}


